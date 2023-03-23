package appfactory.uwp.edu.parksideapp2.ComputerLab;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Models.LabObj;
import appfactory.uwp.edu.parksideapp2.R;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class LabDetailActivity extends AppCompatActivity {
    private Realm mRealm;
    private RecyclerView mlabRecyclerView;
    private LabAdapter mLabAdapter;
    private ArrayList<LabObj> labList = new ArrayList<>();

    public LabDetailActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_detail);

        mlabRecyclerView = (RecyclerView)findViewById(R.id.lab_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mlabRecyclerView.setLayoutManager(layoutManager);
        Timber.d("Run");
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            RealmResults<LabObj> currentBuildingResult = realm.where(LabObj.class)
                    .equalTo("building", getIntent().getStringExtra("buildingName")).findAll();
            labList.clear();
            labList.addAll(currentBuildingResult);
        });

        mLabAdapter = new LabAdapter(this,labList);
        mlabRecyclerView.setAdapter(mLabAdapter);
    }
}
