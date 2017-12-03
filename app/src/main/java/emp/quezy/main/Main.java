package emp.quezy.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import emp.quezy.R;
import emp.quezy.helper.HelperMethods;
import emp.quezy.other.MyAnimation;
import emp.quezy.other.ProximitySensorManager;

public class Main extends AppCompatActivity  {


    private ImageView[] startButtons;
    private String TAG = getClass().getSimpleName().toLowerCase();

    //private SensorManager  mySensorManger;
    //private Sensor myProximity;
    //private static final int sensitivity = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButtons = new ImageView[]{
                findViewById(R.id.playButton), findViewById(R.id.settingsButton), findViewById(R.id.infoButton)
        };

        animateButtons();
        buttonAction();

        ProximitySensorManager myProximityManager = new ProximitySensorManager(Main.this);

        myProximityManager.initialize();
        myProximityManager.register();


    }



    public void buttonAction() {
        for (ImageView button : startButtons) {

            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        switch (view.getId()) {
                            case R.id.playButton:
                                ((ImageView) view).setImageResource(R.drawable.play_image_pressed);
                                break;
                            case R.id.settingsButton:
                                ((ImageView) view).setImageResource(R.drawable.settings_image_pressed);
                                break;
                            case R.id.infoButton:
                                ((ImageView) view).setImageResource(R.drawable.info_image_pressed);
                                break;
                        }
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        switch (view.getId()) {
                            case R.id.playButton:
                                ((ImageView) view).setImageResource(R.drawable.play_image);
                                break;
                            case R.id.settingsButton:
                                ((ImageView) view).setImageResource(R.drawable.settings_image);
                                break;
                            case R.id.infoButton:
                                ((ImageView) view).setImageResource(R.drawable.info_image);
                                break;
                        }
                    }
                    return  false;
                }

            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (view.getId()) {

                        case R.id.playButton:
                            HelperMethods.showToast(Main.this, R.id.playButton + "");
                            break;
                        case R.id.settingsButton:
                            HelperMethods.showToast(Main.this, R.id.settingsButton + "");
                            break;
                        case R.id.infoButton:
                            HelperMethods.showToast(Main.this, R.id.infoButton + "");
                            break;
                    }
                }
            });
        }
    }

    public void animateButtons() {
        for (ImageView button : startButtons) {

            new MyAnimation().fadeIn(button, getApplicationContext(), R.anim.fade_in);
        }

    }


    /*
    @Override
    protected void onPause() {
        super.onPause();
        mySensorManger.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySensorManger.registerListener(this, myProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] >= -sensitivity && sensorEvent.values[0] <= sensitivity) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                //far
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    */

}
