package ist.cmu.hoponcmu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences(Config.DATA_NAME, Context.MODE_PRIVATE);
        boolean logged = prefs.getBoolean("logged", false);
        String username = prefs.getString("username", null);

        mTextMessage = (TextView) findViewById(R.id.message);

        if (logged && username != null) {
            mTextMessage.setText(getString(R.string.welcome_back, username));
            String quizId = "5";
            //GetQuizzesTask mQuizzesTask = new GetQuizzesTask(quizId);
            //mQuizzesTask.execute((Void) null);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public class GetQuizzesTask extends AsyncTask<Void, Void, Boolean> {
        private String mQuizId;
        private JSONArray quizzesArray;

        public GetQuizzesTask(String quizId) {
            mQuizId = quizId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            try {
                URL url = new URL("http://localhost:8080/quizzes/5" + mQuizId);
                Log.d("URLCENAS", url.toString());

                Request request = new Request.Builder().url(url).build();
                Response response;

                response = client.newCall(request).execute();
                String string = response.body().string();
                JSONObject jsonObject = new JSONObject(string);
                quizzesArray = jsonObject.getJSONArray(("quizzes"));

                return (quizzesArray != null && quizzesArray.length() > 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);

            Log.d("QUZZI RESULT", success.toString());
            if (success) {
                doStuffwithQuizzes(quizzesArray);
            }
        }
    }

    private void doStuffwithQuizzes(JSONArray quizzesArray) {
        try {
            mTextMessage.setText(quizzesArray.join(" "));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
