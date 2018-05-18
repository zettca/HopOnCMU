package ist.cmu.hoponcmu;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;

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

        SharedPreferences prefs = this.getSharedPreferences(CMUtils.DATA_NAME, Context.MODE_PRIVATE);
        boolean logged = prefs.getBoolean("logged", false);
        String username = prefs.getString("username", null);

        mTextMessage = (TextView) findViewById(R.id.message);

        Button mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogout();
            }
        });

        if (logged && username != null) {
            mTextMessage.setText(getString(R.string.welcome, username));

            GetQuizzesTask mQuizzesTask = new GetQuizzesTask("M14");
            mQuizzesTask.execute((Void) null);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SimWifiP2pSocketManager.Init(this.getApplicationContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);

        WifiDirectBroadcastReceiver receiver = new WifiDirectBroadcastReceiver(this);
        registerReceiver(receiver, filter);

    }

    private void doLogout() {
        SharedPreferences prefs = getSharedPreferences(CMUtils.DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("logged", false);
        editor.remove("username");
        editor.remove("token");
        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
    }

    public class GetQuizzesTask extends AsyncTask<Void, Void, Boolean> {
        private String mQuizId;
        private JSONArray quizzesArray;

        public GetQuizzesTask(String quizId) {
            mQuizId = quizId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String getParams = "?location=" + mQuizId;

            SharedPreferences prefs = getSharedPreferences(CMUtils.DATA_NAME, Context.MODE_PRIVATE);
            String authToken = prefs.getString("token", "");

            Response response = CMUtils.getData("quizzes", getParams, authToken);

            try {
                String data = response.body().string();
                mTextMessage.setText(data);
                //Toast.makeText(this, data.toCharArray(), Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject(data);
                quizzesArray = jsonObject.getJSONArray(("quizzes"));
                return true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);

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
