package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.extensions.snackbar
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.Event
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.EventViewModel
import appfactory.uwp.edu.parksideapp2.utils.EVENT_ID
import kotlinx.android.synthetic.main.fragment_rwevent_detail.*

private const val TAG = "RWEventDetailFragment"

class RWEventDetailFragment : Fragment(R.layout.fragment_rwevent_detail) {

    private val eventViewModel by activityViewModels<EventViewModel>()

    //private val userViewModel by activityViewModels<UserViewModel>()
    private var event: Event? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //disable this button until the event data is loaded.
        buttonSubscribe.apply {
            isEnabled = false
            setBackgroundColor(resources.getColor(R.color.dark_gray))
            setTextColor(resources.getColor(R.color.light_gray))
        }

        /**
         * Grab the event id passed in from the bundle
         */
//        val id = arguments?.getString(EVENT_ID)

        val eventDetails = arguments

//        eventDetails.getString("displayDate")
//        eventDetails.getString("endDate")
//        eventDetails.getString("flag")
//        eventDetails.getInt("goal")
//        eventDetails.getString("id")



        //eventViewModel.setEventById(id)
        /**
         * get the event by id using the firebase function in eventViewModel
         */
        eventTitleText.text = eventDetails?.getString("title")
        eventBodyTextView.text = eventDetails?.getString("description")
        eventDateText.text = eventDetails?.getString("startDate")
        eventLocationText.text = eventDetails?.getString("location")


//        eventViewModel.getEventByID(id)?.observe(this, Observer {
//                eventTitleText.text = it.title
//                eventBodyTextView.text = it.description
//                eventDateText.text = it.startDate
//                // TODO remove this or get data with it
//                eventLocationText.text = "TBD"
//                //use this to see if it belongs to list of subscribed events
//                event = it
//                Log.d(TAG, "event is: $event")
////            Snackbar.make(view, "Event ID: ${it?.name} was selected.", Snackbar.LENGTH_SHORT).show()
//            })


        /**
         * Subscribe or unsubscribe from selected event
         *
         */
        eventViewModel.getSubbedEvents().observe(this, Observer {
            if (!it.contains(event) && event != null) {
                buttonSubscribe.apply {
                    isEnabled = true
                    text = "Subscribe"
                    setBackgroundColor(resources.getColor(R.color.colorAccent))
                    setTextColor(resources.getColor(R.color.white))

                }
                buttonSubscribe.setOnClickListener {
                    eventViewModel.subscribeToEvent(event?.id.toString())
                    it.snackbar("Subscribed to Event")
                    buttonSubscribe.apply {
                        isEnabled = false
                        setBackgroundColor(resources.getColor(R.color.dark_gray))
                        setTextColor(resources.getColor(R.color.light_gray))
                    }
                }
            } else {
                buttonSubscribe.apply {
                    isEnabled = true
                    text = "Unsubscribe"
                    setBackgroundColor(resources.getColor(R.color.colorAccent))
                    setTextColor(resources.getColor(R.color.white))
                }
                buttonSubscribe.setOnClickListener {
                    eventViewModel.unsubscribeToEvent(event?.id.toString())
                    it.snackbar("Unsubscribed.")
                    buttonSubscribe.apply {
                        isEnabled = false
                        setBackgroundColor(resources.getColor(R.color.dark_gray))
                        setTextColor(resources.getColor(R.color.light_gray))
                    }
                }
            }
        })
    }
}
