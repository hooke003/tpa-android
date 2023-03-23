package appfactory.uwp.edu.parksideapp2.Detail;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import appfactory.uwp.edu.parksideapp2.Models.EventObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class EventDetailActivity extends AppCompatActivity {
    // UI
    private ViewPager viewPager;
    private EventDetailSlideAdapter viewPagerAdapter;
    private ArrayList<EventObj> eventList = new ArrayList<>();
    private Realm mRealm;
    // Keep track of index of page
    int count = 0;


    /**
     * Init UI
     */
    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.event_detail_viewpager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setContentView(R.layout.activity_event_detail);
        //Init
        initUI();
        //Receive data - get selected event's id from user
        String _id = getIntent().getExtras().getString("event_id");
        // Get Realm instance
        mRealm = Realm.getDefaultInstance();
        // Set up adapter
        viewPagerAdapter = new EventDetailSlideAdapter(this, eventList);
        // Get eventList from Realm
        mRealm.executeTransaction(realm -> {
            RealmResults result = realm.where(EventObj.class).findAll();
            eventList.addAll(result);
            viewPagerAdapter.notifyDataSetChanged();
        });
        // Set viewPager adapter
        viewPager.setAdapter(viewPagerAdapter);
        // Switch to user's selected event page
        for(EventObj event: eventList){
            if(event.get_id().equals(_id)){
                viewPager.setCurrentItem(count);
                }
            count++;
        }
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }
}
