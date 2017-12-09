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

public class VoiceControl extends AppCompatActivity {


    Switch myVoiceSwitch;
    Activity myActivity = VoiceControl.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);


        myVoiceSwitch = (Switch) findViewById(R.id.voiceControlSwitch);


        // initialize shared prefs
        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();

        Object myOb = myPrefs.getBoolean("voiceControl", false);
        if (myOb != null) {
            myVoiceSwitch.setChecked((boolean)myOb);

            setColorOnSwitchState((boolean) myOb);
        }

        myVoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                ContentStore.storeData(value, "voiceControl");
                setColorOnSwitchState(value);
            }
        });


    }

    public void setSwitchColor(String color) {
        myVoiceSwitch.getThumbDrawable().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        myVoiceSwitch.getTrackDrawable().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
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
