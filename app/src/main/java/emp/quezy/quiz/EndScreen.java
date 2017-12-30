package emp.quezy.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import emp.quezy.R;
import emp.quezy.helper.DatabaseConnector;
import emp.quezy.info.HighScores;
import emp.quezy.other.MyAnimation;

public class EndScreen extends AppCompatActivity implements View.OnClickListener {

    int numCorrect;  // Number of correct answers
    int numQuestions;  // Number of questions
    int maxPoints;
    int overallScore;
    String category;
    String difficulty;
    TextView mainText;
    TextView scoreText;
    TextView overallPointsText;
    ImageView scoreImage;
    ImageView playAgainBtn;
    ImageView showHighScore;
    ViewGroup wholeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        initialize();
        insertScore();
        setTexts();

        new MyAnimation().fadeIn(wholeLayout, EndScreen.this, R.anim.fade_in);

        playAgainBtn.setOnClickListener(this);
        showHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighScores.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setTexts() {
        double percent = ((double) numCorrect / (double) numQuestions);
        if (percent < 0.0) {
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
        showHighScore = findViewById(R.id.endScreenHighScore);
        scoreImage = findViewById(R.id.scoreImage);
        wholeLayout = findViewById(R.id.endScreenLayout);

        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            numCorrect = bnd.getInt("emp.quezy.correctAnswers");
            numQuestions = bnd.getInt("emp.quezy.numberOfQuestions");
            maxPoints = bnd.getInt("emp.quezy.maxPoints");
            overallScore = bnd.getInt("emp.quezy.overallScore");
            difficulty = bnd.getString("emp.quezy.difficulty");
            category = bnd.getString("emp.quezy.category");
        }
    }

    private void insertScore() {
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String date = sdf.format(new Date());
        databaseConnector.insertScore(new Date().toString().trim().replace("GMT+01:00", ""),
                category, difficulty, overallScore);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SelectQuiz.class);
        startActivity(intent);
        finish();
    }
}
