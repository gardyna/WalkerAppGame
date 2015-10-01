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
import android.widget.TextView;

public class StepCount extends AppCompatActivity implements SensorEventListener {

    private TextView textView;

    private SensorManager mSenssorManager;

    private Sensor mStepCountSensor;

    private Sensor mStepDetectorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        textView = (TextView)findViewById(R.id.textview);

        mSenssorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = mSenssorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mStepCountSensor = mSenssorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
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
            textView.setText("Step counter detected : " + value);
        }else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            textView.setText("Step detector detected : " + value);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
