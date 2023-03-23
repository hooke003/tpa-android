package appfactory.uwp.edu.parksideapp2.Radio;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import appfactory.uwp.edu.parksideapp2.R;

public class RadioFragment extends Fragment {
    ImageView playPause;
    ImageView stop;
    ProgressBar bufferingIndicator;
    RadioBroadcastReceiver radioBroadcastReceiver = new RadioBroadcastReceiver();

    public RadioFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        bufferingIndicator = view.findViewById(R.id.buffering_indicator);

        // Play Pause Image Button
        playPause = view.findViewById(R.id.new_play_pause);
        playPause.setOnClickListener(v -> {
            if (!isMyServiceRunning(ParksideRadioService.class)) {
                bufferingIndicator.setVisibility(View.VISIBLE);
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                Intent intent = new Intent(getActivity(), ParksideRadioService.class);
                intent.setAction(ParksideRadioService.ACTION_START_FOREGROUND_SERVICE);
                getActivity().startService(intent);
            }
            else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "You are not connected to an active network.", Toast.LENGTH_SHORT);
                toast.show();
                bufferingIndicator.setVisibility(View.GONE);
            }
        });
        // Stop Image Button
        stop = view.findViewById(R.id.new_stop);
        stop.setOnClickListener(v -> {
            if (isMyServiceRunning(ParksideRadioService.class)) {
                Intent intent = new Intent(getActivity(), ParksideRadioService.class);
                intent.setAction(ParksideRadioService.ACTION_STOP_FOREGROUND_SERVICE);
                getActivity().startService(intent);
            }
            bufferingIndicator.setVisibility(View.GONE);
        });

        registerBroadcastReceiver();

        return view;
    }

    /**
     * Checks to see if the Radio Service is currently active
     * @param serviceClass
     * @return true if it is, false if not
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(radioBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMyServiceRunning(ParksideRadioService.class)) {
            bufferingIndicator.setVisibility(View.GONE);
        }
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ParksideRadioService.ACTION_BUFFERING_COMPLETE);
        getActivity().registerReceiver(radioBroadcastReceiver, intentFilter);
    }

    private class RadioBroadcastReceiver extends BroadcastReceiver {
        public RadioBroadcastReceiver() {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            bufferingIndicator.setVisibility(View.GONE);
        }
    }
}