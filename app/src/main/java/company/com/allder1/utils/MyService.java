package company.com.allder1.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import company.com.allder1.Activity.CartActivity;
import company.com.allder1.Activity.MainActivity;
import company.com.allder1.Activity.Sheet_payman;
import company.com.allder1.R;

public class MyService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;
    public static String notification;
//  public  static  String getNotificationPayment;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Manh", "service Notification: " + remoteMessage.getData());
        sendnotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        if (remoteMessage.getNotification() != null) {
            Log.d("Manh1", "service Notification: " + remoteMessage.getNotification().getBody());
            Log.d("Manh2", "service Notification: " + remoteMessage.getNotification().getTitle());
//            getNotificationPayment = String.valueOf(remoteMessage.getNotification());
//            Log.d("Thanhcong",getNotificationPayment);
            sendnotification(remoteMessage.getData().get("action"), remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }
        if (remoteMessage.getData().size() > 0){
            Log.d("Man3", "service Notification" + remoteMessage.getData());
            Log.d("Manh4", "service Notification" + remoteMessage.getData().get("action"));
            notification = remoteMessage.getData().get("action");
        }
    }
    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    private void sendnotification(String action, String messageBody, String Title) {
        Intent intent = null;
        if (action != null) {
            if (action.equals("order")) {
//            intent = new Intent(this, NotificationFragment.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Log.d("sdfsd", "sendnotification: ");
                Sheet_payman.CloesPayment();
                if (new PreferenceHelper(this).getSpecies().contains("provider")) {
                    intent = new Intent("Comingorder");
                    intent.putExtra("notify", "order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    broadcaster.sendBroadcast(intent);
                } else {
                    intent = new Intent("Orderconsumer");
                    intent.putExtra("notify", "order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    broadcaster.sendBroadcast(intent);
                }

            } else if (action.equals("approved_order") || action.equals("declined_order")) {
                Sheet_payman.CloesPayment();
                if (new PreferenceHelper(this).getSpecies().contains("consumer")) {
                    intent = new Intent("Orderconsumer");
                    //Sheet_payman.CloesPayment();
                    intent.putExtra("notify", "approved_declined_order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    broadcaster.sendBroadcast(intent);
                } else {
                    Log.d("Manh", "sendnotification1111 " + action);
                    intent = new Intent("Comingorder");
                    // intent = new Intent("Provideraddorder");
                    intent.putExtra("notify", "approved_declined_order");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    broadcaster.sendBroadcast(intent);
                }
            } else if (action.equals("provider_add_order")) {
                intent = new Intent("Comingorder");
                intent.putExtra("notify", "provider_add_order");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                broadcaster.sendBroadcast(intent);
            }
            PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_logoallder)
                    .setContentTitle(Title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationbuilder.build());
        }
    }
    private void sendnotification(String messageBody, String Title){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logoallder)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationbuilder.build());
    }

}
