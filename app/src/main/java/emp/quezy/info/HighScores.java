package emp.quezy.info;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import emp.quezy.R;
import emp.quezy.helper.DatabaseConnector;
import emp.quezy.helper.HelperMethods;

public class HighScores extends AppCompatActivity {

    ListView listView;
    CursorAdapter scoreAdapter;
    private static final String DESC = "DESC";
    private static final String ASC = "ASC";
    TextView scoreText;
    TextView categoryText;
    TextView difficultyText;
    private boolean flag = false;
    DatabaseConnector databaseConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        String[] from = new String[]{DatabaseConnector.C_Category, DatabaseConnector.C_Difficulty, DatabaseConnector.C_Score};
        int[] to = new int[]{R.id.textCategory, R.id.textDifficulty, R.id.textScore};

        listView = findViewById(R.id.high_score_listView);
        scoreText = findViewById(R.id.scoreText);
        categoryText = findViewById(R.id.categoryText);
        difficultyText = findViewById(R.id.difficultyText);
        databaseConnector = new DatabaseConnector(getApplicationContext());
        databaseConnector.open();

        scoreAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.high_score_list,
                null, from, to, 0);

        scoreAdapter.changeCursor(databaseConnector.getTopGames(15, DESC, DatabaseConnector.C_Score));
        scoreText.setText(getResources().getString(R.string.score) + " ⬇");
        categoryText.setText((getResources().getString(R.string.category) +  " ⬇"));
        difficultyText.setText((getResources().getString(R.string.difficulty) +  " ⬇"));

        listView.setAdapter(scoreAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Cursor currentCursor = scoreAdapter.getCursor();
                if(currentCursor.moveToPosition(i)) {
                    HelperMethods.showToast(HighScores.this,
                            "Played on: " + currentCursor.getString(currentCursor.getColumnIndex("Date")));
                }
            }
        });

        setClickListenerAndSort(scoreText, R.string.score, DatabaseConnector.C_Score);
        setClickListenerAndSort(categoryText, R.string.category, DatabaseConnector.C_Category);
        setClickListenerAndSort(difficultyText, R.string.difficulty, DatabaseConnector.C_Difficulty);
    }



    public void setClickListenerAndSort(final TextView v, final int resource, final String passedString) {

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String direction = flag ? DESC : ASC;
                flag = !flag;
                scoreAdapter.changeCursor(databaseConnector.getTopGames(15, direction, passedString));
                v.setText((getResources().getString(resource) +  (flag ? " ⬆" : " ⬇")));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseConnector.close();
    }
}
