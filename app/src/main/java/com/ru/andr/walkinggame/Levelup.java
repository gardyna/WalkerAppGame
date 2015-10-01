package com.ru.andr.walkinggame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Levelup extends AppCompatActivity {
    TextView canLevelText;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelup);
        player = Player.getPlayer(this);
        canLevelText = (TextView)findViewById(R.id.LevelTimes);

        canLevelText.setText("You can level " + player.getCanLevelUpTimes() + " times");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_levelup, menu);
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

    public void IncStrength(View v){
        player.incSTR();
        player.Save();
        canLevelText.setText("You can level " + player.getCanLevelUpTimes() + " times");
        checkGoBack();
    }

    private void checkGoBack(){
        if (player.getCanLevelUpTimes() <= 0){
            Intent i = new Intent(this, StepCount.class);
            startActivity(i);
        }
    }
}
