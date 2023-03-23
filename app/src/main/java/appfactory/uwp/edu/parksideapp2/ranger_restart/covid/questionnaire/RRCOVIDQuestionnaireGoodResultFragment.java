package appfactory.uwp.edu.parksideapp2.ranger_restart.covid.questionnaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDHomeFragment;

/**
 * This class inflates the results of the questionnaire view.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDQuestionnaireGoodResultFragment extends Fragment implements View.OnClickListener {
    public static boolean flag = false;
    private ConstraintLayout gReturnToCOVIDHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_questionnaire_good_result, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        gReturnToCOVIDHome = view.findViewById(R.id.gReturnToCOVIDHome);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gReturnToCOVIDHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        if (i == R.id.gReturnToCOVIDHome) {
            fragmentTransaction.replace(
                    R.id.rr_fragment_container,
                    new RRCOVIDHomeFragment(),
                    "RRCOVIDHomeFragment"
            );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
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
