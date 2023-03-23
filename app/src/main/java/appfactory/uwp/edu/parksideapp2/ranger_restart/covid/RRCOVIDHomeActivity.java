package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;

/**
 * This class initializes the home screen.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "RRCOVIDMainActivity";
    NavigationView navigationView;
    private DrawerLayout rr_covid_main_fragment_container;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
        setContentView(R.layout.activity_rr_covid_main);

        // Sets the toolbar and properties
        Toolbar toolbar = findViewById(R.id.rr_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        rr_covid_main_fragment_container = findViewById(R.id.rr_covid_main_fragment_container);
        navigationView = findViewById(R.id.rr_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home_menu_item);

        // Sets the NavigationDrawer layout
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                rr_covid_main_fragment_container,
                toolbar,
                R.string.rr_open_navigation,
                R.string.rr_close_drawer
        );

        // Disable "hamburger to arrow" drawable
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(
                v -> rr_covid_main_fragment_container.openDrawer(GravityCompat.START)
        );
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_hamburger);
        actionBarDrawerToggle.syncState();

        rr_covid_main_fragment_container.addDrawerListener(actionBarDrawerToggle);
        // Do not reload the fragment when the phone is rotated.
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            // Close the NavigationDrawer if open
            if (rr_covid_main_fragment_container.isDrawerOpen(GravityCompat.START))
                rr_covid_main_fragment_container.closeDrawer(GravityCompat.START);
                // Otherwise if the current fragment is the COVIDHome fragment, return to the landing page
            else if (getSupportFragmentManager().getFragments().get(0).getTag().equals("RRCOVIDHomeFragment")) {
                Intent intent = new Intent(this, TPA2LandingActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RRCOVIDDrawerItem rrcovidDrawerItem;
        Bundle args = new Bundle();
        // Check what the item in the DrawerLayout has been pressed
        switch (item.getItemId()) {
            case R.id.home_menu_item:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.housing_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Housing");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.dining_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Dining");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.bookstore_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Bookstore");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.course_delivery_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Course Delivery");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.labs_research_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Classrooms, Labs, and Studios");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.community_events_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "CLUBS, ORGS, AND ACTIVITIES");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.library_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Library and Study Spaces");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.techbar_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "TechBar: Computer and Hotspot Checkout");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.athletics_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Recreation, Intramurals, Sports and Activity Center");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
//            case R.id.facilities_management_menu_item:
//                rrcovidDrawerItem = new RRCOVIDDrawerItem();
//                args.putString("title", "Facilities Management");
//                rrcovidDrawerItem.setArguments(args);
//                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case R.id.faculty_office_hours_menu_item:
//                rrcovidDrawerItem = new RRCOVIDDrawerItem();
//                args.putString("title", "Faculty Office Hours");
//                rrcovidDrawerItem.setArguments(args);
//                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
//            case R.id.internships_menu_item:
//                rrcovidDrawerItem = new RRCOVIDDrawerItem();
//                args.putString("title", "Internships (OFF CAMPUS)");
//                rrcovidDrawerItem.setArguments(args);
//                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
            case R.id.stu_health_menu_item:
                rrcovidDrawerItem = new RRCOVIDDrawerItem();
                args.putString("title", "Student Health, Counseling, and Disability");
                rrcovidDrawerItem.setArguments(args);
                fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidDrawerItem, "RRCOVIDDrawerItem");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
                rr_covid_main_fragment_container.closeDrawer(GravityCompat.START);
                break;
        }
        rr_covid_main_fragment_container.closeDrawer(GravityCompat.START);
        return true;
    }
}
