package roman.com.popularmovies.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import roman.com.popularmovies.dataobjects.MoviesHolder;


/**
 * this class defines the endpoints we'll be accessing to get the popular movies data
 */
public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesHolder> getTopRatedMovies(@Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/top_rated?api_key=1234

    @GET("movie/popular")
    Call<MoviesHolder> getPopularMovies(@Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/popular?api_key=1234

    @GET("movie/{id}")
    Call<MoviesHolder> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/244786?api_key=1234
}