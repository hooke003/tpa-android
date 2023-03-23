package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import timber.log.Timber;

/**
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 * Landing Activity when pressing the notification for Lab or landing tile
 */
public class LabActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_landing);
        // Do not reload fragment on orientation change
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.lab_landing_fragment_container,
                            new LabFragment(),
                            "LabFragment"
                    )
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            switch (Objects.requireNonNull(
                    getSupportFragmentManager().getFragments().get(0).getTag())) {
                // If base fragment is attached, then go back to the TPA2LandingActivity
                case "LabFragment": {
                    Intent intent = new Intent(this, TPA2LandingActivity.class);
                    startActivity(intent);
                }
                // Otherwise just go back
                default:
                    Timber.d("onBackPress: %s", getSupportFragmentManager().getFragments().get(0).getTag());
                    super.onBackPressed();
                    break;
            }
        } catch (NullPointerException e) {
            // Reload LabFragment if there was an issue
            e.printStackTrace();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.lab_landing_fragment_container,
                            new LabFragment(),
                            "LabFragment"
                    )
                    .commit();
            super.onBackPressed();
        }
    }
}