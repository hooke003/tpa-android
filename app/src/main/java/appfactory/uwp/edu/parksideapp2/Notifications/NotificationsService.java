package appfactory.uwp.edu.parksideapp2.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.ApplicationClass.MyApplication;
import appfactory.uwp.edu.parksideapp2.ComputerLab.LabActivity;
import appfactory.uwp.edu.parksideapp2.Event.EventsActivity;
import appfactory.uwp.edu.parksideapp2.Map.MainMapActivity;
import appfactory.uwp.edu.parksideapp2.News.NewsActivity;
import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.TitleIX.TitleIXActivity;
import appfactory.uwp.edu.parksideapp2.menu.MenuLandingActivity;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDHomeActivity;
import appfactory.uwp.edu.parksideapp2.ranger_wellness.RWDashboardActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

/**
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 * <p>
 * See https://firebase.google.com/docs/cloud-messaging for information on FCM for notifications and
 * data being sent form server to client.
 */
public class NotificationsService extends FirebaseMessagingService {
    private PendingIntent pendingIntent;
    private int notificationIcon;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: %s", remoteMessage.getFrom());
        // Opens the app to the MyApplication file (Start up)

        // This is the default launching Activity
        Intent intent = new Intent(this, MyApplication.class);

        Map<String, String> dataPayload = remoteMessage.getData();

        Timber.d(dataPayload.toString());

        String topic = remoteMessage.getNotification().getClickAction();
        String channelId;
        // Set the Intent based on the notification topic
        switch (topic.toLowerCase()) {
            case "wellness": {
                channelId = "Ranger Wellness";
                notificationIcon = R.drawable.ic_ranger_wellness_small_no_bg;
                if (UserConstants.RW_AUTHORIZED)
                    intent = new Intent(this, RWDashboardActivity.class);
            }
            break;
            case "restart": {
                channelId = "Ranger Restart";
                notificationIcon = R.drawable.ic_ranger_restart_small_no_bg;
                intent = new Intent(this, RRCOVIDHomeActivity.class);
            }
            break;
            case "navigate": {
                channelId = "Navigate";
                notificationIcon = R.drawable.ic_navigate_small_no_bg;
                intent = new Intent(this, TPA2LandingActivity.class);
            }
            break;
            case "computer": {
                channelId = "Computer Labs";
                notificationIcon = R.drawable.ic_computer_labs_small_no_bg;
                intent = new Intent(this, LabActivity.class);
            }
            break;
            case "news": {
                channelId = "News";
                notificationIcon = R.drawable.ic_news_small_no_bg;
                intent = new Intent(this, NewsActivity.class);
            }
            break;
            case "eaccounts": {
                channelId = "eAccounts";
                notificationIcon = R.drawable.ic_eaccounts_small_no_bg;
                intent = new Intent(this, TPA2LandingActivity.class);
            }
            break;
            case "map": {
                channelId = "Map";
                notificationIcon = R.drawable.ic_maps_small_no_bg;
                intent = new Intent(this, MainMapActivity.class);
            }
            break;
            case "events": {
                channelId = "Events";
                notificationIcon = R.drawable.ic_events_small_no_bg;
                intent = new Intent(this, EventsActivity.class);
            }
            break;
            case "menu": {
                channelId = "Menu";
                notificationIcon = R.drawable.ic_menu_tile;
                intent = new Intent(this, MenuLandingActivity.class);
            }
            break;
            case "title ix": {
                channelId = "Title IX";
                notificationIcon = R.mipmap.ic_titleix;
                intent = new Intent(this, TitleIXActivity.class);
            }
            break;
            default: {
                channelId = "Main";
                notificationIcon = R.drawable.ic_bell_inactive;
                intent = new Intent(this, TPA2LandingActivity.class);
            }
            break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, 0);
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), channelId);
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NotNull String token) {
        Timber.d("Refreshed token: %s", token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageTitle FCM message title received.
     * @param messageBody  FCM message body received.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String messageTitle, String messageBody, String channelId) {
        // Sets the sound of the notification to the default one.
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                channelId.toLowerCase(),
                channelId,
                NotificationManager.IMPORTANCE_HIGH
        );
        notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));

        // Create a Notification object
        NotificationCompat.Builder notificationBuilder =
                Objects.requireNonNull(
                        ManageNotifications.createNotification(null, this, channelId,
                                notificationIcon, messageTitle, messageBody,
                                NotificationCompat.PRIORITY_MAX
                        )
                )
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSound(defaultSoundUri);

        // Give the notification a unique ID based on the time
        notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
    }
}
