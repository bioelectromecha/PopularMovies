package roman.com.popularmovies.network;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import roman.com.popularmovies.dataobjects.MoviesHolder;
import roman.com.popularmovies.utils.Constants;

/**
 * this class is a singleton through which all TMDb api calls are managed
 */
public class ApiManager {

    // base url for movie queries
    static final String BASE_URL = "http://api.themoviedb.org/3/";
    // base url for movie image queries (i.e for gridview)
    static final String BASE_URL_IMAGE_POSTER = "http://image.tmdb.org/t/p/w185";
    // base url for large image queries (i.e for detail view)
    static final String BASE_URL_IMAGE_BACKDROP = "http://image.tmdb.org/t/p/w780";

    // TMDb api key - left empty because of license restrictions
    private static final String API_KEY = "";

    private static ApiManager mInstance = new ApiManager();
    private ApiInterface mApiService;

    /**
     * get an instanec of the ApiManager singleton object
     * @return
     */
    public static ApiManager getInstance(){
        return mInstance;
    }

    /**
     *     private constructor since this is a singleton
     */
    private ApiManager() {
        init();
    }

    private void init() {
        mApiService = getApiService();
    }

    private ApiInterface getApiService() {
        if (mApiService == null) {
            mApiService = ApiClient.getClient().create(ApiInterface.class);
        }
        return mApiService;
    }

    /**
     * Get a list of movies from TMDb
     *
     * @param wCallback
     */
    public void getMovies(final WeakReference<ApiCallback> wCallback, String movieSortFilter) {

        final Callback<MoviesHolder> objectCallback = new Callback<MoviesHolder>() {
            @Override
            public void onResponse(Call<MoviesHolder> call, Response<MoviesHolder> response) {
                ApiCallback request = null;
                if (wCallback != null) {
                    request = wCallback.get();
                }

                if (request != null) {
                    // response.isSuccessful() is true if the response code is 2xx
                    if (response != null && response.isSuccessful()) {
                        request.onSuccess(response);
                    } else {
                        int statusCode = response.code();
                        // handle response errors yourself
                        ResponseBody errorBody = response.errorBody();
                        try {
                            LogUtils.d("onResponse status code : " + statusCode + " , error message : " + errorBody.string());
                        } catch (IOException e) {
                            LogUtils.d("onResponse exception message : " + e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesHolder> call, Throwable t) {
                // handle execution failures like no internet connectivity
                // timeout exception here IOException or SocketTOException
                ApiCallback req = null;
                if (wCallback != null) {
                    req = wCallback.get();
                }
                if (req != null) {
                    req.onFailure(t);
                }
            }
        };

        Call<MoviesHolder> call;

        if (movieSortFilter.equals(Constants.KEY_HIGHEST_RATED)) {
            call = mApiService.getTopRatedMovies(API_KEY);
            call.enqueue(objectCallback);
        } else if (movieSortFilter.equals(Constants.KEY_MOST_POPULAR)) {
            call = mApiService.getPopularMovies(API_KEY);
            call.enqueue(objectCallback);
        }else{
            throw new IllegalArgumentException("bad movieSortFilter");
        }
    }
}
