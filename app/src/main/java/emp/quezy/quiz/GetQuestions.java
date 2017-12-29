package emp.quezy.quiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import emp.quezy.R;
import emp.quezy.helper.DialogReturnCommand;
import emp.quezy.helper.HelperMethods;

public class GetQuestions extends AppCompatActivity {

    ArrayList<Question> questions;        // Kle v temu listu so vsa vprašanja
    JSONObject jsonObject;
    public static String dif;

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

    public static String getURL(Bundle bnd) {
        if (bnd != null) {
            String cat = bnd.getString("emp.quezy.category");
            dif = bnd.getString("emp.quezy.difficulty").toLowerCase();
            String nQ = bnd.getString("emp.quezy.num_questions");

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
                int response = parseJSON(jsonObject);
                Log.i("response_return", Integer.toString(response));
                if (response == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("emp.quezy.questionsList", questions);
                    bundle.putString("emp.quezy.difficulty", dif);

                    Intent intent = new Intent(getApplicationContext(), PlayQuiz.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    HelperMethods.createDialog(GetQuestions.this, "Error", "Ups, this category doesn't have enough questions. Chose a different category or difficulty.",
                            "Ok", "", new DialogReturnCommand() {
                                @Override
                                public void finishIt() {
                                    finish();
                                }
                            });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private int parseJSON(JSONObject jObj) {
        int response = -1;
        try {
            response = jObj.getInt("response_code");
            if (response != 0) {
                Log.i("response_code", Integer.toString(response));
            } else {
                JSONArray jsonArray = jObj.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject qs = jsonArray.getJSONObject(i);
                    String str = HelperMethods.unescapeChars(qs.getString("question"));
                    String right = HelperMethods.unescapeChars(qs.getString("correct_answer"));
                    JSONArray jaWrong = qs.getJSONArray("incorrect_answers");
                    String[] wrong = new String[jaWrong.length()];
                    for (int j = 0; j < jaWrong.length(); j++) {
                        wrong[j] = HelperMethods.unescapeChars(jaWrong.getString(j));
                    }
                    this.questions.add(new Question(str, right, wrong));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}

