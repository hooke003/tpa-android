package appfactory.uwp.edu.parksideapp2.ranger_restart.covid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.RecentUpdateCard;
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.UpdateCardAdapter;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;

/**
 * This class initializes the home screen.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class RRCOVIDRecentUpdatesFragment extends Fragment {
    private RecyclerView recentUpdatesRecyclerView;
    private RecyclerView.Adapter adapter;
    private List<RecentUpdateCard> recentUpdateCards;
    private ConstraintLayout returnToHomeConstraintLayout;

    private void initUI(@NotNull View view) {
        returnToHomeConstraintLayout = view.findViewById(R.id.returnToHomeConstraintLayout);
        recentUpdatesRecyclerView = view.findViewById(R.id.recentUpdatesRecyclerView);
        recentUpdatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentUpdateCards = new ArrayList<>();

        for (int i = 0; i < UserConstants.RECENT_UPDATE_CARDS.size(); i++) {
            recentUpdateCards.add(
                    new RecentUpdateCard(
                            UserConstants.RECENT_UPDATE_CARDS.get(i).getTitle(),
                            UserConstants.RECENT_UPDATE_CARDS.get(i).getBody(),
                            UserConstants.RECENT_UPDATE_CARDS.get(i).getDate())
            );
            adapter = new UpdateCardAdapter(recentUpdateCards, getContext(), getParentFragmentManager());
            recentUpdatesRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rr_updates, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        returnToHomeConstraintLayout.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.rr_fragment_container, new RRCOVIDHomeFragment(), "RRCOVIDHomeFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
