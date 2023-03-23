package appfactory.uwp.edu.parksideapp2.ranger_restart.covid.questionnaire;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDHomeFragment;

import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.EMAIL;
import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LOGGED_IN;

/**
 * This class inflates the results of the questionnaire view.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDQuestionnaireBadResultFragment extends Fragment implements View.OnClickListener {
    public static boolean flag = false;
    private ConstraintLayout bReturnToCOVIDHome;
    private Button callStudentHealth;
    private Button openCOVIDReportForm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_questionnaire_bad_result, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        bReturnToCOVIDHome = view.findViewById(R.id.bReturnToCOVIDHome);
        callStudentHealth = view.findViewById(R.id.callStudentHealth);
        openCOVIDReportForm = view.findViewById(R.id.openCOVIDReportForm);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bReturnToCOVIDHome.setOnClickListener(this);
        callStudentHealth.setOnClickListener(this);
        openCOVIDReportForm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        switch (i) {
            case R.id.bReturnToCOVIDHome: {
                fragmentTransaction.replace(
                        R.id.rr_fragment_container,
                        new RRCOVIDHomeFragment(),
                        "RRCOVIDHomeFragment"
                );
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            break;
            case R.id.callStudentHealth: {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "262-595-2366"));
                startActivity(callIntent);
            }
            break;
            case R.id.openCOVIDReportForm: {
                String url = "";
                if (!LOGGED_IN) {
                    if (EMAIL.toLowerCase().contains("@rangers.uwp.edu"))
                        url = "https://cm.maxient.com/reportingform.php?UnivofWisconsinParkside&layout_id=5";
                    else
                        url = "http://uwparkside.qualtrics.com/jfe/form/SV_430mqk7nt4YJODz";

                } else
                    url = "https://cm.maxient.com/reportingform.php?UnivofWisconsinParkside&layout_id=5";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
            break;
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
