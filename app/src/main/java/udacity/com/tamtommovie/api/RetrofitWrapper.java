package udacity.com.tamtommovie.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static udacity.com.tamtommovie.util.Constants.API_BASE_URL;

/**
 * Created by omaraltamimi on 3/3/18.
 */

public class RetrofitWrapper {
    private static volatile RetrofitWrapper mInstance;
    private APIService mApiService;
    private Gson mGson;

    private RetrofitWrapper() {
        mGson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okClient = getOkClient();
        RxJava2CallAdapterFactory rxAdapter =
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .client(okClient)
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(rxAdapter).build();
        mApiService = retrofit.create(APIService.class);
    }

    public static RetrofitWrapper getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitWrapper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitWrapper();
                }
            }
        }
        return mInstance;
    }


    public APIService getApiService() {
        return mApiService;
    }

    private OkHttpClient getOkClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        return okHttpClientBuilder.build();
    }
}
