package ist.cmu.hoponcmu;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        Intent intent = getIntent();

        String question = intent.getStringExtra("QUESTION");
        String[] options = intent.getStringArrayExtra("OPTIONS");

        if (question == null || options.length == 0) {
            Toast.makeText(this, "No data...", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView textQuestion = findViewById(R.id.text_question);
        final RadioGroup radioGroup = findViewById(R.id.radiogroup_options);
        Button buttonSubmit = findViewById(R.id.button_submit_quiz);

        textQuestion.setText(question);

        for (int i = 0; i < options.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(options[i]);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = radioGroup.getCheckedRadioButtonId();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ANSWER", index);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
