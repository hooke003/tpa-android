package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import appfactory.uwp.edu.parksideapp2.R;

public class RWInfoFragment extends Fragment {
    private Button rwNextButton;

    public RWInfoFragment() {

    }

    private void initUI(@NotNull View view) {
        rwNextButton = view.findViewById(R.id.rwNextButton);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rw_info, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rwNextButton.setOnClickListener(
                click -> {
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(
                            R.id.tpa2_landing_fragment_container,
                            new RWGetStartedFragment(),
                            "RWGetStartedFragment"
                    )
                    .addToBackStack(null)
                    .commit();
        });
    }
}
