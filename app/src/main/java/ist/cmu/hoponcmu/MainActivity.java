package ist.cmu.hoponcmu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = LocationFragment.newInstance();
                    mTextMessage.setText(R.string.title_home);
                    break;
                case R.id.navigation_dashboard:
                    fragment = QuizzesFragment.newInstance();
                    mTextMessage.setText(R.string.title_dashboard);
                    break;
                case R.id.navigation_notifications:
                    fragment = RankingFragment.newInstance();
                    mTextMessage.setText(R.string.title_notifications);
                    break;
            }

            if (fragment == null) {
                return false;
            } else {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(CMUtils.DATA_NAME, Context.MODE_PRIVATE);
        boolean logged = prefs.getBoolean("logged", false);
        String username = prefs.getString("username", null);
        String authToken = prefs.getString("token", null);

        mTextMessage = findViewById(R.id.message);

        Button mLogoutButton = findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogout();
            }
        });

        Button mConnectButton = findViewById(R.id.connect_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConnect();
            }
        });

        if (logged && username != null) {
            mTextMessage.setText(getString(R.string.welcome, username));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        doFindQuizzes();
    }

    private void doFindQuizzes() {
        SharedPreferences prefs = getSharedPreferences(CMUtils.DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> locationIDs = new HashSet<>(Arrays.asList("M14", "M42"));
        editor.putStringSet("locationIDs", locationIDs);
        editor.apply();
    }

    private void doConnect() {
        //TODO: implement WiFi-direct stuff

        Toast toast = Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT);
        toast.show();
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
}
