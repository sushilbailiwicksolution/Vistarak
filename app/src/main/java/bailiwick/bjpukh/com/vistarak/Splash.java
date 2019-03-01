package bailiwick.bjpukh.com.vistarak;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nullwire.trace.ExceptionHandler;

import bailiwick.bjpukh.com.vistarak.LocationService.LocationService;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;

/**
 * Created by Prince on 22-11-2016.
 */

public class Splash extends AppCompatActivity {
// requriment
/*
1    imei no verification
2    Get Version work in mobile
3    In Web Show Sc Area According to booth
4
*/


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences pref;
    boolean isLogin = false;

    Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   ExceptionHandler.register(this, "http://uvcabs.esy.es/crash_log_vistarak/server.php");
        setContentView(R.layout.splash);


        isLogin = SavedData.getLogin();
        Log.e("Login Value ",""+isLogin);
        locationService = new Intent(Splash.this, LocationService.class);

        startSplash();
    }

    private void startLocationUpdates() {
        locationService.putExtra("mobile", "1234567890");
        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.startService(locationService);
    }

    private void stopLocationUpdates() {

        ContextWrapper cont = new ContextWrapper(getBaseContext());
        cont.stopService(locationService);
    }

    private void startSplash() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Log.e("Login Value ",""+isLogin);

                if (isLogin) {
                    Intent i = new Intent(Splash.this, Booth_record.class);
                    if (!SavedData.getLeaveStatus()) {
                        stopLocationUpdates();
                        startLocationUpdates();
                    }
                    startActivity(i);

                } else {
                    Intent i = new Intent(Splash.this, LoginActivity.class);
                    startActivity(i);

                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}





