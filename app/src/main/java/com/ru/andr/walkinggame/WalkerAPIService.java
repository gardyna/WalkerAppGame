package com.ru.andr.walkinggame;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ragnar on 10/9/15.
 */
public interface WalkerAPIService {
    @GET("/friends/")
    Callback<List<User>> getMyFriends();

    @POST("/login/")
    Call<String> basicLogin(@Body User user);
}
