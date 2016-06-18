package com.p2pdinner.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.p2pdinner.R;
import com.p2pdinner.activities.MainActivity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Created by rajaniy on 1/4/16.
 */
public class P2PDinnerListenerService extends GcmListenerService {
    private static final String TAG = "P2PGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String price = data.getString("price");
        String confirmationCode = data.getString("confirmationCode");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTime startDtTime = formatter.withZoneUTC().parseDateTime(data.getString("startTime"));
        DateTime endDtTime = formatter.withZoneUTC().parseDateTime(data.getString("endTime"));
        String address = data.getString("address");
        DateTimeFormatter printFormatter = new DateTimeFormatterBuilder()
                .appendHourOfHalfday(2)
                .appendLiteral(":")
                .appendMinuteOfHour(2)
                .appendLiteral(" ")
                .appendHalfdayOfDayText().toFormatter();
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + title);

        String message = String.format("Pickup between %s and %s, %s. confirmation code: %s",
                printFormatter.withZone(DateTimeZone.getDefault()).print(startDtTime),
                printFormatter.withZone(DateTimeZone.getDefault()).print(endDtTime),
                address,
                confirmationCode);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.p2p_logo_small)
                .setContentTitle("P2P Dinner")
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
