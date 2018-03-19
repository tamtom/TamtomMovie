package udacity.com.tamtommovie;

import android.app.Application;
import android.content.Context;

import udacity.com.tamtommovie.util.PrefManager;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class MyApplication extends Application {


    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public MyApplication() {
    }

    public static PrefManager getPrefManager() {
        return PrefManager.getInstance(getInstance());
    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
