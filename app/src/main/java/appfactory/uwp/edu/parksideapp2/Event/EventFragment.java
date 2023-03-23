package appfactory.uwp.edu.parksideapp2.Event;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.EventObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class EventFragment extends Fragment {
    private final String TAG = "EventFragment";
    // Fragment Interaction
    // Event List
    private ArrayList<EventObj> eventList = new ArrayList<>();
    private EventAdapter adapter;
    //Realm
    // In order to get data from Realm between date -> use Date Type, if only get data in single date ( use String type )
    private Realm mRealm;
    //Date picker
    private DatePickerDialog datePicker;
    // Calendar for View All
    private Calendar calendar;
    // Calendar object for datepicker, next button, previous button
    private Calendar secondCalendar;
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
    //UI
    RecyclerView recyclerView;
    private TextView dateTextView;
    private ImageButton backButton;
    private ImageButton nextButton;
    private TextView emptyMessage;


    private void initUI(View view) {
        Timber.d("Initializing User Interface");
        recyclerView = view.findViewById(R.id.event_recycle_view);
        dateTextView = view.findViewById(R.id.date_text_view);
        backButton = view.findViewById(R.id.back_button);
        nextButton = view.findViewById(R.id.next_button);
        emptyMessage = view.findViewById(R.id.empty_event_message);

    }

    /**
     * Empty Constructor
     */
    public EventFragment() {
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        // Calendar get instance
        calendar = Calendar.getInstance();
        secondCalendar = Calendar.getInstance();
        // Pull event object from RealmDatabase and store into arrayList
        mRealm = Realm.getDefaultInstance();

        datePicker = new DatePickerDialog(getContext(), R.style.DialogThemeOrange, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Show previous, next button
                backButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                // Set time
                secondCalendar.set(Calendar.YEAR, year);
                secondCalendar.set(Calendar.MONTH, month);
                secondCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                final Date date = secondCalendar.getTime();
                String dateFormat = format.format(date);
                // Set date label
                dateTextView.setText(dateFormat);
                // User server date format to get data from realm
                final String serverDateFormat = serverFormat.format(date);

                mRealm.executeTransaction(realm -> {
                    RealmResults result = realm.where(EventObj.class).equalTo("eventDateString", serverDateFormat).findAll();
                    eventList.clear();
                    eventList.addAll(realm.copyFromRealm(result));
                    if (eventList.size() == 0) {
                        emptyMessage.setVisibility(View.VISIBLE);
                    } else {
                        emptyMessage.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                });

            }
        }, secondCalendar.get(Calendar.YEAR), secondCalendar.get(Calendar.MONTH), secondCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        initUI(view);
        // Get current date in yyyy-mm-dd format
        final Date currentDate = new Date(calendar.getTimeInMillis());
        dateTextView.setText("All Events");
        // format date to YYYY-MM-dd
        final String currentDateString = serverFormat.format(currentDate);
        // Add 30days to calendar
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        final Date endDate = calendar.getTime();


        // Set up linearlayoutmanager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventAdapter(getContext(), eventList);

        // Get data from RealmDB base on current date on user device
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<EventObj> result = realm.where(EventObj.class).between("eventDate", currentDate, endDate).findAll();
                if(result.size() >= 20){
                    for(int i = 0; i < 20; i++){
                        eventList.add(result.get(i));
                    }
                }
                else{
                    eventList.addAll(result);
                }
                adapter.notifyDataSetChanged();
            }
        });

        // View all button in datePickerDialog
        datePicker.setButton(DialogInterface.BUTTON_NEUTRAL, "View All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get data from RealmDB base on current date on user device
                // Clear the list before insert a new set of data
                eventList.clear();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<EventObj> result = realm.where(EventObj.class).between("eventDate", currentDate, endDate).findAll();
                        if(result.size() >= 20){
                            for(int i = 0; i < 20; i++){
                                eventList.add(result.get(i));
                            }
                        }
                        else{
                            eventList.addAll(result);
                        }
                        adapter.notifyDataSetChanged();
                        // Set emptyMessage to Gone and set dateTextView to "View All Events"
                        dateTextView.setText("All Events");
                        emptyMessage.setVisibility(View.GONE);
                        //Hide back, next button
                        backButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                        // Set back to current date with new Calendar
                        Calendar tempCalendar = Calendar.getInstance();
                        datePicker.updateDate(tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.get(Calendar.DAY_OF_MONTH));
                    }
                });
            }
        });

        // Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondCalendar.add(Calendar.DAY_OF_MONTH, -1);
                final Date previousDate = secondCalendar.getTime();
                // Server date format as String
                final String previousDateString = serverFormat.format(previousDate);
                // Set textView
                dateTextView.setText(format.format(previousDate));

                // Execute Realm
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults result = realm.where(EventObj.class).equalTo("eventDateString", previousDateString).findAll();
                        // Refresh the list and update with new data
                        eventList.clear();
                        eventList.addAll(realm.copyFromRealm(result));
                        if (eventList.size() == 0) {
                            emptyMessage.setVisibility(View.VISIBLE);
                        } else {
                            emptyMessage.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                // set DateDialog to show current data
                datePicker.updateDate(secondCalendar.get(Calendar.YEAR), secondCalendar.get(Calendar.MONTH), secondCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });

        // Next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondCalendar.add(Calendar.DAY_OF_MONTH, 1);
                final Date nextDate = secondCalendar.getTime();
                // Server date format String
                final String nextDateString = serverFormat.format(nextDate);
                // Set textView
                dateTextView.setText(format.format(nextDate));

                // Execute Realm
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults result = realm.where(EventObj.class).equalTo("eventDateString", nextDateString).findAll();
                        // Refresh the list and update with new data
                        eventList.clear();
                        eventList.addAll(realm.copyFromRealm(result));
                        if (eventList.size() == 0) {
                            emptyMessage.setVisibility(View.VISIBLE);
                        } else {
                            emptyMessage.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                // set DateDialog to show current data
                datePicker.updateDate(secondCalendar.get(Calendar.YEAR), secondCalendar.get(Calendar.MONTH), secondCalendar.get(Calendar.DAY_OF_MONTH));
            }
        });
        // Set recyclerView, layoutManager and adapter
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.events_news_option_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.date_edit) {
            datePicker.show();
            return true;
        }
        return false;
    }
}
