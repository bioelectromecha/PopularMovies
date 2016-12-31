package roman.com.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import roman.com.popularmovies.dataobjects.trailers.TrailersHolder;
import roman.com.popularmovies.network.ApiCallback;
import roman.com.popularmovies.network.ApiManager;
import roman.com.popularmovies.utils.Constants;


public class MovieDetailFragment extends Fragment implements ApiCallback {
    private ApiManager mApiManager = ApiManager.getInstance();

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mApiManager.getReviews(new WeakReference<ApiCallback>(this),movie.getId());

    }

    @Override
    public void onFailure(Throwable error) {
        LogUtils.d("getReviews failed");
    }

    @Override
    public void onMoviesFetchSuccess(MoviesHolder moviesHolder) {
        //ignore this here
    }

    @Override
    public void onReviewsFetchSuccess(ReviewsHolder reviewsHolder) {
        onReviewsLoaded(reviewsHolder.getReviews());
    }

    @Override
    public void onTrailersFetchSuccess(TrailersHolder trailersHolder) {

    }

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
        }else {
            reviewsMetaContainer.setVisibility(View.VISIBLE);
            //this counter is used to put the number of the review on the opening textview in the review view
            int counter=0;
            for (Review review : reviews) {
                counter++;
                if (TextUtils.isEmpty(review.getAuthor())) {
                    continue;
                }

                final View reviewView = inflater.inflate(R.layout.review_item, reviewsContainer, false);
                final TextView reviewNumberView = (TextView) reviewView.findViewById(R.id.review_number);
                final TextView reviewAuthorView = (TextView) reviewView.findViewById(R.id.review_author);
                final TextView reviewContentView = (TextView) reviewView.findViewById(R.id.review_content);

                reviewNumberView.setText(reviewNumberView.getText()+" "+ String.valueOf(counter));
                reviewAuthorView.setText(review.getAuthor());
                reviewContentView.setText(review.getContent());

                reviewsContainer.addView(reviewView);
            }
        }
    }
}
