package appfactory.uwp.edu.parksideapp2.tpa2.auth;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.Notifications.ManageNotifications;
import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

/**
 * Initial set up for managing subscriptions. Only called once per app install.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2InitializeNotificationSubscriptionsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button saveSubscriptionsButton;

    //<editor-fold desc="Subscription Switches">
    private Switch mapsNotificationSwitch;
    private Switch newsNotificationSwitch;
    private Switch eventsNotificationSwitch;
    private Switch eAccountsNotificationSwitch;
    private Switch rwNotificationSwitch;
    private Switch compLabsNotificationSwitch;
    private Switch navigateNotificationSwitch;
//    private Switch rrNotificationSwitch;
    // TODO Uncomment menu switch tile
//    private Switch menuNotificationSwitch;
    //</editor-fold>

    private CardView savingProgressBarCardView;
    private ProgressBar savingProgressBar;

    private void initUI(@NotNull View view) {
        saveSubscriptionsButton = view.findViewById(R.id.saveSubscriptionsButton);

        //<editor-fold desc="Switch assignments">
        mapsNotificationSwitch = view.findViewById(R.id.initmapsNotificationSwitch);
        newsNotificationSwitch = view.findViewById(R.id.initnewsNotificationSwitch);
        eventsNotificationSwitch = view.findViewById(R.id.initeventsNotificationSwitch);
        eAccountsNotificationSwitch = view.findViewById(R.id.initeAccountsNotificationSwitch);
        rwNotificationSwitch = view.findViewById(R.id.initrwNotificationSwitch);
        compLabsNotificationSwitch = view.findViewById(R.id.initcompLabsNotificationSwitch);
        navigateNotificationSwitch = view.findViewById(R.id.initnavigateNotificationSwitch);
//        rrNotificationSwitch = view.findViewById(R.id.initrrNotificationSwitch);
        // TODO Uncomment menu switch tile
//        menuNotificationSwitch = view.findViewById(R.id.initMenuNotificationSwitch);
        //</editor-fold>

        savingProgressBarCardView = view.findViewById(R.id.savingProgressBarCardView);
        savingProgressBar = view.findViewById(R.id.savingProgressBar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_tpa2_initialize_notification_subscriptions,
                container,
                false
        );
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveSubscriptionsButton.setOnClickListener(this);

        //<editor-fold desc="Switch checked status assignments">
        mapsNotificationSwitch.setChecked(UserConstants.MAPS_SUBSCRIPTION);
        newsNotificationSwitch.setChecked(UserConstants.NEWS_SUBSCRIPTION);
        eventsNotificationSwitch.setChecked(UserConstants.EVENTS_SUBSCRIPTION);
        eAccountsNotificationSwitch.setChecked(UserConstants.E_ACCOUNTS_SUBSCRIPTION);
        rwNotificationSwitch.setChecked(UserConstants.RANGER_WELLNESS_SUBSCRIPTION);
        compLabsNotificationSwitch.setChecked(UserConstants.COMPUTER_LAB_SUBSCRIPTION);
        navigateNotificationSwitch.setChecked(UserConstants.NAVIGATE_SUBSCRIPTION);
//        rrNotificationSwitch.setChecked(UserConstants.RANGER_RESTART_SUBSCRIPTION);
        // TODO Uncomment menu switch tile
//        menuNotificationSwitch.setChecked(UserConstants.MENU_SUBSCRIPTION);
        //</editor-fold>

        //<editor-fold desc="Switch listener assignments">
        mapsNotificationSwitch.setOnCheckedChangeListener(this);
        newsNotificationSwitch.setOnCheckedChangeListener(this);
        eventsNotificationSwitch.setOnCheckedChangeListener(this);
        eAccountsNotificationSwitch.setOnCheckedChangeListener(this);
        rwNotificationSwitch.setOnCheckedChangeListener(this);
        compLabsNotificationSwitch.setOnCheckedChangeListener(this);
        navigateNotificationSwitch.setOnCheckedChangeListener(this);
//        rrNotificationSwitch.setOnCheckedChangeListener(this);
        // TODO Uncomment menu switch tile
//        menuNotificationSwitch.setOnCheckedChangeListener(this);
        //</editor-fold>
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.saveSubscriptionsButton: {
                // Save the switch statuses to preferences and Firebase
                new AsyncTask<Void, Integer, Void>() {

                    @Override
                    protected void onPreExecute() {
                        savingProgressBarCardView.setVisibility(View.VISIBLE);
                        savingProgressBar.setVisibility(View.VISIBLE);
                        saveSubscriptionsButton.setClickable(false);
                        mapsNotificationSwitch.setClickable(false);
                        newsNotificationSwitch.setClickable(false);
                        eventsNotificationSwitch.setClickable(false);
                        eAccountsNotificationSwitch.setClickable(false);
                        rwNotificationSwitch.setClickable(false);
                        compLabsNotificationSwitch.setClickable(false);
                        navigateNotificationSwitch.setClickable(false);
//                        rrNotificationSwitch.setClickable(false);
                        // TODO Uncomment menu switch tile
//                        menuNotificationSwitch.setClickable(false);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    protected Void doInBackground(Void... voids) {
                        HashMap<String, Object> topicsMap = new HashMap<>();
                        ArrayList<String> topics = new ArrayList<>();
                        NotificationManager notificationManager =
                                (NotificationManager) requireActivity()
                                        .getSystemService(Context.NOTIFICATION_SERVICE);

                        //<editor-fold desc="Create the notification channels based on the subscriptions set">
                        if (UserConstants.RANGER_WELLNESS_SUBSCRIPTION) {
                            NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                                    "wellness",
                                    "Ranger Wellness",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                            topics.add("wellness");
                        }
//                        if (UserConstants.RANGER_RESTART_SUBSCRIPTION) {
//                            NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
//                                    "restart",
//                                    "Ranger Restart",
//                                    NotificationManager.IMPORTANCE_HIGH
//                            );
//                            notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
//                            topics.add("restart");
//                        }
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
                        if (UserConstants.EMAIL.split("@")[0].equalsIgnoreCase("rangers.uwp.edu")) {
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

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference ref = db.document(String.format("users/%s", FirebaseAuth.getInstance().getCurrentUser().getUid()));

                        ref.update(topicsMap)
                                .addOnCompleteListener(task -> {
                                    Timber.d("Posted map: %s", topics.toString());
                                    savingProgressBarCardView.setVisibility(View.GONE);
                                    savingProgressBar.setVisibility(View.GONE);
                                    UserConstants.INITIAL_REGISTRATION = false;
                                    UserConstants.save(requireActivity());
                                    Intent intent = new Intent(requireActivity(), TPA2LandingActivity.class);
                                    saveSubscriptionsButton.setClickable(true);
                                    mapsNotificationSwitch.setClickable(true);
                                    newsNotificationSwitch.setClickable(true);
                                    eventsNotificationSwitch.setClickable(true);
                                    eAccountsNotificationSwitch.setClickable(true);
                                    rwNotificationSwitch.setClickable(true);
                                    compLabsNotificationSwitch.setClickable(true);
                                    navigateNotificationSwitch.setClickable(true);
//                                    rrNotificationSwitch.setClickable(true);
                                    // TODO Uncomment menu switch tile
//                        menuNotificationSwitch.setClickable(true);
                                    startActivity(intent);
                                });
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        savingProgressBar.setProgress(values[0]);
                    }
                }.execute();
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int i = compoundButton.getId();
        switch (i) {
            case R.id.initmapsNotificationSwitch: {
                UserConstants.MAPS_SUBSCRIPTION = b;
            }
            break;
            case R.id.initnewsNotificationSwitch: {
                UserConstants.NEWS_SUBSCRIPTION = b;
            }
            break;
            case R.id.initeventsNotificationSwitch: {
                UserConstants.EVENTS_SUBSCRIPTION = b;
            }
            break;
            case R.id.initeAccountsNotificationSwitch: {
                UserConstants.E_ACCOUNTS_SUBSCRIPTION = b;
            }
            break;
            case R.id.initrwNotificationSwitch: {
                UserConstants.RANGER_WELLNESS_SUBSCRIPTION = b;
            }
            break;
            case R.id.initcompLabsNotificationSwitch: {
                UserConstants.COMPUTER_LAB_SUBSCRIPTION = b;
            }
            break;
            case R.id.initnavigateNotificationSwitch: {
                UserConstants.NAVIGATE_SUBSCRIPTION = b;
            }
//            break;
//            case R.id.initrrNotificationSwitch: {
//                UserConstants.RANGER_RESTART_SUBSCRIPTION = b;
//            }
            break;
            // TODO Uncomment menu switch tile
//            case R.id.initMenuNotificationSwitch: {
//                UserConstants.MENU_SUBSCRIPTION = b;
//            }
//            break;
            default:
                break;
        }
    }
}
