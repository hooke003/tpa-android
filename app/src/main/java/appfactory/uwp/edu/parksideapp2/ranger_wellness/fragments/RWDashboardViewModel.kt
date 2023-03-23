/**
 * RWDashbaordViewModel
 * Reworked by:
 * @author Nick Claffey
 */
package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.TAG

import appfactory.uwp.edu.parksideapp2.utils.SHARED_PREF_FOR_APP
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.lang.Exception

/**
 * RW Dashboard rework
 * @author Nick Claffey
 */

private const val TAG = "RWDashboardViewModel"
class RWDashboardViewModel(application: Application) : AndroidViewModel(application) {


  private var functions = FirebaseFunctions.getInstance()


    //create a mutable LiveData to record the steps
    private val _steps = MutableLiveData<String>()
    val steps: LiveData<String>
        get() = _steps

    //create a mutable LiveData to record Today's steps
    private val _todaysteps = MutableLiveData<String>()
    val todaysteps: LiveData<String>
        get() = _todaysteps



    /**
     * This is the firebase function to get global step goal
     * from the server
     */
    private fun getglobalgoal(): Task<Any> {
        return functions
                .getHttpsCallable("getglobalgoal")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "globalGoal: $result")
                    result
                }
    }


    /**
     * This function calls the firebase function to get global step goal,
     * It implements an addOnCompleteListener to handle the data
     * and return it to the dashbaord as LiveData
     */
    fun getsteps(): LiveData<String>{
        getglobalgoal()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }
                        // [START_EXCLUDE]
                        Log.w(TAG, "addMessage:onFailure", e)
                        //showSnackbar("An error occurred.")
                        return@OnCompleteListener
                        // [END_EXCLUDE]
                    }
                        // [START_EXCLUDE]
                        val result = task.result
                        Log.d(TAG, "ans: ${result}")
                        val tempData = result as HashMap<String, String>
                        _steps.postValue(tempData["globalGoal"].toString())

                    // [END_EXCLUDE]
                })

        return steps

    }


    /**
     * Logs steps and updates firebase
     */
    fun logpedoSteps(steps: Int): Task<Any>{

        val data = hashMapOf(
                "steps" to steps
        )

        return functions
                .getHttpsCallable("updatepedo")
                .call(data)
                .continueWith(){task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "Pedo steps: $result")
                    result

                }

    }

    /**
     * Get todays steps from FB
     */

    private fun getTodaySteps(): Task<Any> {
        return functions
                .getHttpsCallable("getdaysteps")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "getdaysteps(): $result")
                    result
                }
    }


    /**
     * This will return the Days steps back to
     * the dashboard
     */
    fun getsDayteps(): LiveData<String>{
        getTodaySteps()
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }
                        // [START_EXCLUDE]
                        Log.w(TAG, "addMessage:onFailure", e)
                        //showSnackbar("An error occurred.")
                        return@OnCompleteListener
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE]
                    val result = task.result
                    Log.d(TAG, "DaySteps: ${result}")
                    val tempData = result as HashMap<String, String>
                    val todaysSteps = tempData["steps"].toString().toInt() + tempData["pedo"].toString().toInt()
                    _todaysteps.postValue(todaysSteps.toString())

                    // [END_EXCLUDE]
                })

        return todaysteps

    }

}