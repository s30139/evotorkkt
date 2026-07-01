package ru.svod.evotorkkt;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ru.evotor.pushNotifications.PushNotificationReceiver;

public class PushReceiver extends PushNotificationReceiver {

    @Override
    public void onReceivePushNotification(@NonNull Context context, @NonNull Bundle data, long messageId) {

        //...receive push notification
        //Toast.makeText(context, data.getString("header") + " " + data.getString("description")
        //        + " " + messageId, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "onReceivePushNotification", Toast.LENGTH_LONG).show();

    }


}
