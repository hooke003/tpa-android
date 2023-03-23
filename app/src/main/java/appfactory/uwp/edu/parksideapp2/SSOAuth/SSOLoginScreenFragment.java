package appfactory.uwp.edu.parksideapp2.SSOAuth;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.auth.TPA2InitializeNotificationSubscriptionsFragment;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;


/**
 * @author Christopher Mata
 * @version 1.0
 * @since 10.10.2021
 * Landing Activity when pressing the notification for News or landing tile
 * URL for the webview: https://uwp-app-saml.web.app/
 */

public class SSOLoginScreenFragment extends AppCompatActivity {
    WebView webView;
    public Button loginBtn;
    public String Url = "https://uwp-app-saml.web.app/";
    public TextView guestLogin;
    public TextView welcomeText;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        // why did they call this here? I am assuming to either automatically log a user in after they have already
        // done the login process
        //TODO figure out what happens when there is no current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curUser = mAuth.getCurrentUser();

        // set the layout to look how it was designed in the activity_sso_auth.xml file
        setContentView(R.layout.activity_sso_auth);

        // doing view binding (linking the xml attributes (aka components (aka buttons, etc)) so that this file
        // can use them.
        loginBtn = findViewById(R.id.button2);
        webView = findViewById(R.id.loginSSO);
        guestLogin = findViewById(R.id.guestLogIn);
        welcomeText = findViewById(R.id.welcomeRangerTextView);

        // Loads all constants for this page
        // DO NOT DELETE
        //UserConstants.load(this);

        // when the guest login button is clicked
        guestLogin.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Opening App", Toast.LENGTH_SHORT).show();
            // update a static user variable (logged in)
            UserConstants.LOGGED_IN = false;
            startActivity(new Intent(this, TPA2LandingActivity.class));

            // TODO: examine this as it may be creating bugs
            webView.setVisibility(View.GONE);

        });

        // when the login button is clicked
        loginBtn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Moving to Web", Toast.LENGTH_SHORT).show();
            // call toggleVisibility() which displays parkside's login webview
            toggleVisibility();
        });

        // Im assuming this block of code executes after the user logs in using the webview
        if (curUser != null) {
            UserConstants.INITIAL_REGISTRATION = false;
            Timber.d("Email: %s", curUser.getEmail());
            // updateUI for current user
            updateUI(curUser);
        }
    }

    private void toggleVisibility() {
        //TODO how this is implemented might be creating bugs, but also may be correct

        // if the webview is not visible
        if (webView.getVisibility() != View.VISIBLE) {
            // set webview to visible
            webView.setVisibility(View.VISIBLE);
            // other ui components to be invisible (aka gone)
            loginBtn.setVisibility(View.GONE);
            welcomeText.setVisibility(View.GONE);
            guestLogin.setVisibility(View.GONE);

            // this block of code sets up the webview
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            // uses SSOWebAppInterface allows for java code to interact with javascript code
            webView.addJavascriptInterface(new SSOWebAppInterface(this), "userLogin");
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(Url);
        }
    }
    // TODO find out if the login function is actually used
    // is this ever used? if javascript interface
    public void login(String token, Boolean newUser) {
        UserConstants.INITIAL_REGISTRATION = newUser;

        mAuth.signInWithCustomToken(token)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCustomToken:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.getException(), "signInWithCustomToken:failure");
                        Toast.makeText(SSOLoginScreenFragment.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account) {
        UserConstants.LOGGED_IN = true;

        // clears all webviews
        webView.removeAllViews();
        webView.clearHistory();
        webView.clearHistory();
        webView.clearCache(true);
        webView.destroy();
        webView.setVisibility(View.GONE);
        webView = null;

        if (account != null) {
            UserConstants.EMAIL = account.getEmail();
            UserConstants.DISPLAY_NAME = account.getDisplayName();

            //TODO is initial registration ever not false?

            // if initial registration is true
            if (UserConstants.INITIAL_REGISTRATION) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        // what is a fragment transaction?, in this case, fragment transaction, replaces a current
                        // fragment view with another fragment view.
                        fragmentTransaction.replace(
                                R.id.rr_login_main_fragment_container,
                                new TPA2InitializeNotificationSubscriptionsFragment(),
                                "TPA2InitializeNotificationSubscriptionsFragment")
                                .addToBackStack(null)
                                .commit();
                        // send user to TPA2LandingActivity
                    } else {
                        startActivity(new Intent(this, TPA2LandingActivity.class));
                    }

            }
        }
}
