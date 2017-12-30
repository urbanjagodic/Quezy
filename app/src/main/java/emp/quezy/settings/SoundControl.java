package emp.quezy.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import emp.quezy.R;
import emp.quezy.other.ContentStore;

public class SoundControl extends AppCompatActivity {


    Switch mySoundSwitch;
    Activity myActivity = SoundControl.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_control);

        mySoundSwitch = findViewById(R.id.soundControlSwitch);

        // initialize shared prefs
        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();

        Object myOb = myPrefs.getBoolean("soundControl", false);
        if (myOb != null) {
            mySoundSwitch.setChecked((boolean)myOb);
            setColorOnSwitchState((boolean) myOb);
        }

        mySoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                ContentStore.storeData(value, "soundControl");
                setColorOnSwitchState(value);
            }
        });
    }

    public void setSwitchColor(String color) {
        mySoundSwitch.getThumbDrawable().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        mySoundSwitch.getTrackDrawable().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
    }

    public void setColorOnSwitchState(boolean value) {
        if (value) {
            setSwitchColor("#ff9933");
        }
        else {
            setSwitchColor("#ffffff");
        }
    }
}
