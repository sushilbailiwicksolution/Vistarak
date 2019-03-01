package bailiwick.bjpukh.com.vistarak.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Prince on 08-11-2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static String FCM_Token;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Log.e(TAG, "Refreshed token: eroorr    ...   " + refreshedToken);
        FCM_Token = refreshedToken;
    }
    public String getFcm(){
        String refreshedToken;
         refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(refreshedToken == null)
            return "";
        return refreshedToken;
    }


    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}