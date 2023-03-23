package appfactory.uwp.edu.parksideapp2.tpa2.auth;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * The class initializes the activity for the log-in/restart/sign-up fragments.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
// THIS IS NOT USED ANYMORE
public class TPA2LoginActivity extends AppCompatActivity {
    private final String TAG = "TPA2LoginActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
        setContentView(R.layout.activity_sso_auth); //Og Params -> activity_tpa2_login_main
        // Do not reload the activity when the phone is rotated.
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(
//                            R.id.rr_login_main_fragment_container,
//                            new TPA2LoginFragment(),
//                            "TPA2LoginFragment"
//                    )
//                    .addToBackStack(null)
//                    .commit();
//        }
    }

    // Don't use the back button
    @Override
    public void onBackPressed() {

    }
}
