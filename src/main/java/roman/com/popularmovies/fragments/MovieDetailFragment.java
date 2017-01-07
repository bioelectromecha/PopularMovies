package roman.com.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import roman.com.popularmovies.R;
import roman.com.popularmovies.dataobjects.movies.Movie;
import roman.com.popularmovies.dataobjects.movies.MoviesHolder;
import roman.com.popularmovies.dataobjects.reviews.Review;
import roman.com.popularmovies.dataobjects.reviews.ReviewsHolder;
import roman.com.popularmovies.dataobjects.trailers.Trailer;
import roman.com.popularmovies.dataobjects.trailers.TrailersHolder;
import roman.com.popularmovies.network.ApiCallback;
import roman.com.popularmovies.network.ApiManager;
import roman.com.popularmovies.utils.Constants;


public class MovieDetailFragment extends Fragment implements ApiCallback, View.OnClickListener {
    private static final String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private ApiManager mApiManager = ApiManager.getInstance();
    private boolean mIsFavorite = false;
    private List<Trailer> mTrailerList = null;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Movie movie = getActivity().getIntent().getExtras().getParcelable(Constants.KEY_MOVIE_PARCEL);

        //load the cover image
        ImageView coverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image);
        Picasso.with(getContext())
                .load(movie.getBackdropPath())
                .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                .into(coverImageView);

        //load the poster image
        ImageView posterImageView = (ImageView) view.findViewById(R.id.fragment_detail_poster_image);
        Picasso.with(getContext())
                .load(movie.getPosterPath())
                .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                .into(posterImageView);

        TextView textview = (TextView) view.findViewById(R.id.movie_title);
        textview.setText(movie.getTitle());

        textview = (TextView) view.findViewById(R.id.movie_date);
        textview.setText(movie.getReleaseDate());

        textview = (TextView) view.findViewById(R.id.movie_rating);
        textview.setText(String.valueOf(movie.getVoteAverage()));

        textview = (TextView) view.findViewById(R.id.movie_description);
        textview.setText(movie.getOverview());

        //TODO remove this, it's really not needed
        getActivity().setTitle(movie.getTitle());

        mApiManager.getReviews(new WeakReference<ApiCallback>(this), movie.getId());
        mApiManager.getTrailers(new WeakReference<ApiCallback>(this), movie.getId());
    }

    @Override
    public void onFailure(Throwable error) {
        LogUtils.d("getReviews failed");
    }

    @Override
    public void onMoviesFetchSuccess(MoviesHolder moviesHolder) {
        //ignore this here - it's used only where you actually need to fetch a list of movies
    }

    @Override
    public void onReviewsFetchSuccess(ReviewsHolder reviewsHolder) {
        onReviewsLoaded(reviewsHolder.getReviews());
    }

    @Override
    public void onTrailersFetchSuccess(TrailersHolder trailersHolder) {
        onTrailersLoaded(trailersHolder.getTrailers());
    }

    /**
     * this is called after the trailers list have been loaded, to add the trailers to the view
     *
     * @param trailers
     */
    private void onTrailersLoaded(List<Trailer> trailers) {
        mTrailerList = trailers;
        //this is the container of the container of the trailers
        View trailersMetaContainer = getView().findViewById(R.id.fragment_detail_trailers_meta_container);
        //this is the container to which the reviews are added
        ViewGroup trailersContainer = (ViewGroup) trailersMetaContainer.findViewById(R.id.fragment_detail_trailers_container);

        // Remove all existing trailers, 1>= because there is one element that should no be removed! (the one that says "trailers"
        for (int i = trailersContainer.getChildCount() - 1; i >= 1; i--) {
            trailersContainer.removeViewAt(i);
        }

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        //make the reviews visible if there's at least one review
        if (trailers.isEmpty()) {
            trailersMetaContainer.setVisibility(View.GONE);
        } else {
            trailersMetaContainer.setVisibility(View.VISIBLE);
            //this counter is used to put the number of the review on the opening textview in the review view
            int counter = 0;
            for (Trailer trailer : trailers) {
                counter++;
                if (TextUtils.isEmpty(trailer.getName())) {
                    continue;
                }

                final View trailersView = inflater.inflate(R.layout.trailer_item, trailersContainer, false);
                final TextView trailerNumberView = (TextView) trailersView.findViewById(R.id.trailer_number);
                final TextView trailerNameView = (TextView) trailersView.findViewById(R.id.trailer_name);

                trailerNumberView.setText(trailerNumberView.getText() + " " + String.valueOf(counter));
                trailerNameView.setText(trailer.getName());
                trailersContainer.addView(trailersView);
                trailersView.setOnClickListener(this);
            }
        }

    }


    /**
     * the trailer onclick listener
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        ViewGroup trailersContainer = (ViewGroup) getView().findViewById(R.id.fragment_detail_trailers_container);
        int position = trailersContainer.indexOfChild(view) - 1;

        // http://www.youtube.com/watch?v=cxLG2wtE7TM
        String youtubeVideoUrl = BASE_YOUTUBE_URL + mTrailerList.get(position).getKey();
        //launch and play the video
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl)));
    }

    /**
     * this is called after the reviews list has been loaded. used to add the reviews to the view
     *
     * @param reviews
     */
    private void onReviewsLoaded(List<Review> reviews) {
        //this is the container of the container of the reviews
        View reviewsMetaContainer = getView().findViewById(R.id.fragment_detail_reviews_meta_container);
        //this is the container to which the reviews are added
        ViewGroup reviewsContainer = (ViewGroup) reviewsMetaContainer.findViewById(R.id.fragment_detail_reviews_container);

        // Remove all existing reviews, 1>= because there is one element that should no be removed! (the one that says "reviews"
        for (int i = reviewsContainer.getChildCount() - 1; i >= 1; i--) {
            reviewsContainer.removeViewAt(i);
        }

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        //make the reviews visible if there's at least one review
        if (reviews.isEmpty()) {
            reviewsMetaContainer.setVisibility(View.GONE);
        } else {
            reviewsMetaContainer.setVisibility(View.VISIBLE);
            //this counter is used to put the number of the review on the opening textview in the review view
            int counter = 0;
            for (Review review : reviews) {
                counter++;
                if (TextUtils.isEmpty(review.getAuthor())) {
                    continue;
                }

                final View reviewView = inflater.inflate(R.layout.review_item, reviewsContainer, false);
                final TextView reviewNumberView = (TextView) reviewView.findViewById(R.id.review_number);
                final TextView reviewAuthorView = (TextView) reviewView.findViewById(R.id.review_author);
                final TextView reviewContentView = (TextView) reviewView.findViewById(R.id.review_content);

                reviewNumberView.setText(reviewNumberView.getText() + " " + String.valueOf(counter));
                reviewAuthorView.setText(review.getAuthor());
                reviewContentView.setText(review.getContent());

                reviewsContainer.addView(reviewView);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_favorite_icon) {
            if (!mIsFavorite) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_full, getActivity().getTheme()));
                mIsFavorite = true;
                makeMovieFavorite();
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_empty, getActivity().getTheme()));
                mIsFavorite = false;
                unmakeMovieFavorite();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void unmakeMovieFavorite() {

    }

    private void makeMovieFavorite() {
        ImageView coverImageView = (ImageView) getView().findViewById(R.id.fragment_detail_cover_image);

    }

}


