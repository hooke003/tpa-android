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
 * This class inflates the second question view.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDQuestionnaireQ2Fragment extends Fragment implements View.OnClickListener {
    public static boolean flag = false;
    private ConstraintLayout returnToQ2ConstraintLayout;
    private ConstraintLayout q2SubmitButton;
    private ImageView selectionACheck;
    private ImageView selectionBCheck;
    private ImageView selectionCCheck;
    private ImageView selectionDCheck;
    private ImageView selectionECheck;
    private ImageView selectionFCheck;
    private ImageView selectionGCheck;
    private ImageView selectionHCheck;
    private ImageView selectionICheck;
    private CardView selectionACardView;
    private CardView selectionBCardView;
    private CardView selectionCCardView;
    private CardView selectionDCardView;
    private CardView selectionECardView;
    private CardView selectionFCardView;
    private CardView selectionGCardView;
    private CardView selectionHCardView;
    private CardView selectionICardView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_questionnaire_q2, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        returnToQ2ConstraintLayout = view.findViewById(R.id.returnToQ2ConstraintLayout);

        q2SubmitButton = view.findViewById(R.id.q2SubmitButton);

        selectionACheck = view.findViewById(R.id.selectionACheck);
        selectionBCheck = view.findViewById(R.id.selectionBCheck);
        selectionCCheck = view.findViewById(R.id.selectionCCheck);
        selectionDCheck = view.findViewById(R.id.selectionDCheck);
        selectionECheck = view.findViewById(R.id.selectionECheck);
        selectionFCheck = view.findViewById(R.id.selectionFCheck);
        selectionGCheck = view.findViewById(R.id.selectionGCheck);
        selectionHCheck = view.findViewById(R.id.selectionHCheck);
        selectionICheck = view.findViewById(R.id.selectionICheck);

        selectionACardView = view.findViewById(R.id.selectionACardView);
        selectionBCardView = view.findViewById(R.id.selectionBCardView);
        selectionCCardView = view.findViewById(R.id.selectionCCardView);
        selectionDCardView = view.findViewById(R.id.selectionDCardView);
        selectionECardView = view.findViewById(R.id.selectionECardView);
        selectionFCardView = view.findViewById(R.id.selectionFCardView);
        selectionGCardView = view.findViewById(R.id.selectionGCardView);
        selectionHCardView = view.findViewById(R.id.selectionHCardView);
        selectionICardView = view.findViewById(R.id.selectionICardView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        returnToQ2ConstraintLayout.setOnClickListener(this);

        q2SubmitButton.setOnClickListener(this);

        selectionACardView.setOnClickListener(this);
        selectionBCardView.setOnClickListener(this);
        selectionCCardView.setOnClickListener(this);
        selectionDCardView.setOnClickListener(this);
        selectionECardView.setOnClickListener(this);
        selectionFCardView.setOnClickListener(this);
        selectionGCardView.setOnClickListener(this);
        selectionHCardView.setOnClickListener(this);
        selectionICardView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            case R.id.returnToQ2ConstraintLayout: {
                flag = false;
                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDQuestionnaireQ1Fragment(),
                        "RRCOVIDQuestionnaireQ1Fragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.q2SubmitButton: {
                flag = selectionACheck.getVisibility() != View.GONE ||
                        selectionBCheck.getVisibility() != View.GONE ||
                        selectionCCheck.getVisibility() != View.GONE ||
                        selectionDCheck.getVisibility() != View.GONE ||
                        selectionECheck.getVisibility() != View.GONE ||
                        selectionFCheck.getVisibility() != View.GONE ||
                        selectionGCheck.getVisibility() != View.GONE ||
                        selectionHCheck.getVisibility() != View.GONE ||
                        selectionICheck.getVisibility() != View.GONE;
                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDQuestionnaireQ3Fragment(),
                        "RRCOVIDQuestionnaireQ3Fragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.selectionACardView: {
                toggleSelection(selectionACheck);
            }
            break;
            case R.id.selectionBCardView: {
                toggleSelection(selectionBCheck);
            }
            break;
            case R.id.selectionCCardView: {
                toggleSelection(selectionCCheck);
            }
            break;
            case R.id.selectionDCardView: {
                toggleSelection(selectionDCheck);
            }
            break;
            case R.id.selectionECardView: {
                toggleSelection(selectionECheck);
            }
            break;
            case R.id.selectionFCardView: {
                toggleSelection(selectionFCheck);
            }
            break;
            case R.id.selectionGCardView: {
                toggleSelection(selectionGCheck);
            }
            break;
            case R.id.selectionHCardView: {
                toggleSelection(selectionHCheck);
            }
            break;
            case R.id.selectionICardView: {
                toggleSelection(selectionICheck);
            }
            break;
            default:
                break;
        }
    }

    private void toggleSelection(ImageView selection) {
        if (selection.getVisibility() == View.VISIBLE)
            selection.setVisibility(View.GONE);
        else
            selection.setVisibility(View.VISIBLE);

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
