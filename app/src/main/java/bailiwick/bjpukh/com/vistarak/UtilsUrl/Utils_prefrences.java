package bailiwick.bjpukh.com.vistarak.UtilsUrl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prince on 02-12-2016.
 */

public class Utils_prefrences {
    public static final String pref_isLogin = "pref_isLogin";
    public static final String pref_mobile = "phone";
    public static final String pref_isStart = "pref_isStart";

    public static final String pref_latitude = "pref_latitude";
    public static final String pref_Longitude = "pref_Longitude";
    public static final String pref_Last_Address = "pref_Last_Address";
    public static final String pref_pendingPoints = "pref_pendingPoints";
    public static final String pref_timeStamp = "pref_timeStamp";
    public static final String pref_message = "pref_message";

    public static final String pref_isServiceStarted = "pref_isServiceStarted";

    public static final String pref_isTaskStarted = "pref_isTaskStarted";

    public static final int pref_notificationId = 254;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
