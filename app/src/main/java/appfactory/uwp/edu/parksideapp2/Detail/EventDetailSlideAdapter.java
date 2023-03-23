package appfactory.uwp.edu.parksideapp2.Detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import appfactory.uwp.edu.parksideapp2.Models.EventObj;
import appfactory.uwp.edu.parksideapp2.R;

public class EventDetailSlideAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<EventObj> eventList;

    public EventDetailSlideAdapter(Context context, ArrayList eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ScrollView) object;
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //Variable
        String allEventTime;
        View view = LayoutInflater.from(context).inflate(R.layout.event_detail_view, container, false);

        TextView event_title = (TextView) view.findViewById(R.id.event_title_detail);
        TextView event_description = (TextView) view.findViewById(R.id.event_description_detail);
        TextView event_date = (TextView) view.findViewById(R.id.event_date);
        TextView event_time = (TextView) view.findViewById(R.id.event_time);
        TextView event_location = (TextView) view.findViewById(R.id.event_location);
        TextView event_contact = (TextView) view.findViewById(R.id.event_contact);
        FloatingActionButton mShareEventButton = (FloatingActionButton) view.findViewById(R.id.shareEventButton);


        // event start time
        final SimpleDateFormat sdf = new SimpleDateFormat("k:mm");
        String startTime = Html.fromHtml(eventList.get(position).getStartTime()).toString();
        String endTime = Html.fromHtml(eventList.get(position).getEndTime()).toString();
        String start = "";
        String end = "";
        try {
            final Date startTimeObj = sdf.parse(startTime);
            final Date endTimeObj = sdf.parse(endTime);
            start = new SimpleDateFormat("h:mm a").format(startTimeObj);
            end = new SimpleDateFormat("h:mm a").format(endTimeObj);
        } catch (final ParseException | NullPointerException e) {
            e.printStackTrace();
        }

        if (startTime.length() > 0 && endTime.length() == 0) {
            allEventTime = start;
            event_time.setText(allEventTime);
        } else if (startTime.length() > 0 && endTime.length() > 0) {
            allEventTime = start + " to " + end;
            event_time.setText(allEventTime);
        } else {
            allEventTime = "All Day";
            event_time.setText(allEventTime);
        }

        // event location
        String eventLocation = Html.fromHtml(eventList.get(position).getLocation()).toString();
        if(!eventLocation.equals("")){
            event_location.setText(eventLocation);
        }else{
            event_location.setText("N/A");
        }

        // All form of contact
        String contactName = Html.fromHtml(eventList.get(position).getContactName()).toString();
        String contactEmail = Html.fromHtml(eventList.get(position).getContactEmail()).toString();
        String contactPhone = Html.fromHtml(eventList.get(position).getContactPhone()).toString();
        String wholeContact = contactName + "\n" + contactEmail + "\n" + contactPhone;
        event_contact.setText(wholeContact);
        event_title.setText(Html.fromHtml(eventList.get(position).getTitle()));

        // set description
        event_description.setText(Html.fromHtml(eventList.get(position).getDescription()));

        // event date
        String eventDate = "Start: " + Html.fromHtml(eventList.get(position).getEventDateString());
        event_date.setText(eventDate);

        container.addView(view);

        // share event content
        final String eventTitle = eventList.get(position).getTitle();
        final String eventContent = "\nEvent date: " + eventDate
                + "\nEvent time: " + allEventTime
                + "\nEvent location: " + eventLocation
                + "\nEvent description: " + eventLocation;
        mShareEventButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, eventTitle);
            sendIntent.putExtra(Intent.EXTRA_TEXT, eventContent);
            sendIntent.setType("text/plain");
            sendIntent = sendIntent.createChooser(sendIntent, "share Parkside news");
            context.startActivity(sendIntent);
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ScrollView) object);
    }
}
