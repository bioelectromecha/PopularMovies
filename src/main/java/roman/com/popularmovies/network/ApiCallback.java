package roman.com.popularmovies.network;

import roman.com.popularmovies.dataobjects.MoviesHolder;

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
     * Fetch request succeeded.
     */
    void onSuccess(MoviesHolder moviesHolder);
}
