package emp.quezy.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import emp.quezy.R;

public class EndScreen extends AppCompatActivity implements View.OnClickListener {

    int numCorrect;  // Number of correct answers
    int numQuestions;  // Number of questions
    int maxPoints;
    int overallScore;
    TextView mainText;
    TextView scoreText;
    TextView overallPointsText;
    ImageView scoreImage;
    Button playAgainBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        initialize();
        setTexts();

        playAgainBtn.setOnClickListener(this);
    }

    private void setTexts() {

        double percent = ((double) numCorrect / (double) numQuestions);
        if (percent < 0.0){
            Log.i("percent_ERR", Double.toString(percent));
        } else if (percent < 0.4) {
            mainText.setText(R.string.bad_score);
            mainText.setTextColor(getResources().getColor(R.color.listitemfalse));
            scoreImage.setImageResource(R.drawable.dissapointed);
        } else if (percent < 0.6) {
            mainText.setText(R.string.ok_score);
            mainText.setTextColor(getResources().getColor(R.color.goodtext));
            scoreImage.setImageResource(R.drawable.notbad);
        } else if (percent <= 1.0) {
            mainText.setText(R.string.congratulations);
            mainText.setTextColor(getResources().getColor(R.color.greattext));
            scoreImage.setImageResource(R.drawable.congratulations);
        } else {
            Log.i("percent_ERR", Double.toString(percent));
        }
        String rightAnswers = "Right answers: " + numCorrect + " / " + numQuestions + "";
        String overallPointsString = getString(R.string.overallpointstext) + " " + overallScore + " / " + maxPoints;
        scoreText.setText(rightAnswers);
        overallPointsText.setText(overallPointsString);
    }

    private void initialize() {
        mainText = findViewById(R.id.mainText);
        scoreText = findViewById(R.id.scoreText);
        overallPointsText = findViewById(R.id.overallPointsText);
        playAgainBtn = findViewById(R.id.endScreenPlayAgain);
        scoreImage = findViewById(R.id.scoreImage);

        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            numCorrect = bnd.getInt("emp.quezy.correctAnswers");
            numQuestions = bnd.getInt("emp.quezy.numberOfQuestions");
            maxPoints = bnd.getInt("emp.quezy.maxPoints");
            overallScore = bnd.getInt("emp.quezy.overallScore");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SelectQuiz.class);
        startActivity(intent);
        finish();
    }

}
