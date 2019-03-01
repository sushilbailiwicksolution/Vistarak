package bailiwick.bjpukh.com.vistarak.Support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkState {

    public static boolean isNetworkAvailable(Context mContext) {
    /*	boolean outcome = false;
		if (mContext != null) {
			ConnectivityManager cm = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
			for (NetworkInfo tempNetworkInfo : networkInfos) {
				if (tempNetworkInfo.isConnected()) {
					outcome = true;
					break;
				}
			}
		}*/

        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi.isConnected() || mobile.isConnected());
    }
}
