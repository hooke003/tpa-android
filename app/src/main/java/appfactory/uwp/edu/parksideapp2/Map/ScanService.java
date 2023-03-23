package appfactory.uwp.edu.parksideapp2.Map;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;

import timber.log.Timber;

public class ScanService extends Service {
    // Log TAG
    private final String TAG = "ScanService";
    //Wifi/ Bluetooth Manager
    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    // Wifi and Bluetooth result
    private JSONObject wifiScanResult;
    private JSONObject bluetoothScanResult;
    private boolean isScanning = false;
    // Body variable
    private JSONObject jsonBody = null;
    private String family = "";
    private String device = "";
    private String location = "";


    @Override
    public void onCreate() {
        super.onCreate();
        // Initializing wifi manager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Register Broadcast Receiver
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        doScan();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Service Destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Start Scanning Wifi
     */
    private void doScan(){
        Timber.d("Start Scanning");
        synchronized (this){
            if(isScanning){
                return;
            }
            else{
                isScanning = true;
            }
        }

        // Init
        wifiScanResult = new JSONObject();
        bluetoothScanResult = new JSONObject();

        if(wifiManager.startScan()){
            Timber.d("Start wifi scan");
        }else{
            Timber.d("Unable to start wifi scan");
        }
    }

    /**
     * Wifi Broadcast Receiver
     */
    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                List<ScanResult> scanResult = wifiManager.getScanResults();
                for (int i = 0; i < scanResult.size(); i++) {
                    String bssi = scanResult.get(i).BSSID.toLowerCase();
                    int level = scanResult.get(i).level;
                    Timber.d("Wifi: " + bssi + " " + level);
                    try {
                        wifiScanResult.put(bssi,level);
                    } catch (JSONException e) {
                        Timber.d(e.toString());
                    }
                }
            }
        }
    };

    /**
     * Bluetooth Broadcast Receiver
     */
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d("bluetoothReceiver's working");
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int level = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String address = device.getAddress().toLowerCase();
                Timber.d("Bluetooth: " + address + ":" + level);
                try {
                    bluetoothScanResult.put(address,level);
                } catch (JSONException e) {
                    Timber.d(e.toString());
                }
                synchronized (this){
                    isScanning = false;
                }
            }
        }
    };
}
