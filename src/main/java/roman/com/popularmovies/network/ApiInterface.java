package roman.com.popularmovies.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import roman.com.popularmovies.dataobjects.movies.MoviesHolder;
import roman.com.popularmovies.dataobjects.reviews.ReviewsHolder;
import roman.com.popularmovies.dataobjects.trailers.TrailersHolder;


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

    //not used
    @GET("movie/{id}")
    Call<MoviesHolder> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/244786?api_key=1234

    @GET("movie/{id}/reviews")
    Call<ReviewsHolder> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/244786/reviews?api_key=1234

    @GET("movie/{id}/videos")
    Call<TrailersHolder> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/244786/videos?api_key=1234
}