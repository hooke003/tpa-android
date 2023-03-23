package appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants;
import appfactory.uwp.edu.parksideapp2.tpa2.user.settings.TPA2SettingsFragment;

import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LANDING_TILE_CARDS;

public class TPA2ArrangeLandingFragment extends Fragment implements StartDragListener {
    private final String TAG = "TPA2ArrangeLandingFragment";

    private ConstraintLayout preferencesBackButton;
    private RecyclerView landingPageTilesRecyclerView;
    private LandingTilesAdapter landingTilesAdapter;
    private ItemTouchHelper touchHelper;
    private RecyclerView.LayoutManager landingPageTilesRecyclerViewLayoutManager;

    private void initUI(View view) {
        preferencesBackButton = view.findViewById(R.id.preferencesBackButton);
        landingPageTilesRecyclerView = view.findViewById(R.id.landingPageTilesRecyclerView);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LANDING_TILE_CARDS.size() == 0) {
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Maps",
                            0,
                            R.drawable.ic_maps_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Ranger Wellness",
                            1,
                            R.drawable.ic_ranger_wellness_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "News",
                            2,
                            R.drawable.ic_news_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Navigate",
                            3,
                            R.drawable.ic_navigate_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Events",
                            4,
                            R.drawable.ic_events_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "eAccounts",
                            5,
                            R.drawable.ic_eaccounts_small
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Computer Labs",
                            6,
                            R.drawable.ic_computer_labs_small
                    )
            );
//            LANDING_TILE_CARDS.add(
//                    new LandingTileCard(
//                            "Ranger Restart",
//                            7,
//                            R.drawable.ic_ranger_restart_small
//                    )
//            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Title IX",
                            8,
                            R.mipmap.ic_titleix
                    )
            );
            LANDING_TILE_CARDS.add(
                    new LandingTileCard(
                            "Indoor Navigation",
                            9,
                            R.drawable.ic_indoornav_small
                    )
            );
        }
        landingPageTilesRecyclerViewLayoutManager = new LinearLayoutManager(requireContext());
        landingTilesAdapter = new LandingTilesAdapter(this);
        ItemTouchHelper.Callback callback = new TileMoveCallback(landingTilesAdapter);
        touchHelper = new ItemTouchHelper(callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tpa2_arrange_landing, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        touchHelper.attachToRecyclerView(landingPageTilesRecyclerView);
        landingPageTilesRecyclerView.setLayoutManager(landingPageTilesRecyclerViewLayoutManager);
        landingPageTilesRecyclerView.setAdapter(landingTilesAdapter);

        preferencesBackButton.setOnClickListener(click -> {
            for (int i = 0; i < landingPageTilesRecyclerView.getChildCount(); i++) {
                final RecyclerView.ViewHolder holder = landingPageTilesRecyclerView
                        .getChildViewHolder(
                                landingPageTilesRecyclerView
                                        .getChildAt(i)
                        );
                for (LandingTileCard landingTileCard : LANDING_TILE_CARDS) {
                    if (
                            ((TextView) holder.itemView.findViewById(R.id.tileCardTextView))
                                    .getText().equals(landingTileCard.getTileCardTextView())
                    )
                        landingTileCard.setPosition(i);
                }
            }
            UserConstants.save(requireActivity());
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.replace(
                    R.id.tpa2_landing_fragment_container,
                    new TPA2SettingsFragment(),
                    "TPA2SettingsFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}
