package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * This contains the fragment to show the selected current update.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDViewUpdateFragment extends Fragment {
    private LinearLayout previousUpdatesLinearLayout;
    private TextView updateTitleTextView;
    private TextView updateBodyTextView;

    private void initUI(@NotNull View view) {
        previousUpdatesLinearLayout = view.findViewById(R.id.previousUpdatesLinearLayout);
        updateTitleTextView = view.findViewById(R.id.updateTitleTextView);
        updateBodyTextView = view.findViewById(R.id.updateBodyTextView);
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previousUpdatesLinearLayout.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDRecentUpdatesFragment(), "RRCOVIDRecentUpdatesFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rr_view_update, container, false);
        initUI(view);
        if (getArguments() != null) {
            updateTitleTextView.setText(getArguments().getString("title"));
            updateBodyTextView.setText(getArguments().getString("body"));
        }
        return view;
    }
}
