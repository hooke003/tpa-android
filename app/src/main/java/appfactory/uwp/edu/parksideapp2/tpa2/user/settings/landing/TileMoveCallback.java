package appfactory.uwp.edu.parksideapp2.tpa2.user.settings.landing;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TileMoveCallback extends ItemTouchHelper.Callback {
    private final TileTouchHelperContract tileTouchHelperContract;

    public TileMoveCallback(TileTouchHelperContract tileTouchHelperContract) {
        this.tileTouchHelperContract = tileTouchHelperContract;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        tileTouchHelperContract.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof LandingTilesAdapter.TileCardViewHolder) {
                LandingTilesAdapter.TileCardViewHolder tileCardViewHolder =
                        (LandingTilesAdapter.TileCardViewHolder) viewHolder;
                tileTouchHelperContract.onRowSelected(tileCardViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof LandingTilesAdapter.TileCardViewHolder) {
            LandingTilesAdapter.TileCardViewHolder tileCardViewHolder =
                    (LandingTilesAdapter.TileCardViewHolder) viewHolder;
            tileTouchHelperContract.onRowClear(tileCardViewHolder);
        }
    }

    public interface TileTouchHelperContract {
        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(LandingTilesAdapter.TileCardViewHolder tileCardViewHolder);

        void onRowClear(LandingTilesAdapter.TileCardViewHolder tileCardViewHolder);

    }

}
