package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
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
 * This class initializes the Dos and Don'ts fragment.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDDosDontsFragment extends Fragment {
    private final String TAG = "SymptomsFragment";
    private ConstraintLayout dosDontsBackButton;
    private CardView dosDontsWebViewCard;

    private void initUI(View view) {
        dosDontsBackButton = view.findViewById(R.id.dosDontsBackButton);
        dosDontsWebViewCard = view.findViewById(R.id.dosDontsWebViewCard);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_dos_donts, container, false);
        initUI(view);
        dosDontsBackButton.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        dosDontsWebViewCard.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("url", "https://www.cdc.gov/coronavirus/2019-ncov/prevent-getting-sick/prevention.html?CDC_AA_refVal=https%3A%2F%2Fwww.cdc.gov%2Fcoronavirus%2F2019-ncov%2Fprepare%2Fprevention.html");
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
