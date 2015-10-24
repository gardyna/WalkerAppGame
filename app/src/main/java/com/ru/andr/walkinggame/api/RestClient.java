
package com.ru.andr.walkinggame.api;

import com.ru.andr.walkinggame.model.Result;
import com.ru.andr.walkinggame.model.User;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;

/**
 * Created by ragnar on 10/24/15.
 */
public class RestClient {

    private final String BASE_URL = "http://127.0.0.1:8080";

    protected Retrofit restAdapter;
    protected RestInterface mApi;

    public RestClient()
    {

        restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public Retrofit getRestAdapter()
    {
        return restAdapter;
    }

    public RestInterface getRestInterface()
    {
        return mApi;
    }
}
