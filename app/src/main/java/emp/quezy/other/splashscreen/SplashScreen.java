package emp.quezy.other.splashscreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import emp.quezy.R;
import emp.quezy.main.Main;

public class SplashScreen extends AppCompatActivity {

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final ImageView[] circles = new ImageView[]{
                findViewById(R.id.circle),
                findViewById(R.id.circle1),
                findViewById(R.id.circle2),
                findViewById(R.id.circle3),
        };


        new CountDownTimer(6000, 400) {

            public void onTick(long millisUntilFinished) {

                if (index >= circles.length) {
                    index = 0;
                }
                Log.d("tick", millisUntilFinished + "");
                for (int i = 0; i < circles.length ; i++) {
                    circles[i].setColorFilter(Color.WHITE);
                }
                circles[index++].setColorFilter(R.color.splash_screen_circle);
            }
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, Main.class));
                finish();
            }

        }.start();

    }
}
