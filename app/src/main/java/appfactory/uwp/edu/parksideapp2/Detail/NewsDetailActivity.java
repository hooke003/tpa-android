package appfactory.uwp.edu.parksideapp2.Detail;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import appfactory.uwp.edu.parksideapp2.Models.NewsObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class NewsDetailActivity extends AppCompatActivity {
    private ViewPager newsViewPager;
    private NewsDetailSlideAdapter newsViewPagerAdapter;
    private Realm mRealm;
// UI

    /**
     * Init UI
     */
    private void initUI() {
        newsViewPager = findViewById(R.id.news_view_pager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initUI();

        // Get data from NewsAdapter
        ArrayList<String> url = Objects.requireNonNull(getIntent().getExtras()).getStringArrayList("url");
        //Timber.d(String.valueOf(url));
        int position = getIntent().getExtras().getInt("position");
        //getActionBar().setDisplayHomeAsUpEnabled(false);
        newsViewPagerAdapter = new NewsDetailSlideAdapter(this,url);
        newsViewPager.setAdapter(newsViewPagerAdapter);
        newsViewPager.setCurrentItem(position);
    }
}