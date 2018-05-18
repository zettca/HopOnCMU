package ist.cmu.hoponcmu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class QuizAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_answer);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_options);

        RadioButton radioButton;
        for (int i = 0; i < 4; i++) {
            radioButton = new RadioButton(this);
            radioButton.setText("Radio " + i);
            radioGroup.addView(radioButton);
        }

    }
}
