package appfactory.uwp.edu.parksideapp2.tpa2.auth;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.SSOAuth.SSOLoginScreenFragment;
import appfactory.uwp.edu.parksideapp2.tpa2.TPA2LandingActivity;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import timber.log.Timber;

import static io.realm.Realm.getApplicationContext;

/**
 * This class initializes the log-in fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2LoginFragment extends Fragment implements View.OnClickListener {
    private static boolean LOGGED_IN = true;
    private final String TAG = "TPA2LoginFragment";
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView forgotPasswordButton;
    private TextView signUpButton;
    private TextView guestLogIn;
    private Button loginButton;
    private ImageView hideLowerLogInLayer;
    private CardView logInProgressBarCardView;
    private ProgressBar logInProgressBar;
    private FirebaseAuth auth;
    private Button logButton;

    public TPA2LoginFragment() {

    }

    private void initUI(@NotNull View view) {
        Timber.d("Initializing User Interface");
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        forgotPasswordButton = view.findViewById(R.id.forgotPasswordButton);
        guestLogIn = view.findViewById(R.id.guestLogIn);
//        loginButton = view.findViewById(R.id.loginButton);
        signUpButton = view.findViewById(R.id.signUpButton);
        hideLowerLogInLayer = view.findViewById(R.id.hideLowerLogInLayer);
        logInProgressBarCardView = view.findViewById(R.id.logInProgressBarCardView);
        logInProgressBar = view.findViewById(R.id.logInProgressBar);
        logButton = view.findViewById(R.id.button2);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        updateUI(auth.getCurrentUser());
//        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        guestLogIn.setOnClickListener(this);

        Log.d("this", "" + this);
        logButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_sso_auth, container, false);
        initUI(view);
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void onLoginClicked() { // in peramiter before - @NotNull String email, @NotNull String password
        logButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Timber.d("button pressed =======================================");
                Intent intent = new Intent(getApplicationContext(), SSOLoginScreenFragment.class);
                startActivity(intent);
            }
        });

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        new AsyncTask<Object, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                emailEditText.setFocusable(false);
                passwordEditText.setFocusable(false);

                forgotPasswordButton.setClickable(false);
                loginButton.setClickable(false);
                signUpButton.setClickable(false);
                guestLogIn.setClickable(false);

                logInProgressBar.setVisibility(View.VISIBLE);
                hideLowerLogInLayer.setVisibility(View.VISIBLE);
                logInProgressBarCardView.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Object... params) {
                updateUI(auth.getCurrentUser());
//                // Check if the e-mail/password combo is not empty
//                if (!email.isEmpty() && !password.isEmpty()) {
//                    // Check the e-mail pattern
//                    if (EmailParse.Parse(email)) {
//                        // Attempt to sign-in
//                        auth.signInWithEmailAndPassword(email, password)
//                                .addOnCompleteListener(requireActivity(), task -> {
//                                    // Check if the sign-in was successful
//                                    if (task.isSuccessful()) {
//                                        Timber.d("signInWithEmail:success");
//                                        // Get the current user
//                                        FirebaseUser user = auth.getCurrentUser();
//                                        updateUI(user);
//                                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                        // Wrong e-mail/password combo.
//                                        Timber.d(
//                                                "signInWithEmail:failure:\n%s",
//                                                task.getException().toString()
//                                        );
//                                        revertUI();
//                                        emailEditText.setError("The password is invalid or the user does not have a password.");
//                                        emailEditText.requestFocus();
//                                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
//                                        // Account does not exist.
//                                        Timber.d(
//                                                "signInWithEmail:failure:\n%s",
//                                                task.getException().toString()
//                                        );
//                                        revertUI();
//                                        emailEditText.setError("There is no account associated with this email.");
//                                        emailEditText.requestFocus();
//                                    }
//                                });
//
//                    } else {
//                        emailEditText.setError("Email entered was not a valid UWP email.");
//                        emailEditText.requestFocus();
//                    }
//                } else {
//                    emailEditText.setError("Please enter your UWP email.");
//                    emailEditText.requestFocus();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(Integer... values) {
//                logInProgressBar.setProgress(values[0]);
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                revertUI();
//            }
//        }.execute();
                return null;
            }

            @SuppressLint("StaticFieldLeak")
            private void updateUI(FirebaseUser currentUser) {
                if (currentUser != null) {
                    // Check if the current user is verified
                    if (currentUser.isEmailVerified()) {
                        LOGGED_IN = true;
                        new AsyncTask<Void, Integer, Void>() {
                            @Override
                            protected void onPreExecute() {
                                emailEditText.setFocusable(false);
                                passwordEditText.setFocusable(false);

                                forgotPasswordButton.setClickable(false);
                                loginButton.setClickable(false);
                                signUpButton.setClickable(false);
                                guestLogIn.setClickable(false);

                                logInProgressBar.setVisibility(View.VISIBLE);
                                hideLowerLogInLayer.setVisibility(View.VISIBLE);
                                logInProgressBarCardView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                UserConstants.EMAIL = currentUser.getEmail();

                            if (UserConstants.INITIAL_REGISTRATION) {
                                UserConstants.DISPLAY_NAME = UserConstants.EMAIL.split("@")[0];
                                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(UserConstants.DISPLAY_NAME).build();
                                currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
//                                    revertUI();
                                    fragmentTransaction.replace(
                                            R.id.rr_login_main_fragment_container,
                                            new TPA2InitializeNotificationSubscriptionsFragment(),
                                            "TPA2InitializeNotificationSubscriptionsFragment")
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });
                        } else {
                            UserConstants.DISPLAY_NAME = currentUser.getDisplayName();
                            try {
                                Thread.sleep(1000);
                                startActivity(new Intent(requireActivity(), TPA2LandingActivity.class));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        logInProgressBar.setProgress(values[0]);
                    }
                }.execute();
            } else {
                Timber.d("signInWithEmail:failure");
                Toast.makeText(
                        getContext(),
                        "Email not verified.",
                        Toast.LENGTH_LONG
                ).show();
                revertUI();
            }
        }

                            }
                        };
                    }

    @Override
    public void onClick(@NotNull View v) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        int i = v.getId();
        switch (i) {
            case R.id.button2: {
                onLoginClicked(); // in peramiter before - emailEditText.getText().toString(), passwordEditText.getText().toString()
            }
            break;
            case R.id.signUpButton: {
                fragmentTransaction.replace(R.id.rr_login_main_fragment_container, new TPA2SignUpFragment(), "TPA2SignUpFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.forgotPasswordButton: {
                fragmentTransaction.replace(R.id.rr_login_main_fragment_container, new TPA2PasswordReset(), "TPA2PasswordReset");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.guestLogIn: {
                LOGGED_IN = false;
                Intent intent = new Intent(requireActivity(), TPA2LandingActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    private void revertUI() {
        emailEditText.setFocusableInTouchMode(true);
        passwordEditText.setFocusableInTouchMode(true);

        forgotPasswordButton.setClickable(true);
        loginButton.setClickable(true);
        signUpButton.setClickable(true);
        guestLogIn.setClickable(true);

        hideLowerLogInLayer.setVisibility(View.GONE);
        logInProgressBarCardView.setVisibility(View.GONE);
        logInProgressBar.setVisibility(View.GONE);
    }
}
