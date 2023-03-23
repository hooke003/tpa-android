package appfactory.uwp.edu.parksideapp2.SSOAuth;
import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.api.Context;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import appfactory.uwp.edu.parksideapp2.BuildConfig;
import timber.log.Timber;

/**
 * This class is the Javascript Interface. What's a Javascript interface? It is something used so that that java code written in
 * this class can interact with the javascript code of the webview
 */
public class SSOWebAppInterface extends Activity {

    SSOLoginScreenFragment mContext;

    // When does postMessage run? when is it called? It says its never used but it might run within javascript code?? irdk
    @JavascriptInterface
    public void postMessage(String message) throws JSONException {

        // This is just a test for the Token
        Timber.d(message);
        Timber.d("Got a message");

        JSONObject obj = new JSONObject(message);

        String credential = obj.getString("credential");
        Boolean isNewUser = obj.getBoolean("isNewUser");



        mContext.login(credential, isNewUser);
    }

    SSOWebAppInterface(SSOLoginScreenFragment c) {
        mContext = c;
    }
}

