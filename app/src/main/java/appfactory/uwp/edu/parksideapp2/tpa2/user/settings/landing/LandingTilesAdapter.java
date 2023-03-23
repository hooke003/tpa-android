package appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import appfactory.uwp.edu.parksideapp2.R;

import static appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.LANDING_TILE_CARDS;

public class LandingTilesAdapter extends RecyclerView.Adapter<LandingTilesAdapter.TileCardViewHolder> implements TileMoveCallback.TileTouchHelperContract {
    private StartDragListener startDragListener;

    public LandingTilesAdapter(StartDragListener startDragListener) {
        this.startDragListener = startDragListener;
    }

    @NonNull
    @Override
    public TileCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tpa2_landing_tile_item, parent, false);
        return new TileCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TileCardViewHolder holder, int position) {
        LandingTileCard landingTileCard = LANDING_TILE_CARDS.get(position);
        holder.tileCardIcon.setImageResource(landingTileCard.getIconRes());
        holder.tileCardTextView.setText(landingTileCard.getTileCardTextView());
        holder.tileCardDragger.performClick();
        holder.tileCardDragger.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                startDragListener.requestDrag(holder);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return LANDING_TILE_CARDS.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        // Moving up
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(LANDING_TILE_CARDS, i, i + 1);
            }
        } else { // Moving down
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(LANDING_TILE_CARDS, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(TileCardViewHolder tileCardViewHolder) {
        tileCardViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFEFEFEF"));
    }

    @Override
    public void onRowClear(TileCardViewHolder tileCardViewHolder) {
        tileCardViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    public class TileCardViewHolder extends RecyclerView.ViewHolder {
        private ImageView tileCardIcon;
        private ImageView tileCardDragger;
        private TextView tileCardTextView;

        public TileCardViewHolder(@NonNull View itemView) {
            super(itemView);
             tileCardIcon = itemView.findViewById(R.id.tileCardIcon);
             tileCardDragger = itemView.findViewById(R.id.tileCardDragger);
             tileCardTextView = itemView.findViewById(R.id.tileCardTextView);
        }
    }
}
