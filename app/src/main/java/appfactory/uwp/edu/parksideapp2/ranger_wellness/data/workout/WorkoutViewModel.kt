package appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

/**
 * WorkoutVieModel rework
 * @author Nick Claffey
 */
private const val TAG = "WorkoutViewModel"

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    var functions = FirebaseFunctions.getInstance()

    // WorkManager
    private val workManager: WorkManager by lazy { WorkManager.getInstance(application) }

    //val workouts: LiveData<List<Workout>>
    private lateinit var types: List<WorkoutType>

    /**
     * Mutable Live data of the total steps towards
     * the global goal
     */
    private val _totalsteps = MutableLiveData<String>()
    val totalsteps: LiveData<String>
        get() = _totalsteps

    /**
     * Mutable Live data for the List of
     * all workouts
     */
    private val _allworkouts = MutableLiveData<List<WorkoutType>>()
    val allworkouts: LiveData<List<WorkoutType>>
        get() = _allworkouts

    /**
     * Mutable live data for the list of
     * logged workouts
     */
    private val _loggedworkouts = MutableLiveData<List<LoggedWorkout>>()
    val loggedworkouts: LiveData<List<LoggedWorkout>>
        get() = _loggedworkouts


    /**
     * Firebase function to retrieve the users total steps
     * towards the global goal
     */
    private fun getTotalStepsFromFB(): Task<Any> {

        return functions
                .getHttpsCallable("gettotalsteps")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "TotalSteps: $result")
                    result
                }
    }

    /**
     * Function that returns the users total steps
     * toward the global goal
     * to the RWDashbaord
     */

    fun getStepsTowardsGoal(): LiveData<String> {
        getTotalStepsFromFB()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    } else {
                        val result = task.result as String
                        Log.d(TAG, "onComplete Total steps: $result")
                        _totalsteps.postValue(result)

                    }


                })
        return totalsteps

    }

    /**
     * Firebase function to retrieve the users total steps
     * towards the global goal
     */
    private fun getAllWorkoutsFB(): Task<Any> {

        return functions
                .getHttpsCallable("getallworkouts")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "All Workouts: $result")
                    result
                }
    }

    /**
     * Function that returns the list
     * of workouts to the RWWorkoutlogfragment
     */

    fun getAllWorkoutsl(): LiveData<List<WorkoutType>> {
        val workouts = mutableListOf<WorkoutType>()
        getAllWorkoutsFB()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    } else {
                        val result = task.result as ArrayList<HashMap<String, String>>
                        Log.d(TAG, "onComplete workouts: $result")
                        for (workout in result) {
                            val workoutType = WorkoutType(workout["id"], workout["uuid"].toString().toInt(), workout["title"],
                                    workout["step_factor"].toString().toInt())
                            workouts.add(workoutType)
                        }

                        _allworkouts.postValue(workouts)

                    }


                })
        return allworkouts

    }

    /**
     * Logs steps and updates firebase
     */
    fun logSteps(stepVal1: Int, stepVal2: Int): Task<Any> {
        val totalSteps = stepVal1 * stepVal2

        val data = hashMapOf(
                "steps" to totalSteps
        )

        return functions
                .getHttpsCallable("logsteps")
                .call(data)
                .continueWith() { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "Logged steps: $result")
                    result

                }

    }

    /**
     * Firebase function to retrieve the users total steps
     * towards the global goal
     */
    private fun getLoggedStepsFB(): Task<Any> {

        return functions
                .getHttpsCallable("getloggedsteps")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "Logged workouts: $result")
                    result
                }
    }

    /**
     * Function that returns the list
     * of workouts to the RWWorkoutlogfragment
     */

    fun getLoggedSteps(): LiveData<List<LoggedWorkout>> {
        val workouts = mutableListOf<LoggedWorkout>()
        getLoggedStepsFB()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    } else {
                        val result = task.result as ArrayList<HashMap<String, String>>
                        Log.d(TAG, "onComplete workouts: $result")
                        for (workout in result) {
                            val loggedWorkout = LoggedWorkout(workout["date"].toString(), workout["steps"].toString())
                            workouts.add(loggedWorkout)
                        }

                        _loggedworkouts.postValue(workouts)

                    }


                })
        return loggedworkouts

    }

}