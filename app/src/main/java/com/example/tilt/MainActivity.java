package com.example.tilt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager snsManager;
    private Sensor aSensor;
    private boolean lightOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean aSensorDisp = false;
        lightOn = false;

        snsManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = snsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if (aSensor != null){
            aSensorDisp = snsManager.registerListener((SensorEventListener) this, aSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(getApplicationContext(),"Pas d'accelerometre detecte, fonctionalite desactivee", Toast.LENGTH_LONG).show();
        }

        if(!aSensorDisp){
            snsManager.unregisterListener((SensorEventListener) this, snsManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            Toast.makeText(getApplicationContext(),"Accelerometre indisponible, fonctionalite desactivee", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        snsManager.registerListener((SensorEventListener) this, aSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause(){
        super.onPause();
        snsManager.unregisterListener((SensorEventListener) this, aSensor);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER :
                onAccelerometerChange(event);
                break;
            default:
                break;
        }
    }

    private void onAccelerometerChange(SensorEvent event) {
        if(event.values[0] + event.values[1] + event.values[2] <= 9.60 || event.values[0] + event.values[1] + event.values[2] >= 10){

            /*Camera cam = Camera.open();
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);*/

            TextView affichage = (TextView) findViewById(R.id.textFlash);

            if(!lightOn){
                /*cam.setParameters(p);
                cam.startPreview();*/
                affichage.setText("Flash eteint");
                lightOn = true;
            }else {
                /*cam.stopPreview();*/
                affichage.setText("Flash allume");
                lightOn = false;
            }
            /*cam.release();*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor", sensor.getType()+":"+accuracy);
    }
}