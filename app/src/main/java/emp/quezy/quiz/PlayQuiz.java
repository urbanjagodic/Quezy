package emp.quezy.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import emp.quezy.R;

// TODO When user click's back go to SelectQuiz activity not GetQuestions
// TODO Display how many questions left and when over display score (?? new activity ??)
// TODO Save score into database upon completion

public class PlayQuiz extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Question> questions;
    ArrayList<String> item;
    ListView listView;
    int qNumber; // Which question are we displaying
    QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        qNumber = 0;

        listView = findViewById(R.id.PlayQuizListView);
        retrieveList();

        item = new ArrayList<>();
        adapter = new QuestionAdapter(getApplicationContext(), item);
        listView.setAdapter(adapter);

        fillItemList(questions.get(qNumber));
        listView.setOnItemClickListener(this);



    }


    /**
     * Fills ArrayList with question parameters.
     * Also notify adapter of the change.
     */
    private void fillItemList(Question question) {
        item.clear();
        item.add(question.getQuestionText());
        item.add(question.getRightAnswer());
        item.add(question.getWrongAnswers()[0]);
        item.add(question.getWrongAnswers()[1]);
        item.add(question.getWrongAnswers()[2]);
        adapter.notifyDataSetChanged();
    }

    private void retrieveList() {
        Bundle bnd = getIntent().getExtras();
        if (bnd != null) {
            questions = bnd.getParcelableArrayList("emp.quezy.questionsList");

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            qNumber++;
            if (qNumber < questions.size()) {
                fillItemList(questions.get(qNumber));
            }
        }
    }
}
