package com.ru.andr.walkinggame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private WalkerRestAdapter mAdapter;
    private WalkerAPIService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdapter = new WalkerRestAdapter();

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
        User loginUser = new User();
        EditText inputUsername = (EditText) findViewById(R.id.editusername);
        EditText inputPassword = (EditText) findViewById(R.id.editpassword);
        loginUser.username = inputUsername.getText().toString();
        loginUser.password = inputPassword.getText().toString();
        loginUser.email = "";

        Call<Result> mCall = mAdapter.getToken(loginUser.username);

        try{
            boolean crap = mCall.execute().isSuccess();
            Log.d("Classss", ""+crap);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
