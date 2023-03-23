package appfactory.uwp.edu.parksideapp2.ranger_wellness.dialogs

import androidx.fragment.app.DialogFragment


class AddWorkoutDialog : DialogFragment() { //, AdapterView.OnItemSelectedListener {
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        Log.d("AddWorkoutDialog", "selected workout type position is: $position")
//        workoutTypePosition = position
//    }
//
//    var message: String = ""
//    var title: String = ""
//    var positiveOnClick: () -> Unit = {}
//    var negativeOnClick: () -> Unit = {}
//    var listContents: List<String> = emptyList()
//    var workoutTypePosition: Int = 0
//
//    private val mUserViewModel: UserViewModel by activityViewModels()
//    private val workoutViewModel: WorkoutViewModel by activityViewModels()
//    private val mWorkoutTypeViewModel: WorkoutTypeViewModel by activityViewModels()
//
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(activity)
//
//        val view = activity?.layoutInflater?.inflate(R.layout.dialog_add_workout, null)
//        val amountOfTimeText = view?.findViewById<TextView>(R.id.textInputTime)
//        val spinner = view?.findViewById<Spinner>(R.id.spinnerWorkoutType)
//        spinner?.visibility = when {
//            listContents.isEmpty() -> View.GONE
//            else -> {
//                context?.let {
//                    val typeArray = listContents
//                    val spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, typeArray)
//                    spinner?.adapter = spinnerAdapter
//                    spinner?.setOnItemSelectedListener(this)
//                }
//                View.VISIBLE
//            }
//        }
//
//
//        builder.setView(view)//.setMessage(message)
//                .setTitle(title)
//                .setPositiveButton("Log Workout") { _, _ ->
//                    if (amountOfTimeText!!.text.isBlank()) {
//                        amountOfTimeText.error = "Time can not be empty"
//                    } else {
//                       // val stepFactXTime = amountOfTimeText.text.toString().toInt() * mWorkoutTypeViewModel.types.value!![workoutTypePosition].stepFactor
//                       // mUserViewModel.addUserSubWorkout(stepFactXTime.toString())
//                        positiveOnClick()
//                    }
//                }
//                .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> negativeOnClick() }
//        return builder.create()
//    }
//
//}
//
//// Extension function for creating DialogFragments
//fun AddWorkoutDialog.newInstance(title: String = "", message: String = "", listContents: List<String> = emptyList(),
//                                 positiveOnClick: () -> Unit = {}, negativeOnClick: () -> Unit = {}): AddWorkoutDialog {
//    return AddWorkoutDialog().apply {
//        this.message = message
//        this.title = title
//        this.positiveOnClick = positiveOnClick
//        this.negativeOnClick = negativeOnClick
//        this.listContents = listContents
//    }
}