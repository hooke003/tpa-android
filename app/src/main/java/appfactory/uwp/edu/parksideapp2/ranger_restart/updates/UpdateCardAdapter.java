package appfactory.uwp.edu.parksideapp2.ranger_restart.updates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appfactory.uwp.edu.parksideapp2.R;
import appfactory.uwp.edu.parksideapp2.ranger_restart.covid.RRCOVIDViewUpdateFragment;

/**
 * Adapter for the RecyclerView in the recent update view
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class UpdateCardAdapter extends RecyclerView.Adapter<UpdateCardAdapter.CardViewHolder> {
    private List<RecentUpdateCard> recentUpdateCards;
    private Context context;
    private FragmentManager recentUpdatesFragmentManager;

    // Initialize the context, recent updates cards, and the fragment manager
    public UpdateCardAdapter(
            List<RecentUpdateCard> recentUpdateCards,
            Context context,
            FragmentManager
                    recentUpdatesFragmentManager
    ) {
        this.recentUpdateCards = recentUpdateCards;
        this.context = context;
        this.recentUpdatesFragmentManager = recentUpdatesFragmentManager;
    }

    /**
     * Holder for the RecyclerView containing this item.
     *
     * @param parent The RecyclerView
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rr_recent_update_item, parent, false);
        return new CardViewHolder(view);
    }

    /**
     *
     *
     * @param holder the RecyclerView
     * @param position The index of this current item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        RecentUpdateCard recentUpdateCard = recentUpdateCards.get(position);
        holder.cardViewTitle.setText(recentUpdateCard.getTitle());
        holder.cardViewBody.setText(recentUpdateCard.getDate());
        if (position % 2 == 0)
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.updateCardGreen));
        else
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.updateCardOrange));
        holder.cardView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("title", String.valueOf(recentUpdateCards.get(position).getTitle()));
            args.putString("body", String.valueOf(recentUpdateCards.get(position).getBody()));
            FragmentTransaction fragmentTransaction = recentUpdatesFragmentManager.beginTransaction();
            RRCOVIDViewUpdateFragment viewUpdateFragment = new RRCOVIDViewUpdateFragment();
            viewUpdateFragment.setArguments(args);
            fragmentTransaction.replace(R.id.rr_fragment_container, viewUpdateFragment, "RRCOVIDViewUpdateFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    /**
     * @return The current amount of cards in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return recentUpdateCards.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView cardViewTitle;
        public TextView cardViewBody;
        public CardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewTitle = itemView.findViewById(R.id.covid19ResourcesTitleTextView);
            cardViewBody = itemView.findViewById(R.id.covid19ResourcesBodyTextView);
            cardView = itemView.findViewById(R.id.updateCard);
        }
    }
}
