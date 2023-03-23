package appfactory.uwp.edu.parksideapp2.News;

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
import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;


public class NewsFragment extends Fragment {
    // Log Tag
    private final String TAG = "NewsFragment";
    //RealmDatabase
    private Realm mRealm;
    // Time and Calendar
    private DatePickerDialog datePicker;
    private Calendar calendar;
    // Date Format
    private SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
    // Server Date Format
    private SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd");
    // UI
    private RecyclerView recyclerView;
    //private ImageView newsImage;

    private TextView dateTextView;
    private ImageButton backButton;
    private ImageButton nextButton;
    private TextView emptyMessage;
    //News list
    private ArrayList<NewsObj> newsList = new ArrayList<>();
    private NewsAdapter adapter =new NewsAdapter(newsList, getContext());

    private void initUI(View view) {
        Timber.d("Initializing User Interface");
        recyclerView = view.findViewById(R.id.news_recycle_view);
        dateTextView = view.findViewById(R.id.date_text_view);
        backButton = view.findViewById(R.id.back_button);
        nextButton = view.findViewById(R.id.next_button);
        emptyMessage = view.findViewById(R.id.empty_event_message);
    }

    public NewsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.date_edit) {
            datePicker.show();
            return true;
        }
        return false;
    }

    // not being called
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_news_option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Calendar get instance
        calendar = Calendar.getInstance();
        // Pull event object from RealmDatabase and store into arrayList
        mRealm = Realm.getDefaultInstance();
        // Set up date picker and listener
        datePicker = new DatePickerDialog(getContext(), R.style.DialogThemeBlue, (view, year, month, dayOfMonth) -> {
            // Show previous, next button
            backButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            // Set calendar
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date date = calendar.getTime();
            String dateTemp = format.format(date);
            final String serverDateTemp = serverFormat.format(date);
            // Set date text view
            dateTextView.setText(dateTemp);
            mRealm.executeTransaction(realm -> {
                RealmResults result = realm.where(NewsObj.class).equalTo("pubDate", serverDateTemp).findAll();
                newsList.clear();
                newsList.addAll(realm.copyFromRealm(result));
                if(newsList.size() == 0){
                    emptyMessage.setVisibility(View.VISIBLE);
                }
                else {
                    emptyMessage.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            });
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mRealm.executeTransactionAsync(realm -> {
            RealmResults result = realm.where(NewsObj.class).findAll();
            newsList.addAll(realm.copyFromRealm(result));
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initUI(view);

        // Get current date in yyyy-mm-dd format
        Date currentDate = calendar.getTime();
        final String currentDateString = format.format(currentDate);
        final String serverCurrentDateString = serverFormat.format(currentDate);
        dateTextView.setText("All News");

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.news_recycle_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newsList, getContext());

        // Execute Realm to get current date data.
        mRealm.executeTransaction(realm -> {
            RealmResults result = realm.where(NewsObj.class).findAll();
            newsList.addAll(realm.copyFromRealm(result));
            adapter.notifyDataSetChanged();
        });

        // View all button in datePickerDialog
        datePicker.setButton(DialogInterface.BUTTON_NEUTRAL, "View All", (dialog, which) -> {
            // Execute Realm to get current date data.
            mRealm.executeTransaction(realm -> {
                RealmResults result = realm.where(NewsObj.class).findAll();
                // Clear the list before insert a new set of data
                newsList.clear();
                newsList.addAll(realm.copyFromRealm(result));
                adapter.notifyDataSetChanged();
                // Sert textviewer Gone and set TextView title to "View All"
                emptyMessage.setVisibility(View.GONE);
                dateTextView.setText("All News");
                //Hide back, next button
                backButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                // Set back to current date with new Calendar
                Calendar tempCalendar = Calendar.getInstance();
                datePicker.updateDate(tempCalendar.get(Calendar.YEAR),tempCalendar.get(Calendar.MONTH),tempCalendar.get(Calendar.DAY_OF_MONTH));
            });
        });
        //Back Button
        backButton.setOnClickListener(v -> {
            // Set calendar time and textView
            calendar.add(Calendar.DAY_OF_MONTH, -1);

            Date date = calendar.getTime();
            // Default format as String
            String dateStringTemp = format.format(date);
            // Server format as String
            final String serverDateStringTemp = serverFormat.format(date);
            // Set textView
            dateTextView.setText(dateStringTemp);

            // Execute Realm
            mRealm.executeTransaction(realm -> {
                RealmResults result = realm.where(NewsObj.class).equalTo("pubDate", serverDateStringTemp).findAll();
                // Refresh the list and update with new data
                newsList.clear();
                newsList.addAll(realm.copyFromRealm(result));
                if(newsList.size() == 0){
                    emptyMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    emptyMessage.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            });

            // set DateDialog to show current data
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        });

        // Next Button
        nextButton.setOnClickListener(v -> {
            // Set calendar time and textView
            calendar.add(Calendar.DAY_OF_MONTH, +1);

            Date date = calendar.getTime();
            String dateStringTemp = format.format(date);
            final String serverDateStringTemp = serverFormat.format(date);
            dateTextView.setText(dateStringTemp);

            // Execute Realm
            mRealm.executeTransaction(realm -> {
                RealmResults result = realm.where(NewsObj.class).equalTo("pubDate", serverDateStringTemp).findAll();
                // Refresh the list and update with new data
                newsList.clear();
                newsList.addAll(realm.copyFromRealm(result));
                if(newsList.size() == 0){
                    emptyMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    emptyMessage.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            });

            // set DateDialog to show current data
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
}
