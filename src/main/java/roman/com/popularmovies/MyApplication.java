package roman.com.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import net.rehacktive.waspdb.WaspDb;
import net.rehacktive.waspdb.WaspFactory;
import net.rehacktive.waspdb.WaspHash;

import java.io.File;

import roman.com.popularmovies.utils.Constants;

/**
 * Created by roman on 10/8/16.
 */

public class MyApplication extends Application {
    private static Context sContext;

    public static Context getContext() {
        return MyApplication.sContext;
    }

    public static SharedPreferences getSharePreferences() {
        return sContext.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public static File getImageDir(){
        File imageDirectory = new File(sContext.getFilesDir(), Constants.IMAGE_DIRECTORY_NAME);
        imageDirectory.mkdirs();
        return imageDirectory;
    }

    private static WaspDb getDatabase() {
        String path = sContext.getFilesDir().getPath();
        WaspDb db = WaspFactory.openOrCreateDatabase(path, Constants.DATABASE_NAME, Constants.DATABASE_PASSWORD);
        return db;
    }

    public static WaspHash getMoviesDbStore() {
        return getDatabase().openOrCreateHash(Constants.KEY_MOVIES_HASH);
    }

    public static WaspHash getTrailersDbStore() {
        return getDatabase().openOrCreateHash(Constants.KEY_TRAILERS_HASH);
    }

    public static WaspHash getReviewsDbStore() {
        return getDatabase().openOrCreateHash(Constants.KEY_REVIEWS_HASH);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();
    }

}
