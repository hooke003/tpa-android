package appfactory.uwp.edu.parksideapp2.ranger_wellness.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout.WorkoutViewModel

class WorkoutDetailDialog : DialogFragment() {

//    var title: String = ""
//    var positiveOnClick: () -> Unit = {}
//    var negativeOnClick: () -> Unit = {}
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(activity)
//        val view = activity?.layoutInflater?.inflate(R.layout.dialog_workout_detail, null)
//
//        val workoutViewModel = activity?.let {
//            ViewModelProviders.of(it).get(WorkoutViewModel::class.java)
//        } ?: throw IllegalStateException()
//
//        builder.setView(view)
//                .setTitle(title)
//                .setPositiveButton("Update") {_, _ -> positiveOnClick()}
//                .setNegativeButton("Go Back") {_, _ -> negativeOnClick()}
//
//        return builder.create()
//    }
}

//fun WorkoutDetailDialog.newInstance(title: String = "", positiveOnClick: () -> Unit = {}, negativeOnClick: () -> Unit = {}): EventDetailDialog {
//    return EventDetailDialog().apply {
//        this.title = title
//        this.positiveOnClick = positiveOnClick
//        this.negativeOnClick = negativeOnClick
//    }
//}