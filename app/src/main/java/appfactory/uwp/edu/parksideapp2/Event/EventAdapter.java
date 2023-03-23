package appfactory.uwp.edu.parksideapp2.Event;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import appfactory.uwp.edu.parksideapp2.Detail.EventDetailActivity;
import appfactory.uwp.edu.parksideapp2.Models.EventObj;
import appfactory.uwp.edu.parksideapp2.R;

/**
 * Created by kyluong09 on 3/27/18.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    //
    private Context context;
    //
    private ArrayList<EventObj> eventList;

    public EventAdapter(Context context, ArrayList<EventObj> eventList){
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.eventTitle.setText(Html.fromHtml(eventList.get(position).getTitle()));
        holder.eventDescription.setText(Html.fromHtml(eventList.get(position).getDescription()));
        // event date
        holder.eventDate.setText(Html.fromHtml(eventList.get(position).getEventDate().toString()));
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context,EventDetailActivity.class);
            intent.putExtra("event_title",eventList.get(position).getTitle());
            intent.putExtra("event_description",eventList.get(position).getDescription());

            // format event date
            String event_date = DateFormat.getDateInstance(DateFormat.LONG).format(eventList.get(position).getEventDate());
            intent.putExtra("event_date", event_date);

            /** formatting date to only use day and month NO year is included*/
            SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM);
            sdf.applyPattern(sdf.toPattern().replaceAll(
                    "([^\\p{Alpha}']|('[\\p{Alpha}]+'))*y+([^\\p{Alpha}']|('[\\p{Alpha}]+'))*",
                    ""));

            // format event start date
            String startDate=  sdf.format(eventList.get(position).getEventDate());
            intent.putExtra("event_start_date", startDate);

            // start time and end time
            intent.putExtra("event_start_time", eventList.get(position).getStartTime());
            intent.putExtra("event_end_time", eventList.get(position).getEndTime());

            // location
            intent.putExtra("event_location", eventList.get(position).getLocation());

            // This is unit test: All form of contact
            String name01 = eventList.get(position).getContactName();
            String email01 = eventList.get(position).getContactEmail();
            String phone01 = eventList.get(position).getContactPhone();

            intent.putExtra("event_contact_name", name01);
            intent.putExtra("event_contact_email", email01);
            intent.putExtra("event_contact_phone", phone01);
            intent.putExtra("event_id", eventList.get(position).get_id());

            context.startActivity(intent);
        });

        try{
            String dateString = new SimpleDateFormat("MM-dd-yyyy").format(eventList.get(position).getEventDate());
            holder.eventDate.setText(dateString);
        } catch(Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // UI
        private CardView cardView;
        private TextView eventTitle;
        private TextView eventDescription;
        private TextView eventDate;
        private TextView eventTime;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.event_card_view);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
        }
    }
}



