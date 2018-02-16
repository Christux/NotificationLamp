package fr.christux.notificationlamp;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static android.app.Notification.COLOR_DEFAULT;

public class LampNotificationService extends NotificationListenerService {

    private int sbnID;

    public LampNotificationService() {
        Log.d("LampNotificationService", "Lamp notification service launched");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Log.d("LampNotificationService", "onNotificationPosted: "+sbn.getPackageName());
        sbnID=sbn.getId();
        BTLamp.getInstance().turnOnLed();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Log.d("LampNotificationService", "onNotificationRemoved: "+sbn.getPackageName());
        if(sbnID == sbn.getId()) {
            BTLamp.getInstance().turnOffLed();
        }
    }
}
