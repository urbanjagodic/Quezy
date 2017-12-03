package emp.quezy.other;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import emp.quezy.helper.HelperMethods;

/**
 * Created by Urban on 3. 12. 2017.
 */

public class ProximitySensorManager  implements SensorEventListener {

    private SensorManager  mySensorManger;
    private Sensor myProximity;
    private static final int sensitivity = 5;

    private Activity myActivity;

    public ProximitySensorManager(Activity myActivity) {
        this.myActivity = myActivity;
    }


    public void initialize() {

        mySensorManger = (SensorManager) this.myActivity.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        myProximity = mySensorManger.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    protected void unregister() {
        mySensorManger.unregisterListener(this);
    }


    public void register() {
        mySensorManger.registerListener(this, myProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] >= -sensitivity && sensorEvent.values[0] <= sensitivity) {
                //near
                HelperMethods.showToast(this.myActivity, "near device");
            } else {
                //far
                HelperMethods.showToast(this.myActivity, "far from device");
            }


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }
}