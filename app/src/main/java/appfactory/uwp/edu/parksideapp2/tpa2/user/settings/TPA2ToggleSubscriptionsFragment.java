package appfactory.uwp.edu.parksideapp2.tpa2.user.settings;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import appfactory.uwp.edu.parksideapp2.Notifications.ManageNotifications;
import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2NotificationCard;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LAST_NOTIFICATION_TIMESTAMP;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.NOTIFICATION_CARDS;

/**
 * Logic for managing subscriptions in the user settings.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2ToggleSubscriptionsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private ConstraintLayout notificationsBackButton;

    private Switch mapsNotificationSwitch;
    private Switch newsNotificationSwitch;
    private Switch eventsNotificationSwitch;
    private Switch eAccountsNotificationSwitch;
    private Switch rwNotificationSwitch;
    private Switch compLabsNotificationSwitch;
    private Switch navigateNotificationSwitch;
    private Switch rrNotificationSwitch;
//    TODO private Switch menuNotificationSwitch;

    private CardView savingProgressBarCardView;
    private ProgressBar savingProgressBar;

    private void initUI(@NotNull View view) {
        notificationsBackButton = view.findViewById(R.id.notificationsSettingsBackButton);
        mapsNotificationSwitch = view.findViewById(R.id.mapsNotificationSwitch);
        newsNotificationSwitch = view.findViewById(R.id.newsNotificationSwitch);
        eventsNotificationSwitch = view.findViewById(R.id.eventsNotificationSwitch);
        eAccountsNotificationSwitch = view.findViewById(R.id.eAccountsNotificationSwitch);
        rwNotificationSwitch = view.findViewById(R.id.rwNotificationSwitch);
        compLabsNotificationSwitch = view.findViewById(R.id.compLabsNotificationSwitch);
        navigateNotificationSwitch = view.findViewById(R.id.navigateNotificationSwitch);
//        rrNotificationSwitch = view.findViewById(R.id.rrNotificationSwitch);
        // TODO Uncomment this and the last CardView in `fragment_tpa2_enable_notifications`
//        menuNotificationSwitch = view.findViewById(R.id.menuNotificationSwitch);

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
                R.layout.fragment_tpa2_enable_notifications,
                container,
                false
        );
        initUI(view);
        return view;
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapsNotificationSwitch.setChecked(UserConstants.MAPS_SUBSCRIPTION);
        newsNotificationSwitch.setChecked(UserConstants.NEWS_SUBSCRIPTION);
        eventsNotificationSwitch.setChecked(UserConstants.EVENTS_SUBSCRIPTION);
        eAccountsNotificationSwitch.setChecked(UserConstants.E_ACCOUNTS_SUBSCRIPTION);
        rwNotificationSwitch.setChecked(UserConstants.RANGER_WELLNESS_SUBSCRIPTION);
        compLabsNotificationSwitch.setChecked(UserConstants.COMPUTER_LAB_SUBSCRIPTION);
        navigateNotificationSwitch.setChecked(UserConstants.NAVIGATE_SUBSCRIPTION);
//        rrNotificationSwitch.setChecked(UserConstants.RANGER_RESTART_SUBSCRIPTION);
        // TODO Uncomment this and the last CardView in `fragment_tpa2_enable_notifications`
//        menuNotificationSwitch.setChecked(UserConstants.MENU_SUBSCRIPTION);

        mapsNotificationSwitch.setOnCheckedChangeListener(this);
        newsNotificationSwitch.setOnCheckedChangeListener(this);
        eventsNotificationSwitch.setOnCheckedChangeListener(this);
        eAccountsNotificationSwitch.setOnCheckedChangeListener(this);
        rwNotificationSwitch.setOnCheckedChangeListener(this);
        compLabsNotificationSwitch.setOnCheckedChangeListener(this);
        navigateNotificationSwitch.setOnCheckedChangeListener(this);
//        rrNotificationSwitch.setOnCheckedChangeListener(this);
//        menuNotificationSwitch.setOnCheckedChangeListener(this);

        notificationsBackButton.setOnClickListener(click -> {
            new AsyncTask<Void, Integer, Void>() {

                protected void onPreExecute() {
                    savingProgressBarCardView.setVisibility(View.VISIBLE);
                    savingProgressBar.setVisibility(View.VISIBLE);
                    notificationsBackButton = view.findViewById(R.id.notificationsSettingsBackButton);
                    mapsNotificationSwitch.setFocusable(false);
                    newsNotificationSwitch.setFocusable(false);
                    eventsNotificationSwitch.setFocusable(false);
                    eAccountsNotificationSwitch.setFocusable(false);
                    rwNotificationSwitch.setFocusable(false);
                    compLabsNotificationSwitch.setFocusable(false);
                    navigateNotificationSwitch.setFocusable(false);
//                    rrNotificationSwitch.setFocusable(false);
                    // TODO Uncomment this and the last CardView in `fragment_tpa2_enable_notifications`
//                    menuNotificationSwitch.setFocusable(false);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected Void doInBackground(Void... voids) {
                    HashMap<String, Object> topicsMap = new HashMap<>();
                    ArrayList<String> topics = new ArrayList<>();
                    NotificationManager notificationManager =
                            (NotificationManager) requireActivity()
                                    .getSystemService(Context.NOTIFICATION_SERVICE);

                    //<editor-fold desc="Topic and notification channel(s) creation">
                    if (UserConstants.RANGER_WELLNESS_SUBSCRIPTION) {
                        NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
                                "wellness",
                                "Ranger Wellness",
                                NotificationManager.IMPORTANCE_HIGH
                        );
                        notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
                        topics.add("wellness");
                    }
//                    if (UserConstants.RANGER_RESTART_SUBSCRIPTION) {
//                        NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
//                                "restart",
//                                "Ranger Restart",
//                                NotificationManager.IMPORTANCE_HIGH
//                        );
//                        notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
//                        topics.add("restart");
//                    }
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
                    if (UserConstants.EMAIL.contains("rangers.uwp.edu")) {
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
                                if (task.isSuccessful()) {
                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put("email", UserConstants.EMAIL);
                                    data.put("limit", 15);
                                    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                                    mFunctions
                                            .getHttpsCallable("gettopicnotifications")
                                            .call(data)
                                            .continueWith(gettopicnotificationsTask -> {
                                                HttpsCallableResult result = gettopicnotificationsTask.getResult();
                                                return result.getData();
                                            })
                                            .addOnCompleteListener(gettopicnotificationsTask -> {
                                                if (gettopicnotificationsTask.isSuccessful()) {
                                                    // ["notification": {"topic": String, "title": String, "body": String, "timestamp": "YY-MM-DD HH:MM:SS"}, ...]
                                                    ArrayList<HashMap<String, Object>> notificationsListMap = (ArrayList<HashMap<String, Object>>) gettopicnotificationsTask.getResult();
                                                    NOTIFICATION_CARDS = new ArrayList<>();

                                                    for (HashMap<String, Object> notification : notificationsListMap) {
                                                        HashMap<String, String> attrs = (HashMap<String, String>) notification.get("notification");
                                                        String topic = Objects.requireNonNull(attrs).get("topic");
                                                        Timber.d(topic);
                                                        String title = attrs.get("title");
                                                        String body = attrs.get("body");
                                                        int icon = R.drawable.ic_pside_small;

                                                        if (topic != null) {
                                                            //<editor-fold desc="Assign the incoming notification items from Firebase the appropriate icon">
                                                            switch (topic) {
                                                                case "wellness":
                                                                    icon = R.drawable.ic_ranger_wellness_small;
                                                                    break;
                                                                case "restart":
                                                                    icon = R.drawable.ic_ranger_restart_small;
                                                                    break;
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
                                                                // TODO Uncomment and change `ic_computer_labs_small` to the appropriate menu icon
//                                                                case "menu":
//                                                                    icon = R.drawable.ic_computer_labs_small;
//                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                            //</editor-fold>
                                                        }

                                                        String timestamp = timeParse(attrs.get("timestamp"));
                                                        String dateSent = Objects.requireNonNull(attrs.get("timestamp")).replace(" CDT", "").trim().replace("am", "").replace("pm", "").replace("-", "/");
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                        long longTime = 0;
                                                        try {
                                                            longTime = simpleDateFormat.parse(dateSent).getTime();
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        NOTIFICATION_CARDS.add(new TPA2NotificationCard(title, body, timestamp, icon, longTime));
                                                    }

                                                    // Sort the notifications based on their age
                                                    Collections.sort(NOTIFICATION_CARDS);

                                                    // Reverse the sort to get newest first
                                                    Collections.reverse(NOTIFICATION_CARDS);

                                                    // Assign a new last read notification
                                                    if (NOTIFICATION_CARDS.size() > 0) {
                                                        LAST_NOTIFICATION_TIMESTAMP = NOTIFICATION_CARDS.get(0).getLongTime();
                                                    }

                                                    // Save the settings.
                                                    UserConstants.save(requireActivity());

                                                    savingProgressBarCardView.setVisibility(View.GONE);
                                                    savingProgressBar.setVisibility(View.GONE);

                                                    mapsNotificationSwitch.setFocusable(true);
                                                    newsNotificationSwitch.setFocusable(true);
                                                    eventsNotificationSwitch.setFocusable(true);
                                                    eAccountsNotificationSwitch.setFocusable(true);
                                                    rwNotificationSwitch.setFocusable(true);
                                                    compLabsNotificationSwitch.setFocusable(true);
                                                    navigateNotificationSwitch.setFocusable(true);
//                                                    rrNotificationSwitch.setFocusable(true);
                                                    // TODO Uncomment this and the last CardView in `fragment_tpa2_enable_notifications`
//                                                    menuNotificationSwitch.setFocusable(true);

                                                    try {
                                                        Thread.sleep(1500);
                                                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                                        fragmentTransaction.replace(
                                                                R.id.tpa2_landing_fragment_container,
                                                                new TPA2SettingsFragment(),
                                                                "TPA2SettingsFragment");
                                                        fragmentTransaction.addToBackStack(null);
                                                        fragmentTransaction.commit();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            });
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    savingProgressBar.setProgress(values[0]);
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
            }.execute();
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int i = compoundButton.getId();
        switch (i) {
            case R.id.mapsNotificationSwitch: {
                UserConstants.MAPS_SUBSCRIPTION = b;
            }
            break;
            case R.id.newsNotificationSwitch: {
                UserConstants.NEWS_SUBSCRIPTION = b;
            }
            break;
            case R.id.eventsNotificationSwitch: {
                UserConstants.EVENTS_SUBSCRIPTION = b;
            }
            break;
            case R.id.eAccountsNotificationSwitch: {
                UserConstants.E_ACCOUNTS_SUBSCRIPTION = b;
            }
            break;
            case R.id.rwNotificationSwitch: {
                UserConstants.RANGER_WELLNESS_SUBSCRIPTION = b;
            }
            break;
            case R.id.compLabsNotificationSwitch: {
                UserConstants.COMPUTER_LAB_SUBSCRIPTION = b;
            }
            break;
            case R.id.navigateNotificationSwitch: {
                UserConstants.NAVIGATE_SUBSCRIPTION = b;
            }
            break;
//            case R.id.rrNotificationSwitch: {
//                UserConstants.RANGER_RESTART_SUBSCRIPTION = b;
//            }
//            break;
            default:
                break;
        }
    }
}
