package appfactory.uwp.edu.parksideapp2.ranger_restart.covid.questionnaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDHomeFragment;

/**
 * This class inflates the first question view.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDQuestionnaireQ1Fragment extends Fragment implements View.OnClickListener {
    public static boolean flag = false;
    private ImageView q1YesCheck;
    private ImageView q1NoCheck;
    private ConstraintLayout returnToQ1ConstraintLayout;
    private ConstraintLayout q1SubmitButton;
    private CardView yesQ1CardView;
    private CardView noQ1CardView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_questionnaire_q1, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        returnToQ1ConstraintLayout = view.findViewById(R.id.returnToRRHomeConstraintLayout);
        q1SubmitButton = view.findViewById(R.id.q1SubmitButton);
        yesQ1CardView = view.findViewById(R.id.yesQ1CardView);
        noQ1CardView = view.findViewById(R.id.noQ1CardView);
        q1YesCheck = view.findViewById(R.id.q1YesCheck);
        q1NoCheck = view.findViewById(R.id.q1NoCheck);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        returnToQ1ConstraintLayout.setOnClickListener(this);
        q1SubmitButton.setOnClickListener(this);
        yesQ1CardView.setOnClickListener(this);
        noQ1CardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            case R.id.returnToRRHomeConstraintLayout: {
                flag = false;
                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDHomeFragment(),
                        "RRCOVIDHomeFragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;

            case R.id.q1SubmitButton: {
                if (q1YesCheck.getVisibility() == View.VISIBLE || q1NoCheck.getVisibility() == View.VISIBLE) {
                    fragmentTransaction.replace(
                            R.id.rr_fragment_container,
                            new RRCOVIDQuestionnaireQ2Fragment(),
                            "RRCOVIDQuestionnaireQ2Fragment"
                    );
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
            break;

            case R.id.yesQ1CardView: {
                toggleYes();
                flag = true;
            }
            break;

            case R.id.noQ1CardView: {
                toggleNo();
                flag = false;
            }
            break;

            default:
                break;
        }
    }

    private void toggleYes() {
        q1YesCheck.setVisibility(View.VISIBLE);
        q1NoCheck.setVisibility(View.GONE);
    }

    private void toggleNo() {
        q1YesCheck.setVisibility(View.GONE);
        q1NoCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
