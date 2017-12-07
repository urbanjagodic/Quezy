package emp.quezy.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import emp.quezy.R;
import emp.quezy.helper.HelperMethods;

public class Settings extends AppCompatActivity {



    private ListView myListView;

    private Activity myActivity = Settings.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myListView = (ListView) findViewById(R.id.settingsList);


        String[] values = { "Voice control", "Sound", "Save highscore to external storage"};

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, values);
        myListView.setAdapter(myArrayAdapter);
        adapterOnSelected(myListView);

    }



    public void adapterOnSelected(final ListView myListView) {

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HelperMethods.showToast(myActivity, adapterView.getItemAtPosition(i).toString());
            }
        });


    }
}
