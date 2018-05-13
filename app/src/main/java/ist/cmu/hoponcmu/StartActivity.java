package ist.cmu.hoponcmu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = this.getSharedPreferences(Config.DATA_NAME, Context.MODE_PRIVATE);
        final boolean isLoggedIn = prefs.getBoolean("logged", false);
        Log.d("LOGGED VALUE", isLoggedIn ? "TRUE" : "FALSE");

        // TODO: implement some kind of session (JWT?)

        if (isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
