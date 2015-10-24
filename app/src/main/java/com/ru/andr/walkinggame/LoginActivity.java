package com.ru.andr.walkinggame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ru.andr.walkinggame.api.RestInterface;
import com.ru.andr.walkinggame.model.Result;
import com.ru.andr.walkinggame.model.User;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONObject;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void UserLogin(View view) {
        final User loginUser = new User();
        EditText inputUsername = (EditText) findViewById(R.id.editusername);
        EditText inputPassword = (EditText) findViewById(R.id.editpassword);
        loginUser.username = inputUsername.getText().toString();
        loginUser.password = inputPassword.getText().toString();

        Thread fetchData = new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.68:8080")
                        .client(client)
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                RestInterface mApi = retrofit.create(RestInterface.class);


                try {
                    Call<Result> call = mApi.userLogin(loginUser);
                    Response<Result> rr = call.execute();
                    if(rr.body() != null)
                    {
                        String token = rr.body().content.get("token");
                        if(token != null || !token.isEmpty())
                        {

                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fetchData.start();
    }

    public void registerClicked(View view) {
        Intent newIntern = new Intent(this, RegisterActivity.class);
        this.startActivity(newIntern);
    }
}
