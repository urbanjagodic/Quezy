package emp.quezy.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import emp.quezy.R;

public class GetQuestions extends AppCompatActivity {

    ArrayList<Question> questions;        // Kle v temu listu so vsa vprašanja
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_questions);

        questions = new ArrayList<>();

        Intent intent = getIntent();
        String url = null;
        if (intent != null) {
            url = getURL(intent.getExtras());
        }

        new ReadJSON().execute(url);

    }

    private static String getURL(Bundle bnd) {
        if (bnd != null) {
            String cat = bnd.getString("emp.quezy.category");
            String dif = bnd.getString("emp.quezy.difficulty").toLowerCase();
            String nQ = bnd.getString("emp.quezy.num_questions");
            Log.i("nQ", nQ);

            if (Integer.parseInt(cat) == 8) {
                return "https://opentdb.com/api.php?amount=" + nQ + "&difficulty=" + dif + "&type=multiple";
            } else {
                return "https://opentdb.com/api.php?amount=" + nQ + "&category=" + cat + "&difficulty=" + dif + "&type=multiple";
            }
        }
        return null;
    }

    private class ReadJSON extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                //tx.setText(strings[0]);
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

        @Override
        protected void onPostExecute(String s) {
            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);

            try {
                jsonObject = new JSONObject(s);
                parseJSON(jsonObject);
                //findViewById(R.id.loadingPanel2).setVisibility(View.GONE);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("emp.quezy.questionsList", questions);

                Intent intent = new Intent(getApplicationContext(), PlayQuiz.class);
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void parseJSON(JSONObject jObj) {
        try {
            int response = jObj.getInt("response_code");
            if (response != 0)
                return;

            JSONArray jsonArray = jObj.getJSONArray("results");
            Log.i("length", String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject qs = jsonArray.getJSONObject(i);
                String str = qs.getString("question");
                String right = qs.getString("correct_answer");
                //String wrong = qs.getString("incorrect_answers");
                JSONArray jaWrong = qs.getJSONArray("incorrect_answers");
                String[] wrong = new String[jaWrong.length()];
                for (int j = 0; j < jaWrong.length(); j++) {
                    wrong[j] = jaWrong.getString(j);
                }
                Log.i("wrong", Arrays.toString(wrong));
                this.questions.add(new Question(str, right, wrong));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

