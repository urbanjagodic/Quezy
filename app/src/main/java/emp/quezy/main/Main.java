package emp.quezy.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;
import emp.quezy.other.MyAnimation;
import emp.quezy.other.ProximitySensorManager;
<<<<<<< HEAD
=======
import emp.quezy.other.VoiceCommands;
>>>>>>> b70ef0919d7e122fe68d3fb5aaae41bff238e6dc
import emp.quezy.quiz.SelectQuiz;
import emp.quezy.settings.Settings;

<<<<<<< HEAD
public class Main extends AppCompatActivity  {
=======
public class Main extends AppCompatActivity {
>>>>>>> b70ef0919d7e122fe68d3fb5aaae41bff238e6dc

    private ImageView[] startButtons;
    private String TAG = getClass().getSimpleName().toLowerCase();
    private Activity myActivity = Main.this;

<<<<<<< HEAD

=======
>>>>>>> b70ef0919d7e122fe68d3fb5aaae41bff238e6dc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButtons = new ImageView[]{
                findViewById(R.id.playButton), findViewById(R.id.settingsButton), findViewById(R.id.infoButton)
        };

        animateButtons();
        buttonAction();

<<<<<<< HEAD
=======

>>>>>>> b70ef0919d7e122fe68d3fb5aaae41bff238e6dc
        ProximitySensorManager myProximityManager = new ProximitySensorManager(Main.this);
        myProximityManager.initialize();
        myProximityManager.register();

    }


    // string voice command result and executed task
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            VoiceCommands.initialize();

            String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            //HelperMethods.showToast(myActivity, result);

            if (VoiceCommands.exitCommands.contains(result)) {
                HelperMethods.killApp(myActivity);
            } else if (VoiceCommands.settingsCommands.contains(result)) {
                startSettings();
            } else if (VoiceCommands.infoCommands.contains(result)) {
                //startInfo():
            } else if (VoiceCommands.playCommands.contains(result)) {
                startSelectQuiz();
            }
        }
    }

    @Override
    public void onBackPressed() {

        HelperMethods.createDialog(myActivity, "Exit the application", "Are you sure?", new DialogReturnCommand() {
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

          }
            );

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HelperMethods.playSound(Main.this, R.raw.click1);

                    switch (view.getId()) {

                        case R.id.playButton:
                            startSelectQuiz();
                            break;
                        case R.id.settingsButton:
                            startSettings();
                            break;
                        case R.id.infoButton:
                            HelperMethods.showToast(Main.this, R.id.infoButton + "");
                            break;
                    }
                }
            });
        }
    }

    private void startSelectQuiz() {
        final Intent selectQuiz = new Intent(myActivity, SelectQuiz.class);
        startActivity(selectQuiz);
    }

    private void startSettings() {
        Intent startSettings = new Intent(myActivity, Settings.class);
        startActivity(startSettings);
    }


    public void animateButtons() {
        for (ImageView button : startButtons) {
            new MyAnimation().fadeIn(button, getApplicationContext(), R.anim.fade_in);
        }
    }


}
