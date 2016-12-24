package roman.com.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import roman.com.popularmovies.utils.Constants;

/**
 * Created by roman on 10/8/16.
 */

public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();
    }

    public static Context getContext() {
        return MyApplication.sContext;
    }

    public static SharedPreferences getSharePreferences() {
        return sContext.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
    }


}
