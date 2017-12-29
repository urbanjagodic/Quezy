package emp.quezy.info;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import emp.quezy.R;
import emp.quezy.helper.DatabaseConnector;
import emp.quezy.helper.HelperMethods;

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

        scoreAdapter.changeCursor(databaseConnector.getTopGames(15));


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

        databaseConnector.close();
    }
}
