package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.extensions.snackbar
import appfactory.uwp.edu.parksideapp2.ranger_wellness.adapters.WorkoutRecyclerViewAdapter
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout.WorkoutViewModel
import kotlinx.android.synthetic.main.fragment_rwworkout_log.*

class RWWorkoutLogFragment : Fragment() {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private lateinit var typeList: List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rwworkout_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter = WorkoutRecyclerViewAdapter { }

        recyclerViewWorkoutLog.apply {
            this.adapter = listAdapter
            this.layoutManager = LinearLayoutManager(this.context)
        }

        /**
         * Get List of logged steps from FB
         */

        workoutViewModel.getLoggedSteps().observe(viewLifecycleOwner, Observer {
            cardViewEmptyList.visibility = when {
                it.isNotEmpty() -> View.GONE
                else -> View.VISIBLE
            }
            listAdapter.submitList(it)
        })


        val spinnerAdapter = ArrayAdapter<String>(spinnerWorkoutType.context, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerWorkoutType.adapter = it
        }
        /**
         * Load the spinner with the list of all workouts
         */
        workoutViewModel.getAllWorkoutsl().observe(viewLifecycleOwner, Observer {
            //Timber.d("workouts type are ${workoutTypeViewModel.value}")

            typeList = it.map {
                type -> "${type.title}, ${type.stepFactor}"
            }
            spinnerAdapter.addAll(typeList)
        })

        buttonLogWorkout.setOnClickListener {
            //Get the string from the current spinner position
            val spinnerValue = spinnerAdapter.getItem(spinnerWorkoutType.selectedItemPosition)
            //the step factor is the last part of the string separated by a comma, split around comma
            val split = spinnerValue?.split(",")
            //because we split around comma we know that position 1 is the stepfactor, trim the leading space
            //and cast to int.
            val stepFact = split?.get(1)?.trimStart()?.toInt()
            //log the steps to FB
            if (editTextWorkoutLength.text.isNotEmpty()) {
                if (stepFact != null && editTextWorkoutLength.text.toString().toInt() <= 9999) {
                    workoutViewModel.logSteps(stepFact, editTextWorkoutLength.text.toString().toInt())
                    it.snackbar("Steps Logged!")
                    spinnerAdapter.clear()
                    editTextWorkoutLength.text.clear()
                }else if(stepFact == null){
                    it.snackbar("Please select a workout from the menu.")

                }else if(editTextWorkoutLength.text.toString().toInt() > 9999){
                    it.snackbar("Error. Exceeded the allowed number of hours")
                }
            }
            spinnerAdapter.addAll(typeList)

        }
    }
}
