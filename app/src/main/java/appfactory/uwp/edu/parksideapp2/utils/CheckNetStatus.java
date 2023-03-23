package appfactory.uwp.edu.parksideapp2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class CheckNetStatus {
    private final static String TAG = "CheckNetStatus";
    /**
     * @param context Fragment or Activity
     * @return true if enabled and connected otherwise false
     */
    public static boolean isConnected(@NotNull Context context) {
        Log.d(TAG, "Checking network status...");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo != null && networkInfo.isConnected());
    }
}
