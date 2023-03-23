package appfactory.uwp.edu.parksideapp2.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import appfactory.uwp.edu.parksideapp2.R;


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    //Firebase Analystic
    private final String TAG = "HomeFragment";
    private final int LOCATION_REQUEST_CODE = 120;
    //Image Buttons
    private CardView newsImageButton;
    private CardView mapsImageButton;
    private CardView eventsImageButton;
    //private CardView radioImageButton;
    private CardView canvasImageButton;
    private CardView labImageButton;
    private CardView rangerImageButton;
    private CardView navigateAppButton;
    private CardView eServicesButton;

    private void initUI(View view) {
        newsImageButton = view.findViewById(R.id.news);
        mapsImageButton = view.findViewById(R.id.maps);
        eventsImageButton = view.findViewById(R.id.events);
        //radioImageButton = view.findViewById(R.id.radio);
        canvasImageButton = view.findViewById(R.id.lab);
        rangerImageButton = view.findViewById(R.id.rangerWellness);
        labImageButton = view.findViewById(R.id.lab);
        navigateAppButton = view.findViewById(R.id.navigate_app);
        eServicesButton = view.findViewById(R.id.eServices);

    }

    public HomeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);

        newsImageButton.setOnClickListener(v -> navigateTo(R.id.action_tpa2LandingFragment_to_newsFragment));
        mapsImageButton.setOnClickListener(v -> navigateTo(R.id.action_tpa2LandingFragment_to_mainMap));
        eventsImageButton.setOnClickListener(v -> navigateTo(R.id.action_tpa2LandingFragment_to_eventFragment));
//        radioImageButton.setOnClickListener(v -> navigateTo(R.id.action_homeFragment_to_radioFragment));
        labImageButton.setOnClickListener(v -> navigateTo(R.id.action_tpa2LandingFragment_to_labFragment));
        rangerImageButton.setOnClickListener(v -> navigateTo(R.id.action_global_rwNavigation));

        /** LAUNCH the other apps from home screen **/
        /** THESE 2 click listeners need to be test on a real device, with google play store **/
        /** THESE PACKAGE NAMES WERE GRABBED FROM PLAY STORE! **/
        /** IF the package names of these apps change, they will need to be updated here **/
        navigateAppButton.setOnClickListener(v ->{
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(
                    "com.eab.se"
            );
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.eab.se")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.eab.se")));
                }
            }

        });

        eServicesButton.setOnClickListener(v ->{
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(
                    "com.blackboard.transact.android.v2"
            );
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.blackboard.transact.android.v2")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.blackboard.transact.android.v2")));
                }
            }
        });


        return view;
    }

    private void navigateTo(int action) {
        Navigation.findNavController(getActivity(), R.id.navHostFragment).navigate(action);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
