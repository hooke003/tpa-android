package appfactory.uwp.edu.parksideapp2.tpa2.user.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.Notifications.ManageNotifications;
import appfactory.uwp.edu.parksideapp2.R;
//import appfactory.uwp.edu.parksideapp2.geofencing.GeofenceHelper;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingFragment;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing.TPA2ArrangeLandingFragment;
import appfactory.uwp.edu.parksideapp2.utils.CheckNetStatus;
import timber.log.Timber;

/**
 * This class inflates the profile fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2SettingsFragment extends Fragment implements View.OnClickListener {
    private static final int FINE_LOCATION_ACCESS_REQUEST_CODE = 1001;
    private static final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 1002;
    private ConstraintLayout settingsBackButton;
    private ConstraintLayout changePasswordConstraintButton;
    private ConstraintLayout notificationsConstraintLayoutButton;
    private ConstraintLayout arrangeAppsConstraintButton;
    private ConstraintLayout inputTextConstraintLayout;
    private ConstraintLayout editDisplayNameConstraintLayout;
    private TextView displayNameTextView;
    private TextView emailTextView;
    private Switch locationSwitch;
//    private Switch geofencingSwitch;
    private Button cancelChangesButton;
    private Button acceptChangesButton;
    private TextView inputTextTitleTextView;
    private EditText enterValueEditText;
    private ProgressBar uploadingProgressBar;
    private CardView uploadingProgressBarCardView;
//    private GeofencingClient geofencingClient;
//    private GeofenceHelper geofenceHelper;

    private void initUI(View view) {
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        editDisplayNameConstraintLayout = view.findViewById(R.id.editDisplayNameConstraintLayout);
        settingsBackButton = view.findViewById(R.id.settingsBackButton);
//        changePasswordConstraintButton = view.findViewById(R.id.changePasswordConstraintButton);
        notificationsConstraintLayoutButton = view.findViewById(R.id.notificationsConstraintLayoutButton);
        arrangeAppsConstraintButton = view.findViewById(R.id.arrangeAppsConstraintButton);
        locationSwitch = view.findViewById(R.id.locationSwitch);
        locationSwitch.setChecked(UserConstants.FINE_LOCATION);
//        geofencingSwitch = view.findViewById(R.id.geofencingSwitch);
//        geofencingSwitch.setChecked(UserConstants.BACKGROUND_LOCATION);
        cancelChangesButton = view.findViewById(R.id.cancelChangesButton);
        acceptChangesButton = view.findViewById(R.id.acceptChangesButton);
        inputTextConstraintLayout = view.findViewById(R.id.inputTextConstraintLayout);
        inputTextTitleTextView = view.findViewById(R.id.inputTextTitleTextView);
        enterValueEditText = view.findViewById(R.id.enterValueEditText);
        uploadingProgressBar = view.findViewById(R.id.uploadingProgressBar);
        uploadingProgressBarCardView = view.findViewById(R.id.uploadingProgressBarCardView);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tpa2_update_profile, container, false);
//        geofencingClient = LocationServices.getGeofencingClient(requireActivity());
//        geofenceHelper = new GeofenceHelper(requireContext());
        initUI(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayNameTextView.setText(UserConstants.DISPLAY_NAME);
        emailTextView.setText(UserConstants.EMAIL);
        editDisplayNameConstraintLayout.setOnClickListener(this);
        settingsBackButton.setOnClickListener(this);
//        changePasswordConstraintButton.setOnClickListener(this);
        notificationsConstraintLayoutButton.setOnClickListener(this);
        arrangeAppsConstraintButton.setOnClickListener(this);
        cancelChangesButton.setOnClickListener(this);
        acceptChangesButton.setOnClickListener(this);
        locationSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                    }
                    locationSwitch.setChecked(false);
                    UserConstants.FINE_LOCATION = false;
                } else {
                    new AlertDialog.Builder(requireContext())
                            .setTitle(Html.fromHtml("<font color='#000000'>Permissions granted!</font>"))
                            .setMessage(Html.fromHtml("<font color='#000000'>You are now sharing your location!</font>"))
                            .setNeutralButton("Ok", (dialogInterface, i) -> {
                            })
                            .create()
                            .show();
                    UserConstants.FINE_LOCATION = true;
                }
            }
            UserConstants.save(requireActivity());
        });
    }

//        geofencingSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                new AlertDialog.Builder(requireContext())
//                        .setTitle(Html.fromHtml("<font color='#000000'>Not supported!</font>"))
//                        .setMessage(Html.fromHtml("<font color='#000000'>Your version of Android does not support geofencing!</font>"))
//                        .setNeutralButton("Ok", (dialogInterface, i) -> {
//                        })
//                        .create()
//                        .show();
//                geofencingSwitch.setChecked(false);
//                return;
//            }
//
//            if (isChecked) {
//                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    new AlertDialog.Builder(requireContext())
//                            .setTitle(Html.fromHtml("<font color='#000000'>Permissions granted!</font>"))
//                            .setMessage(Html.fromHtml("<font color='#000000'>You are now sharing your background location!</font>"))
//                            .setNeutralButton("Ok", (dialogInterface, i) -> {
//                            })
//                            .create()
//                            .show();
//                    UserConstants.BACKGROUND_LOCATION = true;
//                    genGeofence();
//                } else {
//                    geofencingSwitch.setChecked(false);
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//                        //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
//                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                    } else {
//                        try {
//                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } else {
//                NotificationManager notificationManager =
//                        (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.deleteNotificationChannel("geofencing");
//                geofencingClient.removeGeofences(geofenceHelper.getPendingIntent())
//                        .addOnCompleteListener(task -> {
//                            new AlertDialog.Builder(requireContext())
//                                    .setTitle(Html.fromHtml("<font color='#000000'>Success!</font>"))
//                                    .setMessage(Html.fromHtml("<font color='#000000'>Geofencing has been removed!</font>"))
//                                    .setNeutralButton("Ok", (dialogInterface, i) -> {
//                                    })
//                                    .create()
//                                    .show();
//                            UserConstants.BACKGROUND_LOCATION = false;
//                        });
//            }
//            UserConstants.save(requireActivity());
//        });


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            locationSwitch.setChecked(true);
            return;
        }

//        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                geofencingSwitch.setChecked(true);
//                genGeofence();
//            }
//        }
    }

    private void togglePopUp(String title) {
        if (title.length() > 0) {
            inputTextTitleTextView.setText(title);
//            editDisplayNameConstraintLayout.setClickable(false);
            settingsBackButton.setClickable(false);
//            changePasswordConstraintButton.setClickable(false);
            notificationsConstraintLayoutButton.setClickable(false);
            arrangeAppsConstraintButton.setClickable(false);
//            geofencingSwitch.setClickable(false);
            locationSwitch.setClickable(false);
        }
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            // Return to the landing screen
            case R.id.settingsBackButton: {
                returnHome();
            }
            break;
//            case R.id.editDisplayNameConstraintLayout: {
//                inputTextConstraintLayout.setVisibility(View.VISIBLE);
//                togglePopUp("Enter new display name.");
//            }
//            break;
//            case R.id.changePasswordConstraintButton: {
//                // Navigate to to the
//                fragmentTransaction.replace(
//                        R.id.tpa2_landing_fragment_container,
//                        new TPA2ChangePasswordFragment(),
//                        "TPA2ChangePasswordFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//            break;
            case R.id.notificationsConstraintLayoutButton: {
                fragmentTransaction.replace(
                        R.id.tpa2_landing_fragment_container,
                        new TPA2ToggleSubscriptionsFragment(),
                        "TPA2EnableNotificationsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.arrangeAppsConstraintButton: {
                fragmentTransaction.replace(
                        R.id.tpa2_landing_fragment_container,
                        new TPA2ArrangeLandingFragment(),
                        "TPA2ArrangeLandingFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.cancelChangesButton: {
                inputTextConstraintLayout.setVisibility(View.GONE);
                enterValueEditText.setText("");
                togglePopUp("");
                editDisplayNameConstraintLayout.setClickable(true);
                settingsBackButton.setClickable(true);
                changePasswordConstraintButton.setClickable(true);
                notificationsConstraintLayoutButton.setClickable(true);
                arrangeAppsConstraintButton.setClickable(true);
//                geofencingSwitch.setClickable(true);
                locationSwitch.setClickable(true);
            }
            break;
            case R.id.acceptChangesButton: {
                if (CheckNetStatus.isConnected(requireContext())) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
                    if (inputTextTitleTextView.getText().toString().contains("display name")) {
                        if (enterValueEditText.getText().toString().length() > 1 &&
                                enterValueEditText.getText().toString().length() < 40) {

                            new AsyncTask<Void, Integer, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    UserConstants.DISPLAY_NAME = enterValueEditText.getText().toString();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(UserConstants.DISPLAY_NAME).build();
                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(task -> {
                                        CreateDialog("Success!", "Display name updated!", true);
                                        UserConstants.DISPLAY_NAME = enterValueEditText.getText().toString();
                                    }).addOnFailureListener(e -> {
                                        CreateDialog("Failed!", "Failed to update display name!", false);
                                        UserConstants.DISPLAY_NAME = enterValueEditText.getText().toString();
                                        Timber.d(UserConstants.DISPLAY_NAME);
                                    });

                                    return null;
                                }

                                @Override
                                protected void onPreExecute() {
                                    enterValueEditText.setFocusable(false);
                                    acceptChangesButton.setClickable(false);
                                    cancelChangesButton.setClickable(false);
                                    uploadingProgressBar.setVisibility(View.VISIBLE);
                                    uploadingProgressBarCardView.setVisibility(View.VISIBLE);
                                    editDisplayNameConstraintLayout.setClickable(false);
                                    settingsBackButton.setClickable(false);
                                    changePasswordConstraintButton.setClickable(false);
                                    notificationsConstraintLayoutButton.setClickable(false);
                                    arrangeAppsConstraintButton.setClickable(false);
//                                    geofencingSwitch.setClickable(false);
                                    locationSwitch.setClickable(false);
                                }

                                @Override
                                protected void onProgressUpdate(Integer... values) {
                                    uploadingProgressBar.setProgress(values[0]);
                                }
                            }.execute();
                        } else {
                            enterValueEditText.setError("Not a valid display name!");
                            enterValueEditText.requestFocus();
                            break;
                        }
                    }
                    inputTextConstraintLayout.setVisibility(View.GONE);
                } else {
                    CreateDialog("Failed!", "Failed to update display name!", false);
                }
                togglePopUp("");
            }
            break;
            default:
                break;
        }
    }

    /**
     * Return to the TPA 2.0 landing screen
     */
    private void returnHome() {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.tpa2_landing_fragment_container,
                new TPA2LandingFragment(),
                "TPA2LandingFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * @param title        Title for the pop up message
     * @param message      Body of the pop up message
     * @param isSuccessful Was the display name updated
     */
    private void CreateDialog(String title, String message, boolean isSuccessful) {
        try {
            Thread.sleep(1500);
            Timber.d(UserConstants.DISPLAY_NAME);
            displayNameTextView.setText(UserConstants.DISPLAY_NAME);
            enterValueEditText.setFocusableInTouchMode(true);
            acceptChangesButton.setClickable(true);
            cancelChangesButton.setClickable(true);
            uploadingProgressBar.setVisibility(View.GONE);
            uploadingProgressBarCardView.setVisibility(View.GONE);
            editDisplayNameConstraintLayout.setClickable(true);
            settingsBackButton.setClickable(true);
            changePasswordConstraintButton.setClickable(true);
            notificationsConstraintLayoutButton.setClickable(true);
            arrangeAppsConstraintButton.setClickable(true);
//            geofencingSwitch.setClickable(true);
            locationSwitch.setClickable(true);
            AlertDialog.Builder logOutDialog = new AlertDialog.Builder(getActivity());
            logOutDialog
                    .setTitle(Html.fromHtml(String.format("<font color='#000000'>%s</font>", title)))
                    .setMessage(Html.fromHtml(String.format("<font color='#000000'>%s</font>", message)))
                    .setNeutralButton("Ok", (dialog, which) -> {
                        if (isSuccessful) {
                            returnHome();
                        }
                    })
                    .setIcon(R.drawable.ic_bigbear)
                    .create()
                    .show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void genGeofence() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Geofence geofence = geofenceHelper.getGeofence();
//            GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
//            PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
//            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//                    .addOnSuccessListener(aVoid -> {
//                        Timber.d("onSuccess: Geofence Added...");
//                        NotificationChannel notificationChannel = ManageNotifications.createNotificationChannel(
//                                "geofencing",
//                                "Geofencing",
//                                NotificationManager.IMPORTANCE_HIGH
//                        );
//                        NotificationManager notificationManager =
//                                (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.createNotificationChannel(Objects.requireNonNull(notificationChannel));
//                    })
//                    .addOnFailureListener(e -> {
//                        String errorMessage = geofenceHelper.getErrorString(e);
//                        Timber.d("onFailure: " + errorMessage);
//                    });
//        } else {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
//            Timber.e("ACCESS_FINE_LOCATION permissions not granted");
//        }
//    }
}
