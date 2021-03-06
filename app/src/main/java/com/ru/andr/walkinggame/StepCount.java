package com.ru.andr.walkinggame;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StepCount extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private TextView levelText;
    private TextView nextLevelText;
    private Button mLevelUpButton;
    private ProgressBar levelProgress;
    private BarChart mStatChart;

    private SensorManager sensorManager;

    private Sensor mStepCountSensor;

    private Sensor mStepDetectorSensor;

    private int mStepsTaken;

    private Player player;

    boolean firstStart = true;

    private void setmStatChart(){
        mStatChart.setAutoScaleMinMaxEnabled(true);
        mStatChart.setTouchEnabled(false);
        mStatChart.setDrawGridBackground(false);
        mStatChart.setDescription("");
        mStatChart.getLegend().setFormSize(0);
        mStatChart.getAxisLeft().setDrawLabels(false);
        mStatChart.getAxisRight().setDrawLabels(false);

        // test for chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(player.getStrength(), 0));
        entries.add(new BarEntry(player.getSpeed(), 1));
        entries.add(new BarEntry(player.getAgility(), 2));
        entries.add(new BarEntry(player.getInteligence(), 3));

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Strength");
        labels.add("Speed");
        labels.add("Agility");
        labels.add("Intelligence");

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(labels, dataset);
        data.setValueTextSize(14);
        mStatChart.setData(data);
        mStatChart.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        System.out.println("starting activity");

        player = Player.getPlayer(this);
        textView = (TextView)findViewById(R.id.textview);
        levelText = (TextView)findViewById(R.id.leveltext);
        nextLevelText = (TextView)findViewById(R.id.tonextlevel);
        levelProgress = (ProgressBar)findViewById(R.id.progressBar);
        mLevelUpButton = (Button)findViewById(R.id.button);
        mStatChart = (BarChart)findViewById(R.id.chart);
        setmStatChart();
        mLevelUpButton.setVisibility(View.INVISIBLE);
        mLevelUpButton.setClickable(false);

        // uncomment to clear info
        //getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit().clear().commit();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mStepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        /* comment or uncomment for testing leveling methods
        if(player.getCanLevelUpTimes() <= 0){
            player.addEXP(99999999);
        }
        */
        levelText.setText("LVL: " + player.getLevel());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_step_count, menu);
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

    @Override
    protected void onResume(){
        super.onResume();
        player = Player.getPlayer(this);
        sensorManager.registerListener(this, mStepCountSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mStepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void StartLevelUp(View v){
        player.Save();
        Intent i = new Intent(this, Levelup.class);
        startActivity(i);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //sensorManager.unregisterListener(this, mStepDetectorSensor);
        //sensorManager.unregisterListener(this, mStepCountSensor);
    }

    @Override
    public void onPause(){
        super.onPause();
        player.Save();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0){
            value = (int)values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            textView.setText("EXP:  : " + player.getEXP());
            if (firstStart){
                mStepsTaken = value;
            }
            player.addEXP(value - mStepsTaken);
            mStepsTaken = value;
            levelText.setText("LVL: " + player.getLevel());
            nextLevelText.setText("EXP to LVL: " + player.getExpToNextLevel());
            levelProgress.setMax(player.getExpToNextLevel());
            levelProgress.setProgress(player.getEXP());
            setmStatChart();
            if(player.getCanLevelUpTimes() != 0){
                mLevelUpButton.setVisibility(View.VISIBLE);
                mLevelUpButton.setClickable(true);
            }
        }/*else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            textView.setText("Step detector detected : " + value);
        }*/
        firstStart = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void PlayGame(View v){
        Intent i = new Intent(this, WarpTest.class);
        startActivity(i);
    }
}
