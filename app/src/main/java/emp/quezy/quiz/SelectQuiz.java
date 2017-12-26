package emp.quezy.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import emp.quezy.R;

public class SelectQuiz extends AppCompatActivity {

    Spinner spnDifficulty;
    Spinner spnNumQuestions;
    Spinner spnCategory;
    Button btnPlay;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quiz);

        spnDifficulty = findViewById(R.id.spinnerDifficulty);
        spnNumQuestions = findViewById(R.id.spinnerNumQuestions);
        spnCategory = findViewById(R.id.spinnerCategory);

        btnPlay = findViewById(R.id.buttonPlay);

        fillSpiners();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("emp.quezy.category", Integer.toString(spnCategory.getSelectedItemPosition() + 8));
                bundle.putString("emp.quezy.difficulty", spnDifficulty.getSelectedItem().toString());
                bundle.putString("emp.quezy.num_questions", spnNumQuestions.getSelectedItem().toString());

                Intent intent = new Intent(SelectQuiz.this, PlayQuiz.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void fillSpiners() {
        hideView();

        new RetrieveCategories().execute();

        ArrayAdapter<CharSequence> adapterDiff = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDifficulty.setAdapter(adapterDiff);

        ArrayAdapter<CharSequence> adapterNumQ = ArrayAdapter.createFromResource(this,
                R.array.questions_array, android.R.layout.simple_spinner_item);
        adapterNumQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNumQuestions.setAdapter(adapterNumQ);

    }


    /**
     * Reads and parses the JSON data for the categories
     */
    private class RetrieveCategories extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideView();
        }

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
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("trivia_categories");
                String[] values = new String[jsonArray.length() + 1];
                values[0] = "Any Category";
                for (int i = 1; i <= jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i - 1);
                    values[i] = jObject.getString("name");
                }

                ArrayAdapter<String> adapterQuestions = new ArrayAdapter<>(SelectQuiz.this, android.R.layout.simple_spinner_item, values);
                adapterQuestions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategory.setAdapter(adapterQuestions);

                showView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hides all views on activity before categories are downloaded
     */
    private void hideView() {
        spnDifficulty.setVisibility(View.GONE);
        spnNumQuestions.setVisibility(View.GONE);
        spnCategory.setVisibility(View.GONE);
        btnPlay.setVisibility(View.GONE);
        findViewById(R.id.textViewCat).setVisibility(View.GONE);
        findViewById(R.id.textViewNumQ).setVisibility(View.GONE);
        findViewById(R.id.textViewDif).setVisibility(View.GONE);
    }

    /**
     * Makes all view visible after the categories were downloaded
     */
    private void showView() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        spnDifficulty.setVisibility(View.VISIBLE);
        spnNumQuestions.setVisibility(View.VISIBLE);
        spnCategory.setVisibility(View.VISIBLE);
        btnPlay.setVisibility(View.VISIBLE);
        findViewById(R.id.textViewCat).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewNumQ).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewDif).setVisibility(View.VISIBLE);
    }

}
