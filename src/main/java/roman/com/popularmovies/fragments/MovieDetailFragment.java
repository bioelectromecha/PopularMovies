package roman.com.popularmovies.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.squareup.picasso.Picasso;

import net.rehacktive.waspdb.WaspHash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import roman.com.popularmovies.MyApplication;
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

import static roman.com.popularmovies.utils.Constants.BASE_YOUTUBE_URL;


public class MovieDetailFragment extends Fragment implements ApiCallback, View.OnClickListener {
    private Movie mMovie = null;
    private TrailersHolder mTrailersHolder = null;
    private ReviewsHolder mReviewsHolder = null;

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
        mMovie = getActivity().getIntent().getExtras().getParcelable(Constants.KEY_MOVIE_PARCEL);

        //check if the movie is in favorites
        WaspHash moviesDbStore = MyApplication.getMoviesDbStore();
        if (moviesDbStore.get(mMovie.getId()) != null) {
            mIsFavorite = true;
            // this is so images will be loaded from local storage and not from the network
            mMovie = moviesDbStore.get(mMovie.getId());
        }
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

        if (mMovie == null) {
            LogUtils.d("ERROR! NULL MOVIE REFERENCE!");
            throw new IllegalArgumentException("shouldn't be able to even reach movieDetailFragment without a valid movie");
        }


        /***********************
         * Load the cover image
         ***********************/
        ImageView coverImageView = (ImageView) view.findViewById(R.id.fragment_detail_cover_image);

        if (mIsFavorite) {
            File localCoverImage = new File(mMovie.getBackdropPath());
            Picasso.with(getContext())
                    .load(localCoverImage)
                    .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                    .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                    .into(coverImageView);
        } else {
            Picasso.with(getContext())
                    .load(mMovie.getBackdropPath())
                    .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                    .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                    .into(coverImageView);
        }
        /***********************
         * load the poster image
         ***********************/
        ImageView posterImageView = (ImageView) view.findViewById(R.id.fragment_detail_poster_image);

        //if the movie exists locally, load it from the file system
        if (mIsFavorite) {
            File localPosterImage = new File(mMovie.getPosterPath());
            Picasso.with(getContext())
                    .load(localPosterImage)
                    .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                    .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                    .into(posterImageView);
        } else {
            //load the image from network
            Picasso.with(getContext())
                    .load(mMovie.getPosterPath())
                    .placeholder(R.drawable.ic_placeholder) // show this image if not loaded yet
                    .error(R.drawable.ic_image_not_found)      // show this if error or image not exist
                    .into(posterImageView);
        }


        TextView textview = (TextView) view.findViewById(R.id.movie_title);
        textview.setText(mMovie.getTitle());

        textview = (TextView) view.findViewById(R.id.movie_date);
        textview.setText(mMovie.getReleaseDate());

        textview = (TextView) view.findViewById(R.id.movie_rating);
        textview.setText(String.valueOf(mMovie.getVoteAverage()));

        textview = (TextView) view.findViewById(R.id.movie_description);
        textview.setText(mMovie.getOverview());

        //TODO remove this, it's really not needed
        getActivity().setTitle(mMovie.getTitle());

        /**********************
         * load the reviews and trailers
         ********************/

        //load from db if the movie is a favorite
        if (mIsFavorite) {
            loadTrailersFromDb();
            loadReviewsFromDb();
            //load from the network
        } else {
            mApiManager.getTrailers(new WeakReference<ApiCallback>(this), mMovie.getId());
            mApiManager.getReviews(new WeakReference<ApiCallback>(this), mMovie.getId());
        }
    }

    private void loadReviewsFromDb() {
        onReviewsFetchSuccess((ReviewsHolder) MyApplication.getReviewsDbStore().get(mMovie.getId()));
    }

    private void loadTrailersFromDb() {
        onTrailersFetchSuccess((TrailersHolder) MyApplication.getTrailersDbStore().get(mMovie.getId()));
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
        mReviewsHolder = reviewsHolder;
    }

    @Override
    public void onTrailersFetchSuccess(TrailersHolder trailersHolder) {
        onTrailersLoaded(trailersHolder.getTrailers());
        mTrailersHolder = trailersHolder;
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

        //check if the movie is in favorites
        if (mIsFavorite) {
            Toast.makeText(getActivity(), "THE MOVIE IS A FAVORITE!", Toast.LENGTH_SHORT).show();
            //menu.size()-1 is the last item in the menu
            menu.getItem(menu.size() - 1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_full, getActivity().getTheme()));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_favorite_icon) {
            if (!mIsFavorite) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_full, getActivity().getTheme()));
                mIsFavorite = true;
                addMovieToFavorites();
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_empty, getActivity().getTheme()));
                mIsFavorite = false;
                removeMovieFromFavorites();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void removeMovieFromFavorites() {
        deleteFromInternalStorage(mMovie.getPosterPath());
        deleteFromInternalStorage(mMovie.getBackdropPath());

        MyApplication.getMoviesDbStore().remove(mMovie.getId());
        if (mReviewsHolder != null) {
            MyApplication.getReviewsDbStore().remove(mReviewsHolder.getId());
        }
        if (mTrailersHolder != null) {
            MyApplication.getTrailersDbStore().remove(mTrailersHolder.getId());
        }
    }

    private void addMovieToFavorites() {

        //save the poster image to local storage
        ImageView posterImageView = (ImageView) getView().findViewById(R.id.fragment_detail_poster_image);
        Bitmap posterImageBitmap = ((BitmapDrawable) posterImageView.getDrawable()).getBitmap();
        String pathToPosterImage = saveToInternalStorage(posterImageBitmap, String.valueOf(mMovie.getId()) + Constants.POSTER_IMAGE_TYPE);

        //save the cover image to local storage
        ImageView coverImageView = (ImageView) getView().findViewById(R.id.fragment_detail_cover_image);
        Bitmap coverImageBitmap = ((BitmapDrawable) coverImageView.getDrawable()).getBitmap();
        String pathToCoverImage = saveToInternalStorage(coverImageBitmap, String.valueOf(mMovie.getId()) + Constants.COVER_IMAGE_TYPE);

        //update images paths before saving to db
        mMovie.setPosterPath(pathToPosterImage);
        mMovie.setBackdropPath(pathToCoverImage);

        LogUtils.d(mMovie.getPosterPath());
        LogUtils.d(mMovie.getBackdropPath());

        //store the image data to db
        MyApplication.getMoviesDbStore().put(mMovie.getId(), mMovie);
        //store the review data to db
        if (mReviewsHolder != null) {
            MyApplication.getReviewsDbStore().put(mReviewsHolder.getId(), mReviewsHolder);
        }
        //store the trailers data to db
        if (mTrailersHolder != null) {
            MyApplication.getTrailersDbStore().put(mTrailersHolder.getId(), mTrailersHolder);
        }
    }

    /**
     * save an image to the images directory with the given name, and return the absolute path to the newly created image
     *
     * @param bitmapImage
     * @param imageName
     * @return
     */
    private String saveToInternalStorage(Bitmap bitmapImage, String imageName) {
        File directory = MyApplication.getImageDir();
        // Create imageDir
        File myPath = new File(directory, imageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }

    /**
     * delete an image from internal storage at the given path, do nothing if it doesn't exist
     *
     * @param pathToFile
     * @return
     */
    private void deleteFromInternalStorage(String pathToFile) {
        File fileAtPath = new File(pathToFile);
        if (fileAtPath.exists()) {
            fileAtPath.delete();
        }

    }
}


