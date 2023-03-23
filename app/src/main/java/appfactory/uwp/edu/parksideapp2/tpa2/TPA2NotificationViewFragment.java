package appfactory.uwp.edu.parksideapp2.tpa2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
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

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

import static appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData.notificationCards;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LAST_NOTIFICATION_TIMESTAMP;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.NOTIFICATION_CARDS;

/**
 * This class initializes the notifications view and contains.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2NotificationViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView notificationRecyclerView;
    private RecyclerView.Adapter notificationRecyclerViewAdapter;
    private RecyclerView.LayoutManager notificationRecyclerViewLayoutManager;
    private SwipeRefreshLayout swipeNotificationRecyclerView;

    private void initUI(@NotNull View view) {
        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        swipeNotificationRecyclerView = view.findViewById(R.id.swipeNotificationRecyclerView);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.document(String.format("users/%s", FirebaseAuth.getInstance().getCurrentUser().getUid()));
        HashMap<String, Object> badgeValue = new HashMap<>();
        badgeValue.put("badge", 0);
        ref.update(badgeValue).addOnSuccessListener(aVoid -> Timber.d("Badge value updated"));
        // what is getting updated?? why call save() here
        UserConstants.save(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tpa2_notification_view, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notificationRecyclerViewLayoutManager = new LinearLayoutManager(requireContext());
        notificationRecyclerViewAdapter = new TPA2NotificationCardAdapter(NOTIFICATION_CARDS);
        notificationRecyclerView.setLayoutManager(notificationRecyclerViewLayoutManager);
        notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
        swipeNotificationRecyclerView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    // Fetch all the when the RecyclerView is pulled down.
    @SuppressLint("StaticFieldLeak")
    private void loadRecyclerViewData() {
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                swipeNotificationRecyclerView.setRefreshing(true);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("email", UserConstants.EMAIL);
                data.put("limit", 15);
                FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
                mFunctions
                        .getHttpsCallable("gettopicnotifications")
                        .call(data)
                        .continueWith(task -> {
                            HttpsCallableResult result = task.getResult();
                            return result.getData();
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // ["notification": {"topic": String, "title": String, "body": String, "timestamp": "YY-MM-DD HH:MM:SS"}, ...]
                                ArrayList<HashMap<String, Object>> notificationsListMap = (ArrayList<HashMap<String, Object>>) task.getResult();
                                NOTIFICATION_CARDS = new ArrayList<>();

                                for (HashMap<String, Object> notification : notificationsListMap) {
                                    HashMap<String, String> attrs = (HashMap<String, String>) notification.get("notification");
                                    String topic = Objects.requireNonNull(attrs).get("topic");
                                    Timber.d(topic);
                                    String title = attrs.get("title");
                                    String body = attrs.get("body");
                                    int icon = R.drawable.ic_pside_small;

                                    if (topic != null) {
                                        switch (topic) {
                                            case "wellness":
                                                icon = R.drawable.ic_ranger_wellness_small;
                                                break;
//                                            case "restart":
//                                                icon = R.drawable.ic_ranger_restart_small;
//                                                break;
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
                                            // Uncomment and assign the appropriate icon for the menu
//                                            case "menu":
//                                                icon = R.drawable.ic_computer_labs_small;
//                                                break;
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
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //notificationCards.add(NotificationCard(title, body, timestamp, icon, longTime))
                                    NOTIFICATION_CARDS.add(new TPA2NotificationCard(title, body, timestamp, icon, longTime));
                                }

                                Collections.sort(NOTIFICATION_CARDS);
                                Collections.reverse(NOTIFICATION_CARDS);
                                if (NOTIFICATION_CARDS.size() > 0) {
                                    LAST_NOTIFICATION_TIMESTAMP = NOTIFICATION_CARDS.get(0).getLongTime();
                                }
                                UserConstants.save(requireActivity());
                                swipeNotificationRecyclerView.setRefreshing(false);
                                notificationRecyclerViewLayoutManager = new LinearLayoutManager(requireContext());
                                notificationRecyclerViewAdapter = new TPA2NotificationCardAdapter(NOTIFICATION_CARDS);
                                notificationRecyclerView.setLayoutManager(notificationRecyclerViewLayoutManager);
                                notificationRecyclerView.setAdapter(notificationRecyclerViewAdapter);
                            }
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
        }.execute();
    }
}
