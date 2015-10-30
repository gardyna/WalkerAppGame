package com.ru.andr.walkinggame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ru.andr.walkinggame.api.RestInterface;
import com.ru.andr.walkinggame.model.Result;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.AllUsersEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveUserInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.MatchedRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.RoomRequestListener;
import com.shephertz.app42.gaming.multiplayer.client.listener.ZoneRequestListener;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class WarpTest extends AppCompatActivity implements ConnectionRequestListener, RoomRequestListener, ZoneRequestListener {
    private WarpClient theClient;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();
    private Spinner friendsList;

    private void populateFriendsList(){
        List<String> list = new ArrayList<>();
        list.add("Dagur");
        list.add("Dabs");
        list.add("FOO");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        friendsList.setAdapter(dataAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warp_test);
        init();

    }

    @Override
    protected void onStart(){
        super.onStart();
        theClient.addConnectionRequestListener(this);
        theClient.addRoomRequestListener(this);
        theClient.addZoneRequestListener(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        theClient.removeConnectionRequestListener(this);
        theClient.removeRoomRequestListener(this);
        theClient.removeZoneRequestListener(this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(theClient!=null){
            theClient.disconnect();
        }
    }

    public void onConnectClicked(View view){
        String userName = Player.getPlayer(this).getName();
        if(userName.length()>0){
            Utils.USER_NAME  = userName;
            progressDialog = ProgressDialog.show(this, "", "Please wait...");
            progressDialog.setCancelable(true);
            theClient.connectWithUserName(userName);
        }else{
            Utils.showToast(this, "Please enter name");
        }
    }

    private void init() {
        friendsList = (Spinner)findViewById(R.id.friends_list);
        populateFriendsList();
        WarpClient.initialize(Constants.publicAPIkey, Constants.secretAPIkey);
        WarpClient.setRecoveryAllowance(120);
        try{
            theClient = WarpClient.getInstance();
        }catch (Exception ex){
            Toast.makeText(this, "Exception in initialize", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectDone(final ConnectEvent event) {
        if(progressDialog!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();

                }
            });
        }
        if(event.getResult() == WarpResponseResultCode.SUCCESS){
            showToastOnUIThread("Connection success");
            theClient.createRoom("myRoom2", Player.getPlayer(this).getName(), 2, null);
            //theClient.joinRoom("myRoom2");
        }
        else if(event.getResult() == WarpResponseResultCode.SUCCESS_RECOVERED){
            showToastOnUIThread("Connection recovered");
        }
        else if(event.getResult() == WarpResponseResultCode.CONNECTION_ERROR_RECOVERABLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(WarpTest.this, "", "Recoverable connection error. Recovering session after 5 seconds");
                }
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setMessage("Recovering...");
                    theClient.RecoverConnection();
                }
            }, 5000);
        }
        else{
            showToastOnUIThread("Non-recoverable connection error.");
        }
    }



    @Override
    public void onJoinRoomDone(RoomEvent event) {
        if(event.getResult()==WarpResponseResultCode.SUCCESS){
            Utils.roomID = event.getData().getId();
            theClient.subscribeRoom(event.getData().getId());

        }else{
            showToastOnUIThread("onJoinRoomDone with ErrorCode: "+event.getResult());
        }
    }





    @Override
    public void onSubscribeRoomDone(RoomEvent event) {
        if(event.getResult()==WarpResponseResultCode.SUCCESS){
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        }else{
            showToastOnUIThread("onSubscribeRoomDone Failed with ErrorCode: "+event.getResult());
        }

    }



    private void showToastOnUIThread(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WarpTest.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_warp_test, menu);
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
    // region not implemented
    @Override
    public void onUnSubscribeRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onLockPropertiesDone(byte b) {

    }

    @Override
    public void onUnlockPropertiesDone(byte b) {

    }

    @Override
    public void onLeaveRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onSetCustomRoomDataDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onUpdatePropertyDone(LiveRoomInfoEvent liveRoomInfoEvent) {

    }

    @Override
    public void onDisconnectDone(ConnectEvent connectEvent) {

    }

    @Override
    public void onInitUDPDone(byte b) {

    }

    @Override
    public void onDeleteRoomDone(RoomEvent roomEvent) {

    }

    @Override
    public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {

    }

    @Override
    public void onCreateRoomDone(RoomEvent roomEvent) {
        //Utils.showToast(this, "Room created, attempting to join");
        theClient.joinRoom(roomEvent.getData().getId());
        //theClient.subscribeRoom(roomEvent.getData().getId());
    }

    @Override
    public void onGetOnlineUsersDone(AllUsersEvent allUsersEvent) {

    }

    @Override
    public void onGetLiveUserInfoDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onSetCustomUserDataDone(LiveUserInfoEvent liveUserInfoEvent) {

    }

    @Override
    public void onGetMatchedRoomsDone(MatchedRoomsEvent matchedRoomsEvent) {

    }

    // endregion
}
