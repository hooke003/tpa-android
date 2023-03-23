package appfactory.uwp.edu.parksideapp2.ranger_wellness;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments.RWDashboardFragment;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingFragment;
import timber.log.Timber;

public class RWDashboardActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rw_landing);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.rw_landing_fragment_container,
                            new RWDashboardFragment(),
                            "RWDashboardFragment"
                    )
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            switch (Objects.requireNonNull(
                    getSupportFragmentManager().getFragments().get(0).getTag())) {
                case "RWDashboardFragment": {
                    Intent intent = new Intent(this, TPA2LandingActivity.class);
                    startActivity(intent);
                }
                default:
                    Timber.d("onBackPress: %s", getSupportFragmentManager().getFragments().get(0).getTag());
                    super.onBackPressed();
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.rw_landing_fragment_container,
                            new RWDashboardFragment(),
                            "RWDashboardFragment"
                    )
                    .commit();
            super.onBackPressed();
        }
    }
}
