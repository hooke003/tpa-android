package appfactory.uwp.edu.parksideapp2.tpa2.user.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;

/**
 * This class inflates the change password fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2ChangePasswordFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "TPA2ChangePasswordFragment";

    private ConstraintLayout changePasswordBackButton;

    private EditText currentPasswordEditText;
    private EditText updatePasswordEditText;
    private EditText confirmPasswordNewPasswordEditText;

    private CardView changePasswordProgressBarCardView;
    private ProgressBar changePasswordProgressBar;

    private Button updatePasswordButton;

    private void initUI(View view) {
        changePasswordBackButton = view.findViewById(R.id.changePasswordBackButton);

        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        updatePasswordEditText = view.findViewById(R.id.updatePasswordEditText);
        confirmPasswordNewPasswordEditText = view.findViewById(R.id.confirmPasswordNewPasswordEditText);

        updatePasswordButton = view.findViewById(R.id.updatePasswordButton);

        changePasswordProgressBarCardView = view.findViewById(R.id.changePasswordProgressBarCardView);
        changePasswordProgressBar = view.findViewById(R.id.changePasswordProgressBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tpa2_change_password, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        changePasswordBackButton.setOnClickListener(this);
        updatePasswordButton.setOnClickListener(this);
    }

    @Override
    @SuppressLint("StaticFieldLeak")
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            // Buttons to assign the logic to the back button and the update password
            case R.id.changePasswordBackButton: {
                fragmentTransaction.replace(
                        R.id.tpa2_landing_fragment_container,
                        new TPA2SettingsFragment(),
                        "TPA2SettingsFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.updatePasswordButton: {
                // Check the entered fields
                String currentPassword = currentPasswordEditText.getText().toString();
                if (currentPassword.isEmpty()) {
                    currentPasswordEditText.setError("Please enter your password.");
                    currentPasswordEditText.requestFocus();
                    break;
                } else if (updatePasswordEditText.getText().toString().isEmpty()) {
                    updatePasswordEditText.setError("Please enter your a new password.");
                    updatePasswordEditText.requestFocus();
                    break;
                } else if (confirmPasswordNewPasswordEditText.getText().toString().isEmpty()) {
                    confirmPasswordNewPasswordEditText.setError("Please re-enter your new password.");
                    confirmPasswordNewPasswordEditText.requestFocus();
                    break;
                }
                if (!updatePasswordEditText.getText().toString().equals(confirmPasswordNewPasswordEditText.getText().toString())) {
                    Timber.d("Password 1: %s", updatePasswordEditText.getText());
                    Timber.d("Password 2: %s", confirmPasswordNewPasswordEditText.getText());
                    updatePasswordEditText.setError("Passwords are not the same");
                    updatePasswordEditText.requestFocus();
                    break;
                }

                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingUI();
                    }

                    @SuppressLint("TimberArgCount")
                    @Override
                    protected Void doInBackground(Void... voids) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential(
                                Objects.requireNonNull(
                                        Objects.requireNonNull(
                                                user
                                        ).getEmail()
                                ), currentPassword
                        );
                        // Check if this email-password combo is correct
                        user.reauthenticate(credential)
                                .addOnCompleteListener(authTask -> {
                                    // Correct email-password combo
                                    if (authTask.isSuccessful()) {
                                        Timber.d("Correct email-password combo");
                                        user.updatePassword(updatePasswordEditText.getText().toString())
                                                .addOnCompleteListener(passUpdateTask -> {
                                                    if (passUpdateTask.isSuccessful()) {
                                                        Timber.d("Correct email-password combo");

                                                        // Get this user's FCM token
                                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                                .addOnCompleteListener(task -> {
                                                                    // Get the users email
                                                                    FirebaseAuth auth = FirebaseAuth.getInstance();

                                                                    // Get new Instance ID token
                                                                    String email = auth.getCurrentUser().getEmail();
                                                                    Map<String, String> userData = new HashMap<>();
                                                                    userData.put("email", email);
                                                                    String token = Objects.requireNonNull(task.getResult()).getToken();
                                                                    userData.put("token", token);

                                                                    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

                                                                    // Return the built request
                                                                    mFunctions
                                                                            .getHttpsCallable("updatefcm")
                                                                            .call(userData)
                                                                            .continueWith(httpsCallableResultTask -> {
                                                                                // This continuation runs on either success or failure, but if the task
                                                                                // has failed then getResult() will throw an Exception which will be
                                                                                // propagated down.

                                                                                Timber.d("Posting e-mail and token.");

                                                                                return (String) httpsCallableResultTask.getResult().getData();
                                                                            }).addOnCompleteListener(updateFCMTask -> {
                                                                        Exception e = updateFCMTask.getException();
                                                                        if (e instanceof FirebaseFunctionsException) {
                                                                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                                                            FirebaseFunctionsException.Code code = ffe.getCode();
                                                                            Object details = ffe.getDetails();
                                                                            Timber.d(String.format("FFE Code: %s", code));
                                                                        }
                                                                        if (updateFCMTask.isSuccessful()) {
                                                                            try {
                                                                                Thread.sleep(1500);
                                                                                revertUI();
                                                                            } catch (InterruptedException ex) {
                                                                                ex.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                });

                                                        CreateDialog(
                                                                "Success!",
                                                                "Your password has been updated."
                                                        );
                                                    } else {
                                                        Timber.d("updatePassword error: %s", passUpdateTask.getException().toString());
                                                        CreateDialog(
                                                                "Failed!",
                                                                passUpdateTask.getException().toString()
                                                        );
                                                    }
                                                });
                                    } else if (authTask.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        CreateDialog(
                                                "Failed!",
                                                "The current password entered is invalid."
                                        );
                                    } else {
                                        Timber.d("reauthenticate error: %s", authTask.getException().toString());
                                        CreateDialog(
                                                "Failed!",
                                                authTask.getException().toString()
                                        );
                                    }
                                });
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        changePasswordProgressBar.setProgress(values[0]);
                    }
                }.execute();

            }
            break;
            default:
                break;
        }
    }

    /**
     * Create the pop up dialog to alert the user.
     *
     * @param title   Title for the pop up message
     * @param message Body of the pop up message
     */
    private void CreateDialog(String title, String message) {
        AlertDialog.Builder popUpDialog = new AlertDialog.Builder(getActivity());
        popUpDialog
                .setTitle(Html.fromHtml(String.format("<font color='#000000'>%s</font>", title)))
                .setMessage(Html.fromHtml(String.format("<font color='#000000'>%s</font>", message)))
                .setNeutralButton("Ok", (dialog, which) -> {
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(
                            R.id.tpa2_landing_fragment_container,
                            new TPA2SettingsFragment(),
                            "TPA2SettingsFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                })
                .setIcon(R.drawable.ic_bigbear)
                .create()
                .show();
    }

    private void revertUI() {
        changePasswordProgressBarCardView.setVisibility(View.GONE);
        changePasswordProgressBar.setVisibility(View.GONE);
        currentPasswordEditText.setFocusable(false);
        updatePasswordEditText.setFocusable(false);
        confirmPasswordNewPasswordEditText.setFocusable(false);
    }

    private void loadingUI() {
        changePasswordProgressBarCardView.setVisibility(View.VISIBLE);
        changePasswordProgressBar.setVisibility(View.VISIBLE);
        currentPasswordEditText.setFocusableInTouchMode(false);
        updatePasswordEditText.setFocusableInTouchMode(false);
        confirmPasswordNewPasswordEditText.setFocusableInTouchMode(false);
    }
}
