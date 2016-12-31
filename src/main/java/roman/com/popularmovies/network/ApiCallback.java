package roman.com.popularmovies.network;

import roman.com.popularmovies.dataobjects.movies.MoviesHolder;
import roman.com.popularmovies.dataobjects.reviews.ReviewsHolder;
import roman.com.popularmovies.dataobjects.trailers.TrailersHolder;

/**
 * an interface that should be implemented by clients that ask for Data from {@code ApiManager}
 */
public interface ApiCallback {

    /**
     * Fetch request failed.
     *
     * @param error
     */
    void onFailure(Throwable error);

    /**
     * Fetch movies request succeeded.
     */
    void onMoviesFetchSuccess(MoviesHolder moviesHolder);

    void onReviewsFetchSuccess(ReviewsHolder reviewsHolder);

    void onTrailersFetchSuccess(TrailersHolder trailersHolder);
}
