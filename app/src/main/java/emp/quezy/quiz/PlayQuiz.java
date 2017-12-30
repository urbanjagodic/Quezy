package emp.quezy.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;
import emp.quezy.other.MyAnimation;

// TODO When over display score (?? new activity ??)
// TODO Save score into database upon completion

/**
 * TODO bom kr pisu p slovenk kwa morva še nardit , zdej to dela za praviln pa napačn odogovr,
 * TODO magar bom jst še naredu jutr da ti sešteva točke gled na zahtevnost pa ramzmislam
 * TODO ce bi se naredu en timer za da ma sam 15 sekund na voljo da odgoovri,
 * TODO ti pa lohk SQL database se lotš če maš cajt
 *
 * TODO evo to je blo po slovensk ostalo commenti pa use je nazaj u anglesčini :D
 */

public class PlayQuiz extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Question> questions;
    ArrayList<String> item;
    ListView listView;
    TextView numQuestionLeftText;
    TextView currentScoreText;
    int qNumber;                    // Which question are we displaying
    int numCorrect;                 // Number of correct answers
    int overAllPoints;              // all points that user achieves
    QuestionAdapter adapter;
    String category;
    String difficulty;
    LinearLayout coreLayout;
    int difficultyMultiplier;       // easy = 2, medium = 3, hard = 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        numCorrect = 0;

        listView = findViewById(R.id.PlayQuizListView);
        numQuestionLeftText = findViewById(R.id.questionsLeftText);
        currentScoreText = findViewById(R.id.currentScoreText);
        coreLayout = findViewById(R.id.coreLayout);
        retrieveListAndSetDifficulty();

        item = new ArrayList<>();
        adapter = new QuestionAdapter(getApplicationContext(), item);
        listView.setAdapter(adapter);

        String updatedString = numQuestionLeftText.getText().toString().replace("NUM", questions.size() - 1 + "");
        numQuestionLeftText.setText(updatedString);
        currentScoreText.setText(getResources().getString(R.string.currentscoretext) + " " + overAllPoints);

        fillItemList(questions.get(qNumber));
        listView.setOnItemClickListener(this);

       slideAnim();

    }

    /**
     * Fills ArrayList with question parameters.
     * Also notify adapter of the change.
     */
    private void fillItemList(Question question) {
        item.clear();
        ((TextView)findViewById(R.id.questionText)).setText(question.getQuestionText());

        String[] shuffledAnswers = randomizeAnswers(question);
        item.addAll(Arrays.asList(shuffledAnswers));
        adapter.notifyDataSetChanged();
    }

    private void retrieveListAndSetDifficulty() {
        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            questions = bnd.getParcelableArrayList("emp.quezy.questionsList");
            category = bnd.getString("emp.quezy.category");
            difficulty = bnd.getString("emp.quezy.difficulty");
            switch (difficulty) {
                case "easy":
                    difficultyMultiplier = 10;
                    break;
                case "medium":
                    difficultyMultiplier = 35;
                    break;
                case "hard":
                    difficultyMultiplier = 60;
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        HelperMethods.createDialog(PlayQuiz.this, "Restart the quiz", "Do you wish to start a new quiz?",
                "Yes", "No", new DialogReturnCommand() {
                    @Override
                    public void finishIt() {
                        Intent startSelectAgain = new Intent(PlayQuiz.this, SelectQuiz.class);
                        startActivity(startSelectAgain);
                        finish();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        final String rightAnswer = questions.get(qNumber).getRightAnswer();

        if (parent.getItemAtPosition(position).toString().equals(rightAnswer)) {
            changeItemColorAndIncrement(new DialogReturnCommand() {
                @Override
                public void finishIt() {
                    QuestionAdapter.setToGreen(position);
                    numCorrect++;
                    overAllPoints += difficultyMultiplier;
                    if (HelperMethods.turnOnSound(PlayQuiz.this)) {
                        HelperMethods.playSound(PlayQuiz.this, R.raw.correct_answer_sound);
                    }
                }
            });
        }
        else {
            changeItemColorAndIncrement(new DialogReturnCommand() {
                @Override
                public void finishIt() {
                    int rightItemPosition = -1;
                    for (int i = 0; i < item.size(); i++) {
                        if (item.get(i).equals(rightAnswer)) {
                            rightItemPosition = i;
                        }
                    }
                    QuestionAdapter.setToRedAndGreen(position, rightItemPosition);
                    if (HelperMethods.turnOnSound(PlayQuiz.this)) {
                    }
                    HelperMethods.playSound(PlayQuiz.this, R.raw.wrong_answer_sound);
                }
            });
        }
        String numberText = numQuestionLeftText.getText().toString();
        String numberToReplace = retRegexNumber(numberText);
        int numberQuestLeft = (questions.size() -1) - qNumber;

        String updatedString = numberText.replace(numberToReplace, numberQuestLeft + "");

        if (numberQuestLeft <= Math.round( (2.0 * questions.size())/10)) {
            numQuestionLeftText.setTextColor(getResources().getColor(R.color.listitemfalse));
        }
        numQuestionLeftText.setText(questions.size() == qNumber ? "" : updatedString);
        currentScoreText.setText(getResources().getString(R.string.currentscoretext) + " " + overAllPoints);
    }


    private void changeItemColorAndIncrement(DialogReturnCommand myCustomCommand) {

        // we pass in the Question.setToRed or setToGreen method to execute it on
        myCustomCommand.finishIt();
        listView.setAdapter(adapter);
        ++qNumber;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                QuestionAdapter.setToDefault();
                listView.setAdapter(adapter);
                if (qNumber == questions.size()) {
                    finishGame();
                }
                else{
                    fillItemList(questions.get(qNumber));
                    slideAnim();
                }
            }
        }, 1800);
    }

    private void finishGame() {
        Bundle bundle = new Bundle();
        bundle.putInt("emp.quezy.correctAnswers", numCorrect);
        bundle.putInt("emp.quezy.numberOfQuestions", questions.size());
        bundle.putInt("emp.quezy.maxPoints", questions.size() * difficultyMultiplier);
        bundle.putInt("emp.quezy.overallScore", overAllPoints);
        bundle.putString("emp.quezy.difficulty", difficulty);
        bundle.putString("emp.quezy.category", category);

        Intent intent = new Intent(getApplicationContext(), EndScreen.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    private String[] randomizeAnswers(Question myQuestion) {
        String[] answers = { myQuestion.getRightAnswer(), myQuestion.getWrongAnswers()[0],
                myQuestion.getWrongAnswers()[1],
                myQuestion.getWrongAnswers()[2]};

        ArrayList<String> answersArray = new ArrayList<>(Arrays.asList(answers));
        Collections.shuffle(answersArray);
        return answersArray.toArray(new String[answers.length]);
    }

    private String retRegexNumber(String text) {
        Pattern myPattern = Pattern.compile("[\\d]+");
        Matcher myMatcher = myPattern.matcher(text);
        return myMatcher.find() ? myMatcher.group() : "";
    }

    private void slideAnim() {
        new MyAnimation().slideFromRight(coreLayout, PlayQuiz.this, R.anim.slide_from_right);
    }
}
