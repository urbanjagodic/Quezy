package emp.quezy.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import emp.quezy.R;

public class SelectQuiz extends AppCompatActivity {

    Spinner spnDifficulty;
    Spinner spnNumQuestions;
    Spinner spnCategory;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quiz);

        spnDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);
        spnNumQuestions = (Spinner) findViewById(R.id.spinnerNumQuestions);
        spnCategory = (Spinner) findViewById(R.id.spinnerCategory);

        btnPlay = (Button) findViewById(R.id.buttonPlay);

        fillSpiners();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("emp.quezy.category", Integer.toString(spnCategory.getSelectedItemPosition()+9));
                bundle.putString("emp.quezy.difficulty", spnDifficulty.getSelectedItem().toString());
                bundle.putString("emp.quezy.num_questions", spnNumQuestions.getSelectedItem().toString());

                Intent intent = new Intent(SelectQuiz.this, Quiz.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void fillSpiners() {
        new RetrieveFeedTask().execute();

        ArrayAdapter<CharSequence> adapterDiff = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDifficulty.setAdapter(adapterDiff);

        ArrayAdapter<CharSequence> adapterNumQ = ArrayAdapter.createFromResource(this,
                R.array.questions_array, android.R.layout.simple_spinner_item);
        adapterNumQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNumQuestions.setAdapter(adapterNumQ);

    }

    private void readCategories() {

        String str = getResources().getString(R.string.jsonCategories);

        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("trivia_categories");
            String [] values = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                values[i] = jObject.getString("name");
            }

            ArrayAdapter<String> adapterQuestions = new ArrayAdapter<String>(SelectQuiz.this, android.R.layout.simple_spinner_item, values);
            adapterQuestions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategory.setAdapter(adapterQuestions);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://opentdb.com/api_category.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response){

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("trivia_categories");
                String [] values = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    values[i] = jObject.getString("name");
                }

                ArrayAdapter<String> adapterQuestions = new ArrayAdapter<String>(SelectQuiz.this, android.R.layout.simple_spinner_item, values);
                adapterQuestions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategory.setAdapter(adapterQuestions);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
