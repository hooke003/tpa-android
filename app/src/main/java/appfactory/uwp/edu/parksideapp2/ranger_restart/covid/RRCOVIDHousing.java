package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * The DrawerLayout item for the housing.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDHousing extends Fragment {
    private ScrollView housingScrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_housing, container, false);
        housingScrollView = view.findViewById(R.id.housingScrollView);
        housingScrollView.setVerticalScrollBarEnabled(false);
        housingScrollView.setHorizontalScrollBarEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
