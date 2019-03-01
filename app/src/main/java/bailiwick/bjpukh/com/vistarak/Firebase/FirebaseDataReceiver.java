package bailiwick.bjpukh.com.vistarak.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import bailiwick.bjpukh.com.vistarak.Booth_record;
import bailiwick.bjpukh.com.vistarak.R;
import bailiwick.bjpukh.com.vistarak.Support.SavedData;
import bailiwick.bjpukh.com.vistarak.UtilsUrl.Itag;

/**
 * Created by Prince on 05-10-2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GCMIntentService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received message");

        for (String key : intent.getExtras().keySet()) {
            Object value = intent.getExtras().get(key);
            Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
        }

        for (String key : intent.getExtras().keySet()) {
            Object value = intent.getExtras().get(key);
            Log.d(TAG, String.format("%s %s (%s)", key, value.toString(), value
                    .getClass().getName()));

            Log.e(TAG, String.format("%s %s (%s)", key, value.toString(), value
                    .getClass().getName()));

        }

        if (!SavedData.getNotify()) {

            String message = intent.getExtras().getString("message");
            Intent i = new Intent(context, Booth_record.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Itag.FCM_Message, message);
            context.startActivity(i);
            Log.e(TAG, "prince 1 : " + message);
            sendNotification(message,context);
        }else {
            String message = intent.getExtras().getString("message");

            sendNotification(message,context);
        }
    }
    private void sendNotification(String messageBody,Context context) {
        Log.e("Message", messageBody);
        SavedData.saveNotify(true);
        Log.e("prince", "prince" + SavedData.getNotify());
        Intent intent = new Intent(context, Booth_record.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("NotificationMessage", messageBody);
        intent.putExtra(Itag.FCM_Message, messageBody);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("Vistarak : ")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
