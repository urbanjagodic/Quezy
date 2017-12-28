package emp.quezy.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;

public class EndScreen extends AppCompatActivity implements View.OnClickListener {

    int numCorrect;  // Number of correct answers
    int numQuestions;  // Number of questions
    TextView mainText;
    TextView scoreText;
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
        } else if (percent < 0.6) {
            mainText.setText(R.string.ok_score);
        } else if (percent <= 1.0) {
            mainText.setText(R.string.congratulations);
        } else {
            Log.i("percent_ERR", Double.toString(percent));
        }
        String score = "Score: " + numCorrect + " / " + numQuestions + "";
        scoreText.setText(score);
    }

    private void initialize() {
        mainText = findViewById(R.id.mainText);
        scoreText = findViewById(R.id.scoreText);
        playAgainBtn = findViewById(R.id.endScreenPlayAgain);

        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            numCorrect = bnd.getInt("emp.quezy.correctAnswers");
            numQuestions = bnd.getInt("emp.quezy.numberOfQuestions");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SelectQuiz.class);
        startActivity(intent);
    }

}
