package appfactory.uwp.edu.parksideapp2.tpa2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.SSOAuth.SSOLoginScreenFragment;
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData;
import appfactory.uwp.edu.parksideapp2.tpa2.auth.TPA2LoginActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import appfactory.uwp.edu.parksideapp2.tpa2.user.settings.TPA2SettingsFragment;
import appfactory.uwp.edu.parksideapp2.utils.CheckNetStatus;
import timber.log.Timber;

import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LAST_NOTIFICATION_TIMESTAMP;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LOGGED_IN;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.NOTIFICATION_CARDS;

/**
 * This class initializes the landing page and contains the logic for the back button and toolbar.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2LandingActivity extends AppCompatActivity {
    private Menu menu;
    private Context mContext;
    private CardView landingProgressBarCardView;
    private ProgressBar landingProgressBar;
    private Activity mActivity;

    // suppress lint is used for async task something
    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate: called");
        setContentView(R.layout.activity_tpa2_landing);
        mActivity = this;
        mContext = this;

        // not familiar with this
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();


        // viewbinding stuff
        landingProgressBarCardView = findViewById(R.id.landingProgressBarCardView);
        landingProgressBar = findViewById(R.id.landingProgressBar);

        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        functions.getHttpsCallable("getcategories")
                .call()
                .addOnSuccessListener(httpsCallableResult -> UserConstants.COVID_DRAWER_DATA = (ArrayList<HashMap<String, String>>) httpsCallableResult.getData())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Timber.d("`getcategories` executed successfully");
                    else
                        Timber.e("`getcategories` did not execute successfully");
                });

        Toolbar tpa2Toolbar = findViewById(R.id.tpa2Toolbar);
        setSupportActionBar(tpa2Toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tpa2Toolbar.setTitle("");
        tpa2Toolbar.setSubtitle("");

        // on a background thread, what does logged in imply? That its not guest login
        if (LOGGED_IN) {
            // POST this user's FCM token
            new AsyncTask<Void, Integer, Void>() {

                //onPreExecute:
                // loading UI is a progress bar showing progress on loading
                @Override
                protected void onPreExecute() {
                    loadingUI();
                }

                //doInBackground:
                // the code to actually be executed in the background thread
                @Override
                protected Void doInBackground(Void... voids) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(task -> {
                                // Get the users email
                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                // Get new Instance ID token
                                String email = auth.getCurrentUser().getEmail();
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                String token = Objects.requireNonNull(task.getResult()).getToken();
                                userData.put("fcmToken", token);
                                // Get this user's FCM token

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .update(userData)
                                        .addOnSuccessListener(documentReference -> {

                                            FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                                            HashMap<String, Object> data = new HashMap<>();
                                            // add userConstants UID
                                            data.put("email", UserConstants.EMAIL);
                                            data.put("limit", 15);
                                            mFunctions
                                                    .getHttpsCallable("gettopicnotifications")
                                                    .call(data)
                                                    .continueWith(gettopicnotificationsTask -> {
                                                        HttpsCallableResult result = gettopicnotificationsTask.getResult();
                                                        return result.getData();
                                                    })
                                                    .addOnCompleteListener(gettopicnotificationsTask -> {
                                                        if (task.isSuccessful()) {
                                                            // ["notification": {"topic": String, "title": String, "body": String, "timestamp": "YY-MM-DD HH:MM:SS"}, ...]
                                                            ArrayList<HashMap<String, Object>> notificationsListMap = (ArrayList<HashMap<String, Object>>) gettopicnotificationsTask.getResult();

                                                            for (HashMap<String, Object> notification : notificationsListMap) {
                                                                HashMap<String, String> attrs = (HashMap<String, String>) notification.get("notification");
                                                                String topic = Objects.requireNonNull(attrs).get("topic");
                                                                String title = attrs.get("title");
                                                                String body = attrs.get("body");
                                                                int icon = R.drawable.ic_pside_small;

                                                                if (topic != null) {
                                                                    switch (topic) {
                                                                        case "wellness":
                                                                            icon = R.drawable.ic_ranger_wellness_small;
                                                                            break;
//                                                                        case "restart":
//                                                                            icon = R.drawable.ic_ranger_restart_small;
//                                                                            break;
                                                                        case "events":
                                                                            icon = R.drawable.ic_events_small;
                                                                            break;
                                                                        case "navigate":
                                                                            icon = R.drawable.ic_navigate_small;
                                                                            break;
                                                                        case "eaccounts":
                                                                            icon = R.drawable.ic_eaccounts_small;
                                                                            break;
                                                                        case "news":
                                                                            icon = R.drawable.ic_news_small;
                                                                            break;
                                                                        case "map":
                                                                            icon = R.drawable.ic_maps_small;
                                                                            break;
                                                                        case "computer":
                                                                            icon = R.drawable.ic_computer_labs_small;
                                                                            break;
                                                                        // TODO Uncomment this and assign the appropriate icon for the menu
//                                                                    case "menu":
//                                                                        icon = R.drawable.ic_computer_labs_small;
//                                                                        break;
                                                                        default:
                                                                            break;
                                                                    }
                                                                }

                                                                String timestamp = timeParse(attrs.get("timestamp"));
                                                                String dateSent = Objects.requireNonNull(attrs.get("timestamp")).replace(" CDT", "").trim().replace("am", "").replace("pm", "").replace("-", "/");
                                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                long longTime = 0;
                                                                try {
                                                                    longTime = simpleDateFormat.parse(dateSent).getTime();
                                                                } catch (ParseException ex) {
                                                                    ex.printStackTrace();
                                                                }
                                                                //
                                                                NOTIFICATION_CARDS.add(new TPA2NotificationCard(title, body, timestamp, icon, longTime));
                                                            }
                                                            Collections.sort(NOTIFICATION_CARDS);
                                                            Collections.reverse(NOTIFICATION_CARDS);

                                                            if (NOTIFICATION_CARDS.size() > 0) {
                                                                if (LAST_NOTIFICATION_TIMESTAMP == NOTIFICATION_CARDS.get(0).getLongTime()) {
                                                                    menu.getItem(0).setIcon(
                                                                            ContextCompat.getDrawable(
                                                                                    mContext,
                                                                                    R.drawable.ic_bell_inactive
                                                                            )
                                                                    );
                                                                } else {
                                                                    menu.getItem(0).setIcon(
                                                                            ContextCompat.getDrawable(
                                                                                    mContext,
                                                                                    R.drawable.ic_bell_new_item
                                                                            )
                                                                    );
                                                                }
                                                                LAST_NOTIFICATION_TIMESTAMP = NOTIFICATION_CARDS.get(0).getLongTime();
                                                            }
                                                        }
                                                        // OG params -> savedInstanceState == null
                                                        if (savedInstanceState == null) {
                                                            getSupportFragmentManager()
                                                                    .beginTransaction()
                                                                    .replace(
                                                                            R.id.tpa2_landing_fragment_container,
                                                                            new TPA2LandingFragment(),
                                                                            "TPA2LandingFragment"
                                                                    )
                                                                    .commit();
                                                        }
                                                        revertUI();
                                                    });
                                        });
                            });
                    return null;
                }

                private String timeParse(String timestamp) {
                    try {
                        String dateSent = timestamp.replace(" CDT", "").trim().replace("am", "").replace("pm", "").replace("-", "/");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String currentDateTime = simpleDateFormat.format(new Date());
                        Date date1 = simpleDateFormat.parse(currentDateTime);
                        Date date2 = simpleDateFormat.parse(dateSent);
                        String timeDelta = "";
                        if (TimeUnit.SECONDS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS) < 59) {
                            timeDelta = String.format("%ss", TimeUnit.SECONDS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS));
                        } else if (TimeUnit.MINUTES.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS) < 59) {
                            timeDelta = String.format("%sm", TimeUnit.MINUTES.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS));
                        } else if (TimeUnit.HOURS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS) < 23) {
                            timeDelta = String.format("%sh", TimeUnit.HOURS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS));
                        } else if (TimeUnit.DAYS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS) < 30) {
                            timeDelta = String.format("%sd", TimeUnit.DAYS.convert(Math.abs(date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS));
                        } else {
                            timeDelta = dateSent.split(" ")[0];
                        }
                        return timeDelta;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return timestamp;
                    }
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    landingProgressBar.setProgress(values[0]);
                }
            }.execute();
        // TPA2LandingFragment only gets called if the user is not logged in
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.tpa2_landing_fragment_container,
                                new TPA2LandingFragment(),
                                "TPA2LandingFragment"
                        )
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int i = item.getItemId();
        switch (i) {
            // Display the notifications
            case R.id.notificationBellButton: {
                if (LOGGED_IN) {
                    if (!CheckNetStatus.isConnected(this)) {
                        showPopUpAlert("Error!", "Could not connect to the Internet.");
                        break;
                    }
                    if (!getSupportFragmentManager().getFragments().get(0).getTag().equalsIgnoreCase("TPA2NotificationViewFragment")) {
                        item.setIcon(
                                ContextCompat.getDrawable(
                                        mContext,
                                        R.drawable.ic_bell_inactive
                                )
                        );
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.tpa2_landing_fragment_container,
                                        new TPA2NotificationViewFragment(),
                                        "TPA2NotificationViewFragment"
                                )
                                .addToBackStack(null)
                                .commit();
                    }
                } else
                    showPopUpAlert("User not logged in", "You must be logged in to use this feature.");
            }
            break;
            // Navigate to the update profile screen
            case R.id.settingsButton: {
                if (LOGGED_IN) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.tpa2_landing_fragment_container,
                                    new TPA2SettingsFragment(),
                                    "TPA2SettingsFragment"
                            )
                            .addToBackStack(null)
                            .commit();
                } else
                    showPopUpAlert("User not logged in.", "You must be logged in to use this feature.");
            }
            break;
            // Show the log out dialog
            case R.id.logoutButton: {
                if (LOGGED_IN) {
                    showLogOutDialog("Sign out", "Are you sure you want to sign out?");
                } else showLogOutDialog("Exit", "Are you sure you want to exit?");
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.tpa2_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Logout dialog
        // Only show the log out dialog when the TPA2LandingFragment is the current fragment
        try {
            switch (Objects.requireNonNull(
                    getSupportFragmentManager().getFragments().get(0).getTag())) {
                case "TPA2LandingFragment": {
                    Timber.d("onBackPress: %s", getSupportFragmentManager().getFragments().get(0).getTag());
                    if (LOGGED_IN) {
                        showLogOutDialog("Log out", "Would you like to log out?");
                    } else showLogOutDialog("Exit", "Are you sure you want to exit?");
                }
                break;
                case "RWDashboardFragment": {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.tpa2_landing_fragment_container,
                                    new TPA2LandingFragment(),
                                    "TPA2LandingFragment"
                            )
                            .commit();
                }
                break;
                case "TPA2NotificationViewFragment": {
                    menu.getItem(0).setIcon(
                            ContextCompat.getDrawable(
                                    mContext,
                                    R.drawable.ic_bell_inactive
                            )
                    );
                    super.onBackPressed();
                }
                break;
                default:
                    Timber.d("onBackPress: %s", getSupportFragmentManager().getFragments().get(0).getTag());
                    super.onBackPressed();
                    break;

            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.tpa2_landing_fragment_container,
                            new TPA2LandingFragment(),
                            "TPA2LandingFragment"
                    )
                    .commit();
            super.onBackPressed();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void showLogOutDialog(String title, String body) {
        // Create the prompt to ask the user if they would like to log out
        AlertDialog.Builder logOutDialog = new AlertDialog.Builder(this);
        logOutDialog
                .setTitle(Html.fromHtml(String.format("<font color='#000000'>%s</font>", title)))
                .setMessage(Html.fromHtml(String.format("<font color='#000000'>%s</font>", body)))
                .setIcon(R.drawable.ic_bigbear);
        if (LOGGED_IN)
            logOutDialog
                    .setPositiveButton("Sign out", (dialog, which) -> {
                        // Get this user's FCM token
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(task -> {
                                    new AsyncTask<Void, Integer, Void>() {

                                        @Override
                                        protected void onPreExecute() {
                                            loadingUI();
                                        }

                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            LOGGED_IN = false;
                                            // Get the users email
                                            FirebaseAuth auth = FirebaseAuth.getInstance();

                                            // Get new Instance ID token
                                            String email = auth.getCurrentUser().getEmail();
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("email", email);
                                            userData.put("fcmToken", "");

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            // Add a new document with a generated ID
                                            db.collection("users")
                                                    .document(FirebaseAuth.getInstance().getUid())
                                                    .update(userData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(mActivity, SSOLoginScreenFragment.class);
                                                        startActivity(intent);
                                                    });
                                            FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                                            return null;
                                        }
                                    }.execute();
                                });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    });
        else
            logOutDialog
                    .setPositiveButton("Exit", (dialog, which) -> {
                        Intent intent = new Intent(this, SSOLoginScreenFragment.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    });
        logOutDialog.create().show();
    }

    private void showPopUpAlert(String title, String body) {
        AlertDialog.Builder notLoggedInPopUp = new AlertDialog.Builder(this);
        notLoggedInPopUp
                .setTitle(Html.fromHtml(String.format("<font color='#000000'>%s</font>", title)))
                .setMessage(Html.fromHtml(String.format("<font color='#000000'>%s</font>", body)))
                .setPositiveButton("Login", (dialogInterface, i) -> {
                    Intent intent = new Intent(this, SSOLoginScreenFragment.class);
                    startActivity(intent);
                })
                .setNeutralButton("Ok", (dialog, which) -> {
                })
                .setIcon(R.drawable.ic_bigbear);
        notLoggedInPopUp.create().show();
    }

    private void revertUI() {
        landingProgressBar.setVisibility(View.GONE);
        landingProgressBarCardView.setVisibility(View.GONE);
    }

    private void loadingUI() {
        landingProgressBar.setVisibility(View.VISIBLE);
        landingProgressBarCardView.setVisibility(View.VISIBLE);
    }

    public void blank(View view) {
        Log.d("TPA2LandingActivity", "Title IX pressed");
    }
}
