package emp.quezy.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;

public class Info extends AppCompatActivity {
    ListView myListView;
    Activity myActivity = Info.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        myListView = findViewById(R.id.infoList);

        String[] values = {"About the game", "Show log of recent voice commands", "Show high score"};

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, values);
        myListView.setAdapter(myArrayAdapter);
        adapterOnSelected(myListView);
    }

    public void adapterOnSelected(final ListView myListView) {
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        String about = getString(R.string.about);
                        HelperMethods.createDialog(myActivity, "About", about, "OK", "", new DialogReturnCommand() {
                            @Override
                            public void finishIt() { /* nothing */ }
                        });
                        break;
                    case 1:
                        openRecentCommands();
                        break;
                    case 2:
                        openHighScore();
                        break;
                }
            }
        });
    }

    private void openHighScore() {
        Intent myIntent = new Intent(myActivity, HighScores.class);
        startActivity(myIntent);
    }

    public void openRecentCommands() {
        Intent myIntent = new Intent(myActivity, RecentCommands.class);
        startActivity(myIntent);
    }
}
