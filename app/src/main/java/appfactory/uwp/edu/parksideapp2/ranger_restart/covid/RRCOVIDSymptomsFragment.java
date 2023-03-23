package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * This class initializes the home screen.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDSymptomsFragment extends Fragment {
    private final String TAG = "SymptomsFragment";
    private ConstraintLayout symptomsBackLayout;
    private CardView symptomsWebViewCard;

    private void initUI(View view) {
        symptomsBackLayout = view.findViewById(R.id.symptomsBackLayout);
        symptomsWebViewCard = view.findViewById(R.id.symptomsWebViewCard);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Called");
        View view = inflater.inflate(R.layout.fragment_rr_symptoms, container, false);
        initUI(view);
        symptomsBackLayout.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        symptomsWebViewCard.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("url", "https://docs.google.com/viewer?embedded=true&url=https://www.uwp.edu/live/services/studenthealth/upload/covid-19_facts_mcw_3-17-20_8-4-1-1.pdf");
            RRCOVIDWebView rrcovidWebView = new RRCOVIDWebView();
            rrcovidWebView.setArguments(args);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, rrcovidWebView, "RRCOVIDWebView");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
