package emp.quezy.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import emp.quezy.R;
import emp.quezy.helper.HelperMethods;

public class SelectQuiz extends AppCompatActivity {

    Spinner spnDifficulty;
    Spinner spnCategory;
    ImageView btnPlay;
    RadioGroup myGroup;
    SeekBar numOfQues;
    TextView mySeekbarText;
    private static final int MIN_VAL = 5;
    private boolean seekbarchanged = false;
    int lastQuestionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quiz);


        spnCategory = findViewById(R.id.spinnerCategory);
        btnPlay = findViewById(R.id.buttonPlay);
        numOfQues = findViewById(R.id.numOfQues);
        myGroup = findViewById(R.id.difficultyGroup);
        mySeekbarText = findViewById(R.id.textViewNumQ);


        setQuestionSeekBarAndRadioGroup();
        fillSpiners();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HelperMethods.turnOnSound(SelectQuiz.this)) {
                    HelperMethods.playSound(SelectQuiz.this, R.raw.click1);
                }
                Bundle bundle = new Bundle();
                bundle.putString("emp.quezy.category_string", spnCategory.getSelectedItem().toString());
                bundle.putString("emp.quezy.category", Integer.toString(spnCategory.getSelectedItemPosition() + 8));
                bundle.putString("emp.quezy.difficulty", ((RadioButton)findViewById(myGroup.getCheckedRadioButtonId())).getText() + "");
                bundle.putString("emp.quezy.num_questions", (seekbarchanged ? lastQuestionValue : lastQuestionValue + MIN_VAL) + "");
                Intent intent = new Intent(getApplicationContext(), GetQuestions.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }


    private void fillSpiners() {
        hideView();
        new RetrieveCategories().execute();
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

    private void setQuestionSeekBarAndRadioGroup() {

        numOfQues.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        numOfQues.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        numOfQues.setMax(45);
        numOfQues.setProgress(5);

        myGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                for (int id : new int[] { R.id.easyButton, R.id.mediumButton, R.id.hardButton}) {
                    RadioButton selected = findViewById(id);
                    if (id == i) {
                        selected.setTypeface(selected.getTypeface(), Typeface.BOLD);
                    }
                    else {
                        selected.setTypeface(null, Typeface.NORMAL);
                    }
                }
            }
        });

        if (!seekbarchanged) {
            mySeekbarText.setText(getResources().getString(R.string.numQ) + "  " + MIN_VAL);
        }

        numOfQues.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekbarchanged = true;

                i += MIN_VAL;
                i = (i / MIN_VAL) * MIN_VAL;
                mySeekbarText.setText(getResources().getString(R.string.numQ) +  ("  " + i));
                lastQuestionValue = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    /**
     * Hides all views on activity before categories are downloaded
     */
    private void hideView() {
        findViewById(R.id.fieldLayout).setVisibility(View.GONE);
    }

    /**
     * Makes all view visible after the categories were downloaded
     */
    private void showView() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.fieldLayout).setVisibility(View.VISIBLE);
    }

}
