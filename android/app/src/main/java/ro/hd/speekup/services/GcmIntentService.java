package ro.hd.speekup.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import ro.hd.speekup.classes.USO;
import ro.hd.speekup.receivers.GcmBroadcastReceiver;

public class GcmIntentService extends IntentService {
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                String rawMessage = extras.getString("message", "");
                if (!rawMessage.equals("")) {
                    /*if(!USO.isSet()) {
                        Log.i("IntentService","App closed, getting USO from storage");
                        USO.getFromPermanentStorage(getApplicationContext());
                    }
                    if(!USO.isSet()) {
                        Log.i("IntentService", "USO not set, not showing notification");
                        return;
                    }*/


                    //if app is closed and a push messages comes, USO will not be set. Fixing that

                    Log.i("IntentService", "Received: " + extras.toString());
                    //show notification only if the user is not already chatting with the sender
                        /*if (!(USO.getForegroundActivity().equals("ChatScreen") && USO.getForegroundActivityId().equals(remoteId))) {
                            sendNotification(remoteId, message);
                        }*/
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    /*private void sendNotification(String remoteId, String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        DatabaseHandler db = USO.getDatabaseHandler(getApplicationContext());
        Contact contact = db.getContact(remoteId);
        String contactName = "";
        int contactId = 0;
        Bitmap icon;
        if (contact.getId() != 0) {
            contactName = contact.getDisplayName();
            contactId = contact.getId();
            Bitmap bitmap;
            try {
                bitmap = Glide.with(getApplicationContext())
                        .load(contact.getAvatar())
                        .asBitmap()
                        .into(200, 200)
                        .get();
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
            }
            icon = ImageTools.cropSquareBitmap(bitmap);
        } else {
            contactName = "+" + remoteId;
            icon = ImageTools.cropSquareBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar));
        }

        Intent chatScreenIntent = new Intent(getApplicationContext(), ChatListActivity.class);
        chatScreenIntent.putExtra("REMOTE_ID", remoteId);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, chatScreenIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.logo_notification)
                        .setContentTitle(contactName)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setGroup("moneymailme_messages")
                        .setAutoCancel(true);

        RoundedBitmapDrawable rIcon = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), icon);
        rIcon.setCornerRadius(Math.max(rIcon.getIntrinsicWidth(), rIcon.getIntrinsicHeight()) / 2.0f);
        mBuilder.setLargeIcon(ImageTools.drawableToBitmap(rIcon));


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(remoteId, contactId, mBuilder.build());
    }*/


}