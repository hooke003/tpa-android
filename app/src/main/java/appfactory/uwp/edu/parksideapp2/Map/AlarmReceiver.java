package appfactory.uwp.edu.parksideapp2.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kyluong09 on 8/2/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "MyAlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceiving");
        // Create scan service intent and start service
        Intent scanServiceIntent = new Intent(context,ScanService.class);
    }
}
