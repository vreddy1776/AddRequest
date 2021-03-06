/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package project.files.android.addrequest.Background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;

import project.files.android.addrequest.Activity.AddTicket.AddTicketActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.C;


/**
 * Notifications
 *
 * Class with all Notification references.
 *
 * @author Vijay T. Reddy
 * @version 1.0.0
 */
public class Notifications {

    /*
     * This notification IdUtils can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int NOTIFICATION_ID = 1138;
    private static final String MAIN_NOTIFICATION_CHANNEL_ID = "main_notification_channel";
    private static final int OPEN_REQUEST = 0;
    private static int mTicketID = C.DEFAULT_TICKET_ID;


    public static void ticketPostedNotification(int ticketID) {

        Context context = MyApplication.getAppContext();
        mTicketID = ticketID;

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MAIN_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, MAIN_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.notification_body)))
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                .addAction(openTicketAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }


    private static Action openTicketAction(Context context) {

        Intent intent = new Intent(context, AddTicketActivity.class);
        intent.putExtra(C.TICKET_ID_KEY, mTicketID);
        intent.putExtra(C.TICKET_TYPE_KEY, C.UPDATE_TICKET_TYPE);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                OPEN_REQUEST,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Action action = new Action(R.drawable.ic_launcher_foreground,
                "View Your Request",
                pendingIntent);

        return action;

    }


    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_launcher_foreground);
        return largeIcon;
    }
}