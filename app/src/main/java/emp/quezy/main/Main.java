package emp.quezy.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;
import emp.quezy.info.Info;
import emp.quezy.other.ContentStore;
import emp.quezy.other.MyAnimation;
import emp.quezy.other.ProximitySensorManager;
import emp.quezy.other.VoiceCommands;
import emp.quezy.quiz.SelectQuiz;
import emp.quezy.settings.Settings;


public class Main extends AppCompatActivity {

    private ImageView[] startButtons;
    private int[] animations;
    private TextView quezyTitle;
    private String TAG = getClass().getSimpleName().toLowerCase();
    private Activity myActivity = Main.this;
    private ProximitySensorManager myProximityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButtons = new ImageView[]{
                findViewById(R.id.playButton), findViewById(R.id.settingsButton), findViewById(R.id.infoButton)
        };
        animations = new int[]{
                R.anim.bounce, R.anim.rotate, R.anim.squeze, R.anim.title_slide_from_left, R.anim.title_slide_from_right};

        quezyTitle = findViewById(R.id.quezyTitle);
        animateButtons();
        animateTitle();
        buttonAction();

        // initialize myproximity manager
        myProximityManager = new ProximitySensorManager(myActivity);
        myProximityManager.initialize();
    }

    @Override
    public void onResume() {
        super.onResume();

        // enable or disable myproximity manager regarding boolean value from shared prefs
        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();
        Object enableVoiceControl = myPrefs.getBoolean("voiceControl", false);

        if (enableVoiceControl != null) {
            if ((boolean) enableVoiceControl) {
                myProximityManager.register();
            } else {
                myProximityManager.unregister();
            }
        }
        animateTitle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myProximityManager.unregister();
    }

    // string voice command result and executed task
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            VoiceCommands.initialize();
            String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            //HelperMethods.showToast(myActivity, result);

            storeVoiceCommandToSharedPrefs(result);

            if (VoiceCommands.exitCommands.contains(result)) {
                HelperMethods.killApp(myActivity);
            } else if (VoiceCommands.settingsCommands.contains(result)) {
                startSettings();
            } else if (VoiceCommands.infoCommands.contains(result)) {
                startInfo();
            } else if (VoiceCommands.playCommands.contains(result)) {
                startSelectQuiz();
            }
        }
    }

    @Override
    public void onBackPressed() {
        HelperMethods.createDialog(myActivity, "Exit the application", "Are you sure?", "Yes", "No",
                new DialogReturnCommand() {
                    @Override
                    public void finishIt() {
                        HelperMethods.killApp(myActivity);
                    }
                });
    }

    public void buttonAction() {
        for (ImageView button : startButtons) {
            button.setSoundEffectsEnabled(false);

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
                    return false;
                }

            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (HelperMethods.turnOnSound(myActivity)) {
                        HelperMethods.playSound(Main.this, R.raw.click1);
                    }
                    switch (view.getId()) {
                        case R.id.playButton:
                            startSelectQuiz();
                            break;
                        case R.id.settingsButton:
                            startSettings();
                            break;
                        case R.id.infoButton:
                            startInfo();
                            break;
                    }
                }
            });
        }
    }

    private void startSelectQuiz() {

        if (HelperMethods.checkInternetConnection(myActivity)) {
            final Intent selectQuiz = new Intent(myActivity, SelectQuiz.class);
            startActivity(selectQuiz);
        } else {
            HelperMethods.createDialog(myActivity, "", "For further use of this application," +
                    " please connect to the internet!", "OK", "", new DialogReturnCommand() {
                @Override
                public void finishIt() {
                    // nothing
                }
            });
        }
    }

    private void startSettings() {
        Intent startSettings = new Intent(myActivity, Settings.class);
        startActivity(startSettings);
    }

    private void startInfo() {
        Intent startInfo = new Intent(myActivity, Info.class);
        startActivity(startInfo);
    }

    public void animateButtons() {
        for (ImageView button : startButtons) {
            new MyAnimation().randomAnim(button, myActivity, R.anim.fade_in);
        }
    }

    public void animateTitle() {
        new MyAnimation().randomAnim(quezyTitle, myActivity, animations[HelperMethods.randomPosition(5)]);
    }

    public void storeVoiceCommandToSharedPrefs(String voiceCommand) {
        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();

        // default value is "" if the string does not exist
        Object storedCommands = myPrefs.getString("voiceCommands", "");

        storedCommands += ";" + new Date().toString().trim() + "|" + voiceCommand;
        ContentStore.storeData(storedCommands, "voiceCommands");
    }

}
