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
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout.LoggedWorkout


class WorkoutRecyclerViewAdapter(private val onClickListener: (LoggedWorkout) -> Unit) :
        ListAdapter<LoggedWorkout, WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_workout_item, parent, false)
        return WorkoutViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.item = getItem(position)
    }

}

class WorkoutViewHolder(private val view: View, private val onClickListener: (LoggedWorkout) -> Unit) :
        RecyclerView.ViewHolder(view) {

    var item: LoggedWorkout? = null
        set(value) {
            value?.let { newValue ->
                field = newValue
                view.findViewById<CardView>(R.id.cardViewListItem).setOnClickListener { onClickListener(newValue) }
                view.findViewById<TextView>(R.id.textViewWorkoutDate).text = newValue.date
                view.findViewById<TextView>(R.id.textViewWorkoutType).text = newValue.steps
               // view.findViewById<TextView>(R.id.textViewLength).text = "${newValue.amount} minutes"
            }
        }

}

class WorkoutDiffCallback : DiffUtil.ItemCallback<LoggedWorkout>() {

    override fun areItemsTheSame(oldItem: LoggedWorkout, newItem: LoggedWorkout): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LoggedWorkout, newItem: LoggedWorkout): Boolean {
        return oldItem == newItem
    }

}
