package appfactory.uwp.edu.parksideapp2.ranger_wellness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.Event


class EventRecyclerViewAdapter(private val onClickListener: (Event) -> Unit) :
        ListAdapter<Event, EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_event_list, parent, false)
        return EventViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.item = getItem(position)
    }

}

class EventViewHolder(private val view: View, private val onClickListener: (Event) -> Unit) :
        RecyclerView.ViewHolder(view) {

    var item: Event? = null
        set(value) {
            value?.let { newValue ->
                field = newValue
                view.findViewById<CardView>(R.id.cardViewListItem).setOnClickListener { onClickListener(newValue) }
                view.findViewById<TextView>(R.id.textViewWorkoutDate).text = newValue.title
            }
        }

}

// TODO verify this is correct logic for EventDiffCallback
class EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        //return oldItem.version == newItem.version
        return oldItem == newItem
    }

}
