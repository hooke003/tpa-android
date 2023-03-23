package appfactory.uwp.edu.parksideapp2.tpa2.user;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.Notifications.ManageNotifications;
import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.RecentUpdateCard;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2NotificationCard;
import appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing.LandingTileCard;
import timber.log.Timber;

/**
 * This class keep track of all the user data and can save and load them.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class UserConstants {
    private final static String TAG = "UserConstants";
    public static ArrayList<HashMap<String, String>> COVID_DRAWER_DATA = new ArrayList<>();
    public static String EMAIL = "";
    public static String DISPLAY_NAME = "";
//    public static String GEOFENCE_NOTIFICATION_TITLE = "";
//    public static String GEOFENCE_NOTIFICATION_BODY = "";
    public static String TODAY_DATE = "";
    public static String LAST_DATE = "";
    public static ArrayList<LandingTileCard> LANDING_TILE_CARDS = new ArrayList<>();
    public static ArrayList<TPA2NotificationCard> NOTIFICATION_CARDS = new ArrayList<>();
    public static ArrayList<RecentUpdateCard> RECENT_UPDATE_CARDS = new ArrayList<>();
    public static long LAST_NOTIFICATION_TIMESTAMP = 0;
    public static boolean INITIAL_REGISTRATION = true;
    public static boolean FINE_LOCATION = false;
    public static boolean BACKGROUND_LOCATION = false;
    public static boolean LOGGED_IN = false;
    public static boolean RW_AUTHORIZED = false;
    public static boolean MAPS_SUBSCRIPTION = false;
    public static boolean NEWS_SUBSCRIPTION = false;
    public static boolean EVENTS_SUBSCRIPTION = false;
    public static boolean E_ACCOUNTS_SUBSCRIPTION = false;
    public static boolean RANGER_WELLNESS_SUBSCRIPTION = false;
    public static boolean COMPUTER_LAB_SUBSCRIPTION = false;
    public static boolean NAVIGATE_SUBSCRIPTION = false;
//    public static boolean RANGER_RESTART_SUBSCRIPTION = false;
    public static boolean MENU_SUBSCRIPTION = false;

    /**
     * This function saves all the static variables to a `USER_CONSTANTS` preferences file.
     *
     * @param context current Activity that this function is being called in.
     */
    public static void save(Context context) {
        Log.d("^^^^^^", String.valueOf(LANDING_TILE_CARDS));
        new AsyncTask<Void, Integer, Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Void doInBackground(Void... voids) {
                HashMap<String, Object> topicsMap = new HashMap<>();
                ArrayList<String> topics = new ArrayList<>();
                NotificationManager notificationManager =
                        (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);

                //<editor-fold desc="Saves subscription and notification channels to Firebase">
                if (UserConstants.RANGER_WELLNESS_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "wellness",
                            "Ranger Wellness",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("wellness");
                }
//                if (UserConstants.RANGER_RESTART_SUBSCRIPTION) {
//                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
//                            "restart",
//                            "Ranger Restart",
//                            NotificationManager.IMPORTANCE_HIGH
//                    );
//                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
//                    topics.add("restart");
//                }
                if (UserConstants.EVENTS_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "events",
                            "Events",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("events");
                }
                if (UserConstants.NAVIGATE_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "navigate",
                            "Navigate",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("navigate");
                }
                if (UserConstants.E_ACCOUNTS_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "eaccounts",
                            "eAccounts",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("eaccounts");
                }
                if (UserConstants.NEWS_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "news",
                            "News",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("news");
                }
                if (UserConstants.MAPS_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "map",
                            "Map",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("map");
                }
                if (UserConstants.COMPUTER_LAB_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "computer",
                            "Computer Labs",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("computer");
                }
                if (UserConstants.MENU_SUBSCRIPTION) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "menu",
                            "Menu",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("menu");
                }
                if (UserConstants.EMAIL.split("@")[1].equalsIgnoreCase("rangers.uwp.edu")) {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "student",
                            "Student",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("student");
                } else {
                    NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                            "staff",
                            "Staff",
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                    topics.add("staff");
                }
                //</editor-fold>

                topicsMap.put("topics", topics);
                FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                mFunctions
                        .getHttpsCallable("setsubscriptions")
                        .call(topicsMap)
                        .continueWith(task -> {
                            HttpsCallableResult result = task.getResult();
                            return result.getData();
                        })
                        .addOnCompleteListener(task -> {
                            UserConstants.INITIAL_REGISTRATION = false;
                        });
                return null;
            }
        }.execute();

        Log.d(TAG, "Applying changes to `USER_CONSTANTS` file.");
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "USER_CONSTANTS",
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONObject userDataJson = new JSONObject();

        //<editor-fold desc="JSONObjects of notification subscriptions">
        JSONObject mapsSub = new JSONObject();
        JSONObject newsSub = new JSONObject();
        JSONObject eventsSub = new JSONObject();
        JSONObject eAccountsSub = new JSONObject();
        JSONObject rwSub = new JSONObject();
        JSONObject clSub = new JSONObject();
        JSONObject navSub = new JSONObject();
