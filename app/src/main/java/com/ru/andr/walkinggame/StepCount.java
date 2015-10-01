package com.ru.andr.walkinggame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class StepCount extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private TextView levelText;
    private TextView nextLevelText;

    private ProgressBar levelProgress;
    private BarChart mStatChart;

    private SensorManager mSenssorManager;

    private Sensor mStepCountSensor;

    private Sensor mStepDetectorSensor;

    private int mStepsTaken;
    private int level;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        textView = (TextView)findViewById(R.id.textview);
        levelText = (TextView)findViewById(R.id.leveltext);
        nextLevelText = (TextView)findViewById(R.id.tonextlevel);
        levelProgress = (ProgressBar)findViewById(R.id.progressBar);
        mStatChart = (BarChart)findViewById(R.id.chart);
        mStatChart.setAutoScaleMinMaxEnabled(false);
        mStatChart.setTouchEnabled(false);
        mStatChart.setDrawGridBackground(false);
        mStatChart.setDescription("");
        mStatChart.getLegend().setFormSize(0);
        mStatChart.getAxisLeft().setDrawLabels(false);
        mStatChart.getAxisRight().setDrawLabels(false);

        // test for chart
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(1f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(3f, 3));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("STR");
        labels.add("PER");
        labels.add("END");
        labels.add("CHA");

        BarDataSet dataset = new BarDataSet(entries, "");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(labels, dataset);
        data.setValueTextSize(14);
        mStatChart.setData(data);


        mSenssorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = mSenssorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mStepCountSensor = mSenssorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        level = 1;
        player = new Player();

        levelText.setText("LVL: " + level);
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

        mSenssorManager.registerListener(this, mStepCountSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSenssorManager.registerListener(this, mStepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mSenssorManager.unregisterListener(this, mStepDetectorSensor);
        mSenssorManager.unregisterListener(this, mStepCountSensor);
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
            player.addEXP(value - mStepsTaken);
            mStepsTaken = value;
            levelText.setText("LVL: " + player.getLevel());
            nextLevelText.setText("EXP to LVL: " + player.getExpToNextLevel());
            levelProgress.setMax(player.getExpToNextLevel());
            levelProgress.setProgress(player.getEXP());

        }/*else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            textView.setText("Step detector detected : " + value);
        }*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
