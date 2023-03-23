package appfactory.uwp.edu.parksideapp2.geofencing;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import appfactory.uwp.edu.parksideapp2.Notifications.ManageNotifications;
import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDHomeActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing.LandingTileCard;
import timber.log.Timber;

//public class GeofenceBroadcastReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        reloadPrefs(context);
//        Timber.d("LAST_DAY: %s", UserConstants.LAST_DATE);
//
//        long diff = 0;
//        if (!UserConstants.LAST_DATE.equals("")) {
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date fd = new Date();
//                Date todayDate = sdf.parse(sdf.format(fd));
//                Date lastSentDate = sdf.parse(UserConstants.LAST_DATE);
//                long diffInMillies = Math.abs(lastSentDate.getTime() - todayDate.getTime());
//                diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (diff < 1 && !UserConstants.LAST_DATE.equals(""))
//            return;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        UserConstants.LAST_DATE = sdf.format(new Date());
//        save(context);
//
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        // User is not signed in
//        if (auth.getCurrentUser() == null)
//            return;
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("misc")
//                .document("otherItems")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
////                            UserConstants.GEOFENCE_NOTIFICATION_TITLE = document.getString("restartTitle");
////                            UserConstants.GEOFENCE_NOTIFICATION_BODY = document.getString("restartBody");
//
//                            Intent launchIntent = new Intent(context, RRCOVIDHomeActivity.class);
//
//                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                            // This method is called when the BroadcastReceiver is receiving  an Intent broadcast.
//                            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//                            // Create a Notification object
//                            NotificationCompat.Builder notificationBuilder = Objects.requireNonNull(
//                                    ManageNotifications.createNotification(
//                                            null,
//                                            context,
//                                            "geofencing",
//                                            R.drawable.ic_p_icon,
//                                            UserConstants.GEOFENCE_NOTIFICATION_TITLE,
//                                            UserConstants.GEOFENCE_NOTIFICATION_BODY,
//                                            NotificationCompat.PRIORITY_MAX
//                                    )
//                            )
//                                    .setColor(context.getResources().getColor(R.color.colorPrimary))
//                                    .setAutoCancel(true)
//                                    .setContentIntent(pendingIntent)
//                                    .setSound(defaultSoundUri);
//
//                            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
//
//                            if (geofencingEvent.hasError()) {
//                                Timber.e("onReceive: Error receiving geofence event...");
//                                return;
//                            }
//
//                            int transitionType = geofencingEvent.getGeofenceTransition();
//
//                            switch (transitionType) {
//                                case Geofence.GEOFENCE_TRANSITION_ENTER: {
//                                    Timber.d("GEOFENCE_TRANSITION_ENTER");
//                                    // Give the notification a unique ID based on the time
//                                    notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
//                                }
//                                break;
//                                case Geofence.GEOFENCE_TRANSITION_DWELL: {
//                                    Timber.d("GEOFENCE_TRANSITION_DWELL");
//                                    notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
//                                }
//                                break;
//                                case Geofence.GEOFENCE_TRANSITION_EXIT: {
//                                    Timber.d("GEOFENCE_TRANSITION_EXIT");
//                                    // Give the notification a unique ID based on the time
//                                    notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
//                                }
//                                break;
//                            }
//                        } else
//                            Timber.d("No such document");
//                    } else
//                        Timber.d("get failed with %s", task.getException().toString());
//                });
//    }
//
//    void reloadPrefs(Context context) {
//        try {
//            SharedPreferences tilesJSONAsString = context.getSharedPreferences("USER_CONSTANTS", Context.MODE_PRIVATE);
//            String userDataAsString = tilesJSONAsString.getString("data", null);
//            // There exists a `USER_CONSTANTS` preferences file.
//            if (userDataAsString != null) {
//                JSONObject userData = new JSONObject(userDataAsString);
//                UserConstants.RW_AUTHORIZED = userData.getBoolean("RWAuthorized");
//                UserConstants.FINE_LOCATION = userData.getBoolean("FINE_LOCATION");
//                try {
//                    UserConstants.LAST_DATE = userData.getString("LAST_DATE");
//                } catch (Exception ignored) {
//                    UserConstants.LAST_DATE = "";
//                }
//                UserConstants.BACKGROUND_LOCATION = userData.getBoolean("BACKGROUND_LOCATION");
//                UserConstants.INITIAL_REGISTRATION = userData.getBoolean("InitialLaunch");
//                UserConstants.LAST_NOTIFICATION_TIMESTAMP = userData.getLong("LastTimeStamp");
//
//                JSONArray subscriptionArray = userData.getJSONArray("subscriptions");
//
//                // Assign the subscriptions
//                for (int i = 0; i < subscriptionArray.length(); i++) {
//                    JSONObject currentSub = subscriptionArray.getJSONObject(i);
//                    while (currentSub.keys().hasNext()) {
//                        String title = currentSub.keys().next();
//                        switch (title.toUpperCase()) {
//                            case "MAPS_SUBSCRIPTION": {
//                                UserConstants.MAPS_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "NEWS_SUBSCRIPTION": {
//                                UserConstants.NEWS_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "EVENTS_SUBSCRIPTION": {
//                                UserConstants.EVENTS_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "E_ACCOUNTS_SUBSCRIPTION": {
//                                UserConstants.E_ACCOUNTS_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "RANGER_WELLNESS_SUBSCRIPTION": {
//                                UserConstants.RANGER_WELLNESS_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "COMPUTER_LAB_SUBSCRIPTION": {
//                                UserConstants.COMPUTER_LAB_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            case "NAVIGATE_SUBSCRIPTION": {
//                                UserConstants.NAVIGATE_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
////                            case "RANGER_RESTART_SUBSCRIPTION": {
////                                UserConstants.RANGER_RESTART_SUBSCRIPTION = currentSub.getBoolean(title);
////                            }
//                            case "MENU_SUBSCRIPTION": {
//                                UserConstants.MENU_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
//                            break;
//                            default:
//                                break;
//                        }
//                        break;
//                    }
//                }
//
//                // Assign the tile arrangement
//                JSONArray tiles = userData.getJSONArray("tiles");
//                for (int i = 0; i < tiles.length(); i++) {
//                    JSONObject tile = tiles.getJSONObject(i);
//                    String title = "";
//                    while (tile.keys().hasNext()) {
//                        title = tile.keys().next();
//                        int position = tile.getJSONArray(title).getJSONObject(0).getInt("position");
//                        int icon = tile.getJSONArray(title).getJSONObject(1).getInt("icon");
//                        UserConstants.LANDING_TILE_CARDS.add(new LandingTileCard(title, position, icon));
//                        break;
//                    }
//                }
//            }
//            // There does not exist a `USER_CONSTANTS` preferences file.
//            else {
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Maps",
//                                0,
//                                R.drawable.ic_maps_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Ranger Wellness",
//                                1,
//                                R.drawable.ic_ranger_wellness_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "News",
//                                2,
//                                R.drawable.ic_news_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Navigate",
//                                3,
//                                R.drawable.ic_navigate_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Events",
//                                4,
//                                R.drawable.ic_events_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "eAccounts",
//                                5,
//                                R.drawable.ic_eaccounts_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Computer Labs",
//                                6,
//                                R.drawable.ic_computer_labs_small
//                        )
//                );
//                UserConstants.LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Ranger Restart",
//                                7,
//                                R.drawable.ic_ranger_restart_small
//                        )
//                );
//                // Uncomment for the menu tile. All that's missing is to replace ic_ranger_restart_small with the correct one
////                UserConstants.LANDING_TILE_CARDS.add(
////                        new LandingTileCard(
////                                "Menu",
////                                8,
////                                R.drawable.ic_ranger_restart_small
////                        )
////                );
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Timber.e("Error loading USER_CONSTANTS file.");
//        }
//        Timber.d("Finished loading USER_CONSTANTS file.");
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void save(Context context) {
//        Timber.d("Applying changes to `USER_CONSTANTS` file.");
//        SharedPreferences sharedPreferences = context.getSharedPreferences(
//                "USER_CONSTANTS",
//                Context.MODE_PRIVATE
//        );
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        JSONObject userDataJson = new JSONObject();
//
//        //<editor-fold desc="JSONObjects of notification subscriptions">
//        JSONObject mapsSub = new JSONObject();
//        JSONObject newsSub = new JSONObject();
//        JSONObject eventsSub = new JSONObject();
//        JSONObject eAccountsSub = new JSONObject();
//        JSONObject rwSub = new JSONObject();
//        JSONObject clSub = new JSONObject();
//        JSONObject navSub = new JSONObject();
//        JSONObject rrSub = new JSONObject();
//        JSONObject menuSub = new JSONObject();
//        //</editor-fold>
//
//        try {
//            //<editor-fold desc="Add the subscription statuses to their JSONObjects">
//            mapsSub.put("MAPS_SUBSCRIPTION", UserConstants.MAPS_SUBSCRIPTION);
//            newsSub.put("NEWS_SUBSCRIPTION", UserConstants.NEWS_SUBSCRIPTION);
//            eventsSub.put("EVENTS_SUBSCRIPTION", UserConstants.EVENTS_SUBSCRIPTION);
//            eAccountsSub.put("E_ACCOUNTS_SUBSCRIPTION", UserConstants.E_ACCOUNTS_SUBSCRIPTION);
//            rwSub.put("RANGER_WELLNESS_SUBSCRIPTION", UserConstants.RANGER_WELLNESS_SUBSCRIPTION);
//            clSub.put("COMPUTER_LAB_SUBSCRIPTION", UserConstants.COMPUTER_LAB_SUBSCRIPTION);
//            navSub.put("NAVIGATE_SUBSCRIPTION", UserConstants.NAVIGATE_SUBSCRIPTION);
//            rrSub.put("RANGER_RESTART_SUBSCRIPTION", UserConstants.RANGER_RESTART_SUBSCRIPTION);
//            menuSub.put("MENU_SUBSCRIPTION", UserConstants.MENU_SUBSCRIPTION);
//            //</editor-fold>
//
//            //<editor-fold desc="Add the subscription jJSONObjects to the userDataJson">
//            userDataJson.accumulate("subscriptions", mapsSub);
//            userDataJson.accumulate("subscriptions", newsSub);
//            userDataJson.accumulate("subscriptions", eventsSub);
//            userDataJson.accumulate("subscriptions", eAccountsSub);
//            userDataJson.accumulate("subscriptions", rwSub);
//            userDataJson.accumulate("subscriptions", clSub);
//            userDataJson.accumulate("subscriptions", navSub);
//            userDataJson.accumulate("subscriptions", rrSub);
//            userDataJson.accumulate("subscriptions", menuSub);
//            //</editor-fold>
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Timber.e("Error applying changes to `USER_CONSTANTS` file.");
//        }
//
//        //<editor-fold desc="Save the lading tile arrangements in to a JSONObject">
//        for (LandingTileCard landingTileCard : UserConstants.LANDING_TILE_CARDS) {
//            try {
//                JSONObject tile = new JSONObject();
//                JSONObject position = new JSONObject();
//                JSONObject icon = new JSONObject();
//
//                position.put("position", landingTileCard.getPosition());
//                icon.put("icon", landingTileCard.getIconRes());
//
//                tile.accumulate(landingTileCard.getTileCardTextView(), position);
//                tile.accumulate(landingTileCard.getTileCardTextView(), icon);
//
//                userDataJson.accumulate("tiles", tile);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Timber.e("Error applying changes to `USER_CONSTANTS` file.");
//            }
//        }
//        //</editor-fold>
//
//        try {
//            userDataJson.accumulate("RWAuthorized", UserConstants.RW_AUTHORIZED);
//            userDataJson.accumulate("LAST_DATE", UserConstants.LAST_DATE);
//            userDataJson.accumulate("FINE_LOCATION", UserConstants.FINE_LOCATION);
//            userDataJson.accumulate("BACKGROUND_LOCATION", UserConstants.BACKGROUND_LOCATION);
//            userDataJson.accumulate("InitialLaunch", UserConstants.INITIAL_REGISTRATION);
//            userDataJson.accumulate("LastTimeStamp", UserConstants.LAST_NOTIFICATION_TIMESTAMP);
//            editor.putString(
//                    "data",
//                    userDataJson.toString()
//            );
//            editor.apply();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Timber.e("Error applying changes to `USER_CONSTANTS` file.");
//        }
//        Timber.d("Changes applied to `USER_CONSTANTS` file.");
//    }
//}
