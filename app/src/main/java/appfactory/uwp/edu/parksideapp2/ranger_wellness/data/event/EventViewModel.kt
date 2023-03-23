package appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

/**
 * EventViewModeel Rework
 * @author Nick Claffey
 */

const val TAG = "EventViewModel"

class EventViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Going to use this, probbaly get rid of the db below
     */
    var functions = FirebaseFunctions.getInstance()


    //create a mutable list of the events
    private val _eventList = MutableLiveData<List<Event>>()
    val eventList: LiveData<List<Event>>
        get() = _eventList

    //create a mutable list of subscribed events
    private val _subevents = MutableLiveData<List<Event>>()
    val subevents: LiveData<List<Event>>
        get() = _subevents

    //create a mutable Event
    private val _eventbyid = MutableLiveData<Event>()
    val eventbyid: LiveData<Event>
        get() = _eventbyid


    /**
     * This function retrieves the events from the database
     * and adds them to a list of live data to be observed in our other fragments
     *
    //     */
    private fun getAllEvents(): Task<Any> {
        return functions
                .getHttpsCallable("getallevents")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as ArrayList<Any>

                    Log.d("GetAllEvents(): ", "All Events: $result")
                    result
                }

    }


    /**
     * Calls the function that gets all the events from Firebase and returns them to
     * The calling fragment
     */
    fun getEventListFromFB(): LiveData<List<Event>> {
        val eventObjList = mutableListOf<Event>()
        getAllEvents()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    }else{
                        val result = task.result as ArrayList<HashMap<String, String>>
                        Log.d(TAG, "All Events: $result")
                        for(map in result){
                            val event = Event(map["description"].toString(),map["displayDate"].toString(),
                                    map["endDate"].toString(), map["flag"].toString(),
                                    map["goal"].toString().toInt(),
                                    map["id"].toString(), map["location"].toString(),
                                    map["startDate"].toString(), map["title"].toString())
                            eventObjList.add(event)
                        }
                    }
                    _eventList.postValue(eventObjList)
                }
        return eventList
    }


     /**
     * Firebase function to retrieve the users subscribed events
     */
    private fun getSubscribedEvents(): Task<Any> {

        return functions
                .getHttpsCallable("geteflagarr")
                .call()
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as ArrayList<Any>

                    Log.d("GetSubEvents(): ", "subEvents: $result")
                    result
                }
    }

    /**
     * Calls the firebsae function to retrieve the users subscriebd
     * events and returns them to the RWDashboard fragment
     */
    fun getSubbedEvents(): LiveData<List<Event>>{
        val eventObjList = mutableListOf<Event>()
        getSubscribedEvents()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    }else{
                        val result = task.result as ArrayList<HashMap<String, String>>
                        Log.d(TAG, "SubbedEvents: $result")
                        for(map in result){
                            val event = Event(map["description"].toString(),map["displayDate"].toString(),
                                    map["endDate"].toString(), map["flag"].toString(),
                                    map["goal"].toString().toInt(),
                                    map["id"].toString(), map["location"].toString(),
                                    map["startDate"].toString(), map["title"].toString())
                            eventObjList.add(event)
                        }


                    }
                    _subevents.postValue(eventObjList)
                }
        return subevents
    }

    /**
     * Firebase function to Get an evenn by
     * id from firebase.
     */
    private fun getEventByIDFromFB(id: String): Task<Any> {
        val data = hashMapOf(
                "id" to id)

        return functions
                .getHttpsCallable("geteventbyid")
                .call(data)
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "EventByID: $result")
                    result
                }
    }

    /**
     * Calls the firebsae function to retrieve an Event by ID and return it to the
     * RWEventDetailFragment
     */
    fun getEventByID(id: String): LiveData<Event>?{
        getEventByIDFromFB(id)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val e = task.exception
                        if (e is FirebaseFunctionsException) {
                            val code = e.code
                            val details = e.details
                        }

                    }
                        val result = task.result as HashMap<String, String>
                        Log.d(TAG, "eventByID: $result")
                            val event = Event(result["description"].toString(),result["displayDate"].toString(),
                                    result["endDate"].toString(), result["flag"].toString(),
                                    result["goal"].toString().toInt(),
                                    result["id"].toString(), result["location"].toString(),
                                    result["startDate"].toString(), result["title"].toString())
                    _eventbyid.postValue(event)
                })
        return eventbyid
    }

    /**
     * Firebase function to
     * Subscribe User to a new event
     */
    fun subscribeToEvent(id: String): Task<Any>{
        val data = hashMapOf(
                "id" to id
        )

        return functions
                .getHttpsCallable("addeflag")
                .call(data)
                .continueWith(){task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "SubScribe To: $result")
                    result

                }
    }

    fun unsubscribeToEvent(id: String): Task<Any>{
        val data = hashMapOf(
                "id" to id
        )

        return functions
                .getHttpsCallable("removeeflag")
                .call(data)
                .continueWith(){task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as Any

                    Log.d(TAG, "SubScribe To: $result")
                    result

                }
    }
}

