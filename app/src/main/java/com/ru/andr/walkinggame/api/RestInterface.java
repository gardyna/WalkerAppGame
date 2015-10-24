package com.ru.andr.walkinggame.api;

import com.ru.andr.walkinggame.model.Result;
import com.ru.andr.walkinggame.model.User;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ragnar on 10/24/15.
 */
public interface RestInterface {

    @GET("/friends/")
    void getFriendsList(Callback<JSONObject> callback);

    @POST("/login/")
    Call<Result> userLogin(@Body User user);
    //void userLogin(@Body User user, Callback<JSONObject> callback);

    @POST("/register/")
    Call<Result>  registerUser(@Body User user);
}