//        JSONObject rrSub = new JSONObject();
        JSONObject menuSub = new JSONObject();
        //</editor-fold>

        try {
            //<editor-fold desc="Add the subscription statuses to their JSONObjects">
            mapsSub.put("MAPS_SUBSCRIPTION", MAPS_SUBSCRIPTION);
            newsSub.put("NEWS_SUBSCRIPTION", NEWS_SUBSCRIPTION);
            eventsSub.put("EVENTS_SUBSCRIPTION", EVENTS_SUBSCRIPTION);
            eAccountsSub.put("E_ACCOUNTS_SUBSCRIPTION", E_ACCOUNTS_SUBSCRIPTION);
            rwSub.put("RANGER_WELLNESS_SUBSCRIPTION", RANGER_WELLNESS_SUBSCRIPTION);
            clSub.put("COMPUTER_LAB_SUBSCRIPTION", COMPUTER_LAB_SUBSCRIPTION);
            navSub.put("NAVIGATE_SUBSCRIPTION", NAVIGATE_SUBSCRIPTION);
//            rrSub.put("RANGER_RESTART_SUBSCRIPTION", RANGER_RESTART_SUBSCRIPTION);
            menuSub.put("MENU_SUBSCRIPTION", MENU_SUBSCRIPTION);
            //</editor-fold>

            //<editor-fold desc="Add the subscription jJSONObjects to the userDataJson">
            userDataJson.accumulate("subscriptions", mapsSub);
            userDataJson.accumulate("subscriptions", newsSub);
            userDataJson.accumulate("subscriptions", eventsSub);
            userDataJson.accumulate("subscriptions", eAccountsSub);
            userDataJson.accumulate("subscriptions", rwSub);
            userDataJson.accumulate("subscriptions", clSub);
            userDataJson.accumulate("subscriptions", navSub);
//            userDataJson.accumulate("subscriptions", rrSub);
            userDataJson.accumulate("subscriptions", menuSub);
            //</editor-fold>
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error applying changes to `USER_CONSTANTS` file.");
        }

        //<editor-fold desc="Save the lading tile arrangements in to a JSONObject">
        for (LandingTileCard landingTileCard : LANDING_TILE_CARDS) {
            try {
                JSONObject tile = new JSONObject();
                JSONObject position = new JSONObject();
                JSONObject icon = new JSONObject();

                position.put("position", landingTileCard.getPosition());
                icon.put("icon", landingTileCard.getIconRes());

                tile.accumulate(landingTileCard.getTileCardTextView(), position);
                tile.accumulate(landingTileCard.getTileCardTextView(), icon);

                userDataJson.accumulate("tiles", tile);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Error applying changes to `USER_CONSTANTS` file.");
            }
        }
        //</editor-fold>

        try {
            userDataJson.accumulate("RWAuthorized", RW_AUTHORIZED);
            userDataJson.accumulate("LAST_DATE", TODAY_DATE);
            userDataJson.accumulate("FINE_LOCATION", FINE_LOCATION);
            userDataJson.accumulate("BACKGROUND_LOCATION", BACKGROUND_LOCATION);
            userDataJson.accumulate("InitialLaunch", INITIAL_REGISTRATION);
            userDataJson.accumulate("LastTimeStamp", LAST_NOTIFICATION_TIMESTAMP);
            editor.putString(
                    "data",
                    userDataJson.toString()
            );
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error applying changes to `USER_CONSTANTS` file.");
        }
        Log.d(TAG, "Changes applied to `USER_CONSTANTS` file.");
    }

    /**
     * This function loads all values from the `USER_CONSTANTS` preferences file.
     *
     * @param activity current Activity that this function is being called in.
     */
    public static void load(Activity activity) {
        Log.d(TAG, "Loading the USER_CONSTANTS file.");

        // why is there a try catch here? what exception will be throwed? ANSWER if this code, does not go through, that
        // means there was some sort of error in loading the user constants file.
        try {

            // im pretty sure the purpose of these two lines are to see if the user preferences are empty or not
            SharedPreferences tilesJSONAsString = activity.getSharedPreferences("USER_CONSTANTS", Context.MODE_PRIVATE);
            String userDataAsString = tilesJSONAsString.getString("data", null);
            // There exists a `USER_CONSTANTS` preferences file.
            // if there is data to grab from the user preferences file, then grab it
            if (userDataAsString != null) {
                JSONObject userData = new JSONObject(userDataAsString);
                RW_AUTHORIZED = userData.getBoolean("RWAuthorized");
                FINE_LOCATION = userData.getBoolean("FINE_LOCATION");

                // why is there a try catch here? LAST_DATE might be null, or might not exist?
                try {
                    LAST_DATE = userData.getString("LAST_DATE");
                } catch (Exception ignored) {
                    LAST_DATE = "";
                }
                // what is Timber. ? from researching, it is a form of logging
                Timber.e(String.valueOf(FINE_LOCATION));
                BACKGROUND_LOCATION = userData.getBoolean("BACKGROUND_LOCATION");
                Timber.e(String.valueOf(BACKGROUND_LOCATION));
                INITIAL_REGISTRATION = userData.getBoolean("InitialLaunch");
                LAST_NOTIFICATION_TIMESTAMP = userData.getLong("LastTimeStamp");

                JSONArray subscriptionArray = userData.getJSONArray("subscriptions");

                // Assign the subscriptions values
                for (int i = 0; i < subscriptionArray.length(); i++) {
                    JSONObject currentSub = subscriptionArray.getJSONObject(i);
                    while (currentSub.keys().hasNext()) {
                        String title = currentSub.keys().next();
                        switch (title.toUpperCase()) {
                            case "MAPS_SUBSCRIPTION": {
                                MAPS_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "NEWS_SUBSCRIPTION": {
                                NEWS_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "EVENTS_SUBSCRIPTION": {
                                EVENTS_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "E_ACCOUNTS_SUBSCRIPTION": {
                                E_ACCOUNTS_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "RANGER_WELLNESS_SUBSCRIPTION": {
                                RANGER_WELLNESS_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "COMPUTER_LAB_SUBSCRIPTION": {
                                COMPUTER_LAB_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            case "NAVIGATE_SUBSCRIPTION": {
                                NAVIGATE_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
//                            case "RANGER_RESTART_SUBSCRIPTION": {
//                                RANGER_RESTART_SUBSCRIPTION = currentSub.getBoolean(title);
//                            }
                            case "MENU_SUBSCRIPTION": {
                                MENU_SUBSCRIPTION = currentSub.getBoolean(title);
                            }
                            break;
                            default:
                                break;
                        }
                        break;
                    }
                }

                // Assign the tile arrangement values
                JSONArray tiles = userData.getJSONArray("tiles");
                for (int i = 0; i < tiles.length(); i++) {
                    JSONObject tile = tiles.getJSONObject(i);
                    String title = "";
                    while (tile.keys().hasNext()) {
                        title = tile.keys().next();
                        int position = tile.getJSONArray(title).getJSONObject(0).getInt("position");
                        int icon = tile.getJSONArray(title).getJSONObject(1).getInt("icon");
                        LANDING_TILE_CARDS.add(new LandingTileCard(title, position, icon));
                        break;
                    }
                }
            }
            // There does not exist a `USER_CONSTANTS` preferences file. then load the default tile postitions
            else {
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Maps",
                                0,
                                R.drawable.ic_maps_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Ranger Wellness",
                                1,
                                R.drawable.ic_ranger_wellness_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "News",
                                2,
                                R.drawable.ic_news_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Navigate",
                                3,
                                R.drawable.ic_navigate_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Events",
                                4,
                                R.drawable.ic_events_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "eAccounts",
                                5,
                                R.drawable.ic_eaccounts_small
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Computer Labs",
                                6,
                                R.drawable.ic_computer_labs_small
                        )
                );
//                LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Ranger Restart",
//                                7,
//                                R.drawable.ic_ranger_restart_small
//                        )
//                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Title IX",
                                8,
                                R.mipmap.ic_titleix
                        )
                );
                LANDING_TILE_CARDS.add(
                        new LandingTileCard(
                                "Indoor Navigation",
                                9,
                                R.drawable.ic_indoornav_small
                        )
                );
//                LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Ranger Restart",
//                                7,
//                                R.drawable.ic_ranger_restart_small
//                        )
//                );
                // Uncomment for the menu tile. All that's missing is to replace ic_ranger_restart_small with the correct one
//                LANDING_TILE_CARDS.add(
//                        new LandingTileCard(
//                                "Menu",
//                                8,
//                                R.drawable.ic_ranger_restart_small
//                        )
//                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading USER_CONSTANTS file.");
        }
        Log.d(TAG, "Finished loading USER_CONSTANTS file.");
    }
}
