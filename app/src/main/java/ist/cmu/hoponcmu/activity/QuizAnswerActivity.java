package ist.cmu.hoponcmu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import ist.cmu.hoponcmu.CMUtils;
import ist.cmu.hoponcmu.R;

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
                radioGroup.setTag(id);

                TextView textView = new TextView(this);
                textView.setText(question);
                radioGroup.addView(textView);

                JSONArray optionsArray = questionJSON.getJSONArray("options");
                for (int j = 0; j < optionsArray.length(); j++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(optionsArray.getString(j));
                    radioButton.setTag(i);
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
                LinearLayout layoutView = findViewById(R.id.layout_radios);
                final int NUM = layoutView.getChildCount();

                int[] answers = new int[NUM];

                for (int i = 0; i < layoutView.getChildCount(); i++) {
                    View v = layoutView.getChildAt(i);
                    if (v instanceof RadioGroup) {
                        RadioGroup radioGroup = (RadioGroup) v;
                        Integer id = (Integer) radioGroup.getTag();
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = radioGroup.findViewById(radioButtonID);
                        Integer tag = (Integer) radioButton.getTag();

                        answers[i] = radioButtonID;

                        SharedPreferences prefs = getSharedPreferences(
                                CMUtils.DATA_NAME, Context.MODE_PRIVATE);
                        String authToken = prefs.getString("token", null);

                        String postData = String.format("?questionId=%s&answer=%s", id, tag);
                        CMUtils.postData("quizzes", postData, authToken);
                    }
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("ANSWERS", answers);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
