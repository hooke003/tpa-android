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

/**
 * This class inflates the third question view.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDQuestionnaireQ3Fragment extends Fragment implements View.OnClickListener {
    public static boolean flag = false;
    private ConstraintLayout returnToQ3ConstraintLayout;
    private ConstraintLayout q3SubmitButton;
    private CardView yesQ3CardView;
    private CardView noQ3CardView;
    private ImageView q3YesCheck;
    private ImageView q3NoCheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_questionnaire_q3, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        returnToQ3ConstraintLayout = view.findViewById(R.id.returnToQ3ConstraintLayout);
        q3SubmitButton = view.findViewById(R.id.q3SubmitButton);
        yesQ3CardView = view.findViewById(R.id.yesQ3CardView);
        noQ3CardView = view.findViewById(R.id.noQ3CardView);
        q3YesCheck = view.findViewById(R.id.q3YesCheck);
        q3NoCheck = view.findViewById(R.id.q3NoCheck);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        returnToQ3ConstraintLayout.setOnClickListener(this);
        q3SubmitButton.setOnClickListener(this);
        yesQ3CardView.setOnClickListener(this);
        noQ3CardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            case R.id.returnToQ3ConstraintLayout: {
                flag = false;
                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDQuestionnaireQ2Fragment(),
                        "RRCOVIDQuestionnaireQ2Fragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.q3SubmitButton: {
                if (q3YesCheck.getVisibility() == View.VISIBLE || q3NoCheck.getVisibility() == View.VISIBLE) {
                    if (RRCOVIDQuestionnaireQ1Fragment.flag || RRCOVIDQuestionnaireQ2Fragment.flag || RRCOVIDQuestionnaireQ3Fragment.flag) {
                        fragmentTransaction.replace(
                                R.id.rr_fragment_container,
                                new RRCOVIDQuestionnaireBadResultFragment(),
                                "RRCOVIDQuestionnaireBadResultFragment"
                        );
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        fragmentTransaction.replace(
                                R.id.rr_fragment_container,
                                new RRCOVIDQuestionnaireGoodResultFragment(),
                                "RRCOVIDQuestionnaireGoodResultFragment"
                        );
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }

            }
            break;
            case R.id.yesQ3CardView: {
                flag = true;
                toggleYes();
            }
            break;
            case R.id.noQ3CardView: {
                flag = false;
                toggleNo();
            }
            break;
            default:
                break;
        }
    }

    private void toggleYes() {
        q3YesCheck.setVisibility(View.VISIBLE);
        q3NoCheck.setVisibility(View.GONE);
    }

    private void toggleNo() {
        q3YesCheck.setVisibility(View.GONE);
        q3NoCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
