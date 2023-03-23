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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.jetbrains.annotations.NotNull;

import appfactory.uwp.edu.parksideapp2.R;
import timber.log.Timber;

/**
 * This class initializes the password reset fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2PasswordReset extends Fragment implements View.OnClickListener {
    private final String TAG = "MainPasswordReset";
    private EditText emailEditText;
    private Button passwordResetButton;
    private ImageView backButton;
    private CardView resetProgressBarCardView;
    private ProgressBar resetProgressBar;

    public TPA2PasswordReset() {

    }

    private void initUI(@NotNull View view) {
        Timber.d("Initializing User Interface");
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordResetButton = view.findViewById(R.id.passwordResetButton);
        backButton = view.findViewById(R.id.passwordResetBackButton);

        resetProgressBarCardView = view.findViewById(R.id.resetProgressBarCardView);
        resetProgressBar = view.findViewById(R.id.resetProgressBar);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tpa2_password_reset, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton.setOnClickListener(this);
        passwordResetButton.setOnClickListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    private void onRestClicked(@NotNull String email) {
        // Check if the e-mail field is empty
        if (!email.isEmpty() && EmailParse.Parse(email)) {
            new AsyncTask<Void, Integer, Void>() {
                @Override
                protected void onPreExecute() {
                    emailEditText.setFocusable(false);
                    backButton.setClickable(false);
                    passwordResetButton.setClickable(false);

                    resetProgressBarCardView.setVisibility(View.VISIBLE);
                    resetProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    // Check the pattern of the e-mail
                    // Send a reset password request
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                revertUI();
                                // Check if the e-mail was sent
                                if (task.isSuccessful()) {
                                    CreateDialog(
                                            "Email sent!",
                                            "Please check your e-mail for instructions."
                                    );
                                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    emailEditText.setError("No account associated with this email.");
                                    emailEditText.requestFocus();
                                } else {
                                    //
                                    CreateDialog(
                                            "Failed!",
                                            "Ran into an unknown error processing your request."
                                    );
                                }
                            });
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    Timber.d(String.valueOf(values[0]));
                    resetProgressBar.setProgress(values[0]);
                }

                private void revertUI() {
                    emailEditText.setFocusableInTouchMode(true);
                    backButton.setClickable(true);
                    passwordResetButton.setClickable(true);

                    resetProgressBarCardView.setVisibility(View.GONE);
                    resetProgressBar.setVisibility(View.GONE);
                }
            }.execute();
        } else {
            emailEditText.setError("Email entered was not a valid UWP email.");
            emailEditText.requestFocus();
        }
    }

    @Override
    public void onClick(@NotNull View v) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        int i = v.getId();
        switch (i) {
            case R.id.passwordResetButton: {
                onRestClicked(emailEditText.getText().toString());
            }
            break;
            case R.id.passwordResetBackButton: {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.rr_login_main_fragment_container, new TPA2LoginFragment(), "TPA2LoginFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            default:
                break;
        }
    }

    private void CreateDialog(String title, String message) {
        // Set up the dialog
        AlertDialog.Builder poUpDialog = new AlertDialog.Builder(getActivity());
        poUpDialog
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
