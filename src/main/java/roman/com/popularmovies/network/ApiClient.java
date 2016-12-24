package roman.com.popularmovies.network;

/**
 * Created by richa.khanna on 5/11/16.
 */

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this class is used to instantiate the retrofit networking library and underling http client
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            //we use okhttp as the http client
            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

            //instantiate retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiManager.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}
