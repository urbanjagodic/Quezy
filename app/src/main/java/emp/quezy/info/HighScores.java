package emp.quezy.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import emp.quezy.R;
import emp.quezy.helper.DatabaseConnector;

public class HighScores extends AppCompatActivity {

    ListView listView;
    CursorAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        String[] from = new String[]{DatabaseConnector.C_Category, DatabaseConnector.C_Difficulty, DatabaseConnector.C_Score};
        int[] to = new int[]{R.id.textCategory, R.id.textDifficulty, R.id.textScore};

        listView = findViewById(R.id.high_score_listView);
        DatabaseConnector databaseConnector = new DatabaseConnector(getApplicationContext());
        databaseConnector.open();
        scoreAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.high_score_list,
                null, from, to, 0);

        scoreAdapter.changeCursor(databaseConnector.getTopTen());
        listView.setAdapter(scoreAdapter);
        databaseConnector.close();
    }
}
