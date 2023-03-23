package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.TreeSet;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.LabBuildingObj;
import appfactory.uwp.edu.parksideapp2.Models.LabObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;

public class LabFragment extends Fragment {
    private Realm mRealm;
    RecyclerView mLabBuildingRecyclerView;
    private LabBuildingAdapter mLabBuildingAdapter;

    ArrayList<LabBuildingObj> labBuildingList = new ArrayList<>();


    public LabFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lab, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLabBuildingRecyclerView = view.findViewById(R.id.building_card_recycler_view);
        mLabBuildingRecyclerView.setLayoutManager(layoutManager);

        mRealm.executeTransaction(realm -> {
            RealmResults<LabObj> result = realm.where(LabObj.class).findAll();
            TreeSet<String> buildingName = new TreeSet<>();
            for(LabObj lab: result){
              buildingName.add(lab.getBuilding());
            }

            for (String currentBuilding : buildingName) {
                RealmResults currentBuildingResult = realm.where(LabObj.class).equalTo("building", currentBuilding).findAll();
                int totalCurrentBuildingLabs = currentBuildingResult.size();
                labBuildingList.add(new LabBuildingObj(currentBuilding, totalCurrentBuildingLabs));
            }

        });


        mLabBuildingAdapter = new LabBuildingAdapter(getContext(),labBuildingList);
        mLabBuildingRecyclerView.setAdapter(mLabBuildingAdapter);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}


