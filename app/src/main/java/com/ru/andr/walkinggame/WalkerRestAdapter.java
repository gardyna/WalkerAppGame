package com.ru.andr.walkinggame;

import android.util.Log;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ragnar on 10/19/15.
 */
public class WalkerRestAdapter {
    protected final String TAG = getClass().getSimpleName();
    protected Retrofit retrofit;
    protected WalkerAPIService mApi;

    static final String API_URL = "http://127.0.0.1:8080";

    public WalkerRestAdapter()
    {
        Log.d("Classsss", "Calling retrofit");
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        Log.d("Classsss", "Calling mApi");
        mApi = retrofit.create(WalkerAPIService.class);
    }

    public Call<String> testLoginApi(User userdata)
    {
        Log.d("Classsss", userdata.username);
        Log.d(TAG, "testLoginApi: for user:" + userdata.username);
        return mApi.basicLogin(userdata);
    }
}
