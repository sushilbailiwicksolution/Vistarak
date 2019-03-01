package bailiwick.bjpukh.com.vistarak.Support;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import bailiwick.bjpukh.com.vistarak.Firebase.MyFirebaseInstanceIDService;

/**
 * Created by Prince on 06-10-2017.
 */

public class DeviceOperation {
    public static String getDeviceDetail(Context con) {
        String deviceID = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);


        return deviceID;
    }

//    public static String getEmi(Context con) {
//        String identifier = "";
//
//        TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
//
//        if (tm != null)
//            identifier = tm.getDeviceId();
//        if (identifier == null || identifier.length() == 0)
//            identifier = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        return identifier;
//    }

    public static String getFcmkey() {
        MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
        String fcmtoken = myfcm.getFcm();
        Log.e("fcm id", fcmtoken);
        if (fcmtoken.isEmpty()) {
            getFcmkey();
            Log.e("Fcm repeat", "empty : " + fcmtoken);
        }
        return fcmtoken;
        //   DB_Function.SavetextFile(Booth_record.this, fcmtoken);
    }

}
