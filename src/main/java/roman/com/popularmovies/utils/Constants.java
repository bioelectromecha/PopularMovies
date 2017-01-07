package roman.com.popularmovies.utils;


/**
 * general app keys and constants
 */
public class Constants {

    // TMDb api key - left empty because of license restrictions
    public static final String API_KEY = "";

    //key to retrieve and store the parcel in the intent
    public static final String KEY_MOVIE_PARCEL = "MOVIE_PARCEL";
    public static final String KEY_SHARED_PREFERENCES = "SHARED_PREFERENCES";

    //MoviesFragment display modes
    public static final String KEY_HIGHEST_RATED = "HIGHEST_RATED";
    public static final String KEY_MOST_POPULAR = "MOST_POPULAR";
    public static final String KEY_FAVORITES = "FAVORITES";

    //base youtube url for youtube app intents
    public static final String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    //wasp database stuff
    public static final String DATABASE_NAME = "myDb";
    public static final String DATABASE_PASSWORD = "passw0rd";
    public static final String KEY_MOVIES_HASH = "movies";
    public static final String KEY_TRAILERS_HASH = "trailers";
    public static final String KEY_REVIEWS_HASH = "reviews";

    //file path for images
    public static final String IMAGE_DIRECTORY_NAME = "images_dir";

    //image types
    public static final String COVER_IMAGE_TYPE = "COVER";
    public static final String POSTER_IMAGE_TYPE = "POSTER";




}
