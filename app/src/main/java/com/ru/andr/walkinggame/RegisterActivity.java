package com.ru.andr.walkinggame;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ru.andr.walkinggame.api.RestInterface;
import com.ru.andr.walkinggame.model.Result;
import com.ru.andr.walkinggame.model.User;
import com.squareup.okhttp.OkHttpClient;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit.Call;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void onRegisterClicked(View view) {
        final User newUser = new User();

        EditText inputUsername = (EditText) findViewById(R.id.editusername);
        EditText inputEmail = (EditText) findViewById(R.id.editemail);
        EditText inputPassword = (EditText) findViewById(R.id.editpassword);

        String username, password, email;

        username = inputUsername.getText().toString();
        password = inputEmail.getText().toString();
        email = inputPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            TextView errormsg = (TextView) findViewById(R.id.text_error_msg);
            errormsg.setText("Some properties are empty!");
            errormsg.setVisibility(View.VISIBLE);

        } else {


            newUser.username = username;
            newUser.email = email;
            newUser.password = password;

            final Context crap = this;

            Thread doData = new Thread(new Runnable() {
                @Override
                public void run() {

                    OkHttpClient client = new OkHttpClient();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.1.68:8080")
                            .client(client)
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();

                    RestInterface mApi = retrofit.create(RestInterface.class);

                    Call<Result> call = mApi.registerUser(newUser);
                    try {
                        Response<Result> rr = call.execute();
                        if (rr.isSuccess()) {
                            String succ = rr.body().content.get("result");
                            if (succ.equals("User created")) {
                                Intent loginIntent = new Intent(crap, LoginActivity.class);
                                crap.startActivity(loginIntent);
                                finish();
                            } else {
                                TextView errormsg = (TextView) findViewById(R.id.text_error_msg);
                                errormsg.setText(rr.body().meta.message.toString());
                                errormsg.setVisibility(View.VISIBLE);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            doData.start();
        }

    }
}
