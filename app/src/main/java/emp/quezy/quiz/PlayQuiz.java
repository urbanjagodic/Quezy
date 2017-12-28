package emp.quezy.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
    int qNumber; // Which question are we displaying
    int numCorrect;  // Number of correct answers
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        numCorrect = 0;

        listView = findViewById(R.id.PlayQuizListView);
        numQuestionLeftText = findViewById(R.id.questionsLeftText);
        retrieveList();

        item = new ArrayList<>();
        adapter = new QuestionAdapter(getApplicationContext(), item);
        listView.setAdapter(adapter);

        String updatedString = numQuestionLeftText.getText().toString().replace("NUM", questions.size() - 1 + "");
        numQuestionLeftText.setText(updatedString);

        fillItemList(questions.get(qNumber));
        listView.setOnItemClickListener(this);
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

    private void retrieveList() {
        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            questions = bnd.getParcelableArrayList("emp.quezy.questionsList");
        }
    }

    @Override
    public void onBackPressed() {
        HelperMethods.createDialog(PlayQuiz.this, "Restart the quiz", "Do you wish to start a new quiz?",
                "Yes", "No", new DialogReturnCommand() {
                    @Override
                    public void finishIt() {
                        finish();
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if (parent.getItemAtPosition(position).toString().equals(questions.get(qNumber).getRightAnswer())) {
            changeItemColorAndIncrement(new DialogReturnCommand() {
                @Override
                public void finishIt() {
                    QuestionAdapter.setToGreen(position);
                    numCorrect++;
                }
            });
        }
        else {
            changeItemColorAndIncrement(new DialogReturnCommand() {
                @Override
                public void finishIt() {
                    QuestionAdapter.setToRed(position);
                }
            });
        }

        String numberText = numQuestionLeftText.getText().toString();
        String numberToReplace = retRegexNumber(numberText);
        String updatedString = numberText.replace(numberToReplace, (questions.size() - 1) - qNumber + "");
        numQuestionLeftText.setText(updatedString);
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
                if (qNumber == questions.size() - 1) {
                    HelperMethods.showToast(PlayQuiz.this, "end is here");
                    finishGame();
                }
                else{
                    fillItemList(questions.get(qNumber));
                }
            }
        }, 1300);
    }

    private void finishGame() {
        Bundle bundle = new Bundle();
        bundle.putInt("emp.quezy.correctAnswers", numCorrect);
        bundle.putInt("emp.quezy.numberOfQuestions", questions.size());

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
}
