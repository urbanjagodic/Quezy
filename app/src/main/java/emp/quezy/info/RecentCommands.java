package emp.quezy.info;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;
import emp.quezy.other.ContentStore;

public class RecentCommands extends AppCompatActivity {

    ListView myListView;
    Activity myActivity = RecentCommands.this;
    FloatingActionButton clearCommandsButton;
    ArrayAdapter<String> myArrayAdapter;


    private ArrayList<String> dateAddedByList = new ArrayList<>();
   private ArrayList<String> commands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_commands);

        getValues();
        myListView = findViewById(R.id.recentCommandsList);

        myArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, commands);
        myListView.setAdapter(myArrayAdapter);



        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String value = dateAddedByList.get(i);
                HelperMethods.showToast(myActivity, "Added on: " + value);
            }
        });

        clearCommandsButton = findViewById(R.id.clearCommandsButton);

        clearCommandsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HelperMethods.createDialog(myActivity, "Delete voice command record", "Are you sure you want to delete?",
                        "Yes", "Canel", new DialogReturnCommand() {
                            @Override
                            public void finishIt() {
                                ContentStore.initialize(myActivity);
                                ContentStore.removeValue("voiceCommands");
                                myArrayAdapter.clear();
                                myArrayAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    public void getValues() {

        ContentStore.initialize(myActivity);
        SharedPreferences myPrefs = ContentStore.getMyPrefrences();

        Object retrieveStringValues = myPrefs.getString("voiceCommands", "");
        String[] values = ((String)retrieveStringValues).split(";");

        for (String val : values) {
            String[] command = val.split("\\|");

            if (command.length < 2) {
                continue;
            }
            dateAddedByList.add(command[0].replace("GMT+01:00", ""));
            commands.add(command[1]);
        }
    }
}
