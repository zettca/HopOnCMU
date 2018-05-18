package ist.cmu.hoponcmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        Intent intent = getIntent();

        String data = intent.getStringExtra("DATA");
        LinearLayout layoutView = findViewById(R.id.layout_radios);

        try {
            JSONArray questions = new JSONArray(data);
            for (int i = 0; i < questions.length(); i++) {
                RadioGroup radioGroup = new RadioGroup(this);

                JSONObject questionJSON = questions.getJSONObject(i);
                int id = questionJSON.getInt("id");
                String question = questionJSON.getString("question");

                TextView textView = new TextView(this);
                textView.setText(question);
                radioGroup.addView(textView);

                JSONArray optionsArray = questionJSON.getJSONArray("options");
                for (int j = 0; j < optionsArray.length(); j++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(optionsArray.getString(i));
                    radioGroup.addView(radioButton);
                }

                layoutView.addView(radioGroup);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button buttonSubmit = findViewById(R.id.button_submit_quiz);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 0; //radioGroup.getCheckedRadioButtonId();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ANSWER", index);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
