package emp.quezy.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import emp.quezy.R;

public class Quiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        TextView tx = findViewById(R.id.proba);

        Intent intent = getIntent();
        if (intent != null){
            Bundle bnd = intent.getExtras();
            if (bnd != null){
                String cat = bnd.getString("emp.quezy.category");
                String dif = bnd.getString("emp.quezy.difficulty");
                String nQ = bnd.getString("emp.quezy.num_questions");

                String API = "https://opentdb.com/api.php?amount="+nQ+"&category="+cat+"&difficulty="+dif+"&type=multiple";
                tx.setText(API);
            }
        }
    }
}
