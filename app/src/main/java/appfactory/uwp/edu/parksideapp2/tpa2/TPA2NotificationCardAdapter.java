package appfactory.uwp.edu.parksideapp2.tpa2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import appfactory.uwp.edu.parksideapp2.R;

/**
 * Adapter for the RecyclerView containing the notification cards.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
public class TPA2NotificationCardAdapter extends RecyclerView.Adapter<TPA2NotificationCardAdapter.NotificationCardViewHolder> {
    private ArrayList<TPA2NotificationCard> TPA2NotificationCards;

    public TPA2NotificationCardAdapter(ArrayList<TPA2NotificationCard> TPA2NotificationCards) {
        this.TPA2NotificationCards = TPA2NotificationCards;
    }

    @NonNull
    @Override
    public NotificationCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tpa2_notification_item, parent, false);
        return new NotificationCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationCardViewHolder holder, int position) {
        TPA2NotificationCard currentCard = TPA2NotificationCards.get(position);
        holder.notificationCardViewTitle.setText(currentCard.getTitle());
        holder.notificationCardViewBody.setText(currentCard.getBody());
        holder.notificationCardViewAge.setText(currentCard.getTimeStamp());
        holder.notificationTopicIcon.setImageResource(currentCard.getIcon());
    }

    @Override
    public int getItemCount() {
        return TPA2NotificationCards.size();
    }

    public static class NotificationCardViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationCardViewTitle;
        public TextView notificationCardViewBody;
        public TextView notificationCardViewAge;
        public ImageView notificationTopicIcon;

        public NotificationCardViewHolder(View view) {
            super(view);
            notificationCardViewTitle = view.findViewById(R.id.notificationCardViewTitle);
            notificationCardViewBody = view.findViewById(R.id.notificationCardViewBody);
            notificationCardViewAge = view.findViewById(R.id.notificationCardViewAge);
            notificationTopicIcon = view.findViewById(R.id.notificationTopicIcon);
        }
    }
}
