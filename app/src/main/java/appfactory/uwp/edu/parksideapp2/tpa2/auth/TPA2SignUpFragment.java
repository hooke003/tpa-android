package appfactory.uwp.edu.parksideapp2.tpa2.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;

/**
 * This class initializes the sign-up fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2SignUpFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "TPA2SignUpFragment";
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button signUpButton;
    private ImageView backButton;
    private FirebaseAuth auth;
    private CardView signUpProgressBarCardView;
    private ProgressBar signUpProgressBar;

    private void initUI(@NotNull View view) {
        Timber.d("Initializing User Interface");
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordConfirmEditText = view.findViewById(R.id.passwordConfirmEditText);
        signUpButton = view.findViewById(R.id.signUpButton);
        backButton = view.findViewById(R.id.signUpBackButton);
        signUpProgressBarCardView = view.findViewById(R.id.signUpProgressBarCardView);
        signUpProgressBar = view.findViewById(R.id.signUpProgressBar);
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tpa2_sign_up, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        signUpButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void onSignUpClicked(String email, String password) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected void onPreExecute() {
                loadingUI();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), task -> {
                            revertUI();
                            // Check if the sign up was successful
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Timber.d("createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                emailEditText.setError("There is already an account associated with the email.");
                                emailEditText.requestFocus();
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.d("createUserWithEmail:failure");
                                Toast.makeText(
                                        getContext(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                updateUI(null);
                            }
                        });
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                signUpProgressBar.setProgress(values[0]);
            }
        }.execute();
    }

    private void revertUI() {
        emailEditText.setFocusableInTouchMode(true);
        passwordEditText.setFocusableInTouchMode(true);
        passwordConfirmEditText.setFocusableInTouchMode(true);

        backButton.setClickable(true);
        signUpButton.setClickable(true);

        signUpProgressBarCardView.setVisibility(View.GONE);
        signUpProgressBar.setVisibility(View.GONE);
    }

    private void loadingUI() {
        emailEditText.setFocusable(false);
        passwordEditText.setFocusable(false);
        passwordConfirmEditText.setFocusable(false);

        backButton.setClickable(false);
        signUpButton.setClickable(false);

        signUpProgressBarCardView.setVisibility(View.VISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (user.isEmailVerified()) {

            } else {
                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected void onPreExecute() {
                        loadingUI();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(requireActivity(), task -> {
                                    revertUI();
                                    // Alert the user they need to verify their account.
                                    if (task.isSuccessful()) {
                                        CreateDialog(
                                                "Please verify your account.",
                                                String.format(
                                                        "Verification email sent to: %s",
                                                        user.getEmail()
                                                )
                                        );
                                    } else {
                                        Timber.e("sendEmailVerification");
                                        Timber.d(task.getException().toString());
                                        Toast.makeText(
                                                getContext(),
                                                "Failed to send verification email.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        signUpProgressBar.setProgress(values[0]);
                    }
                }.execute();
            }
        }
    }

    @Override
    public void onClick(@NotNull View v) {
        int i = v.getId();
        if (i == R.id.signUpButton)
            if (!EmailParse.Parse(emailEditText.getText().toString())) {
                emailEditText.setError("Email entered was not a valid UWP email.");
                emailEditText.requestFocus();
            } else if (passwordEditText.getText().toString().equals(passwordConfirmEditText.getText().toString()))
                onSignUpClicked(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            else {
                passwordEditText.setError("Passwords do not match.");
                passwordConfirmEditText.setError("Passwords do not match.");
                passwordEditText.requestFocus();
            }
        if (i == R.id.signUpBackButton) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(
                    R.id.rr_login_main_fragment_container,
                    new TPA2LoginFragment(),
                    "TPA2LoginFragment"
            );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    /**
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
                    fragmentTransaction.replace(R.id.rr_login_main_fragment_container, new TPA2LoginFragment(), "TPA2LoginFragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                })
                .setIcon(R.drawable.ic_bigbear)
                .create()
                .show();
    }
}
