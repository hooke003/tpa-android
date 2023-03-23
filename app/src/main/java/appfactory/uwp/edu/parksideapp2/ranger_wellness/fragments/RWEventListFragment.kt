package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.ranger_wellness.adapters.EventRecyclerViewAdapter
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.EventViewModel
import appfactory.uwp.edu.parksideapp2.utils.EVENT_ID
import kotlinx.android.synthetic.main.fragment_rwevent_list.*

/**
 * Grabs events from firebase
 * Nick Claffey
 */
private const val TAG = "RWEventListFragment"

class RWEventListFragment : Fragment() {

    private val eventViewModel by activityViewModels<EventViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rwevent_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EventRecyclerViewAdapter {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            eventViewModel.setEventById(it.eventId)
            Log.d(TAG, "the event id is: ${it.id}")
            val eventDetails = Bundle()

            eventDetails.putString("description", it.description)
            eventDetails.putString("displayDate", it.displayDate)
            eventDetails.putString("endDate", it.endDate)
            eventDetails.putString("flag", it.flag)
            eventDetails.putInt("goal", it.goal)
            eventDetails.putString("id", it.id)
            eventDetails.putString("location", it.location)
            eventDetails.putString("startDate", it.startDate)
            eventDetails.putString("title", it.title)

            val rwEventDetailFragment = RWEventDetailFragment()
            rwEventDetailFragment.arguments = eventDetails
            fragmentTransaction
                    .replace(
                            R.id.tpa2_landing_fragment_container,
                            rwEventDetailFragment,
                            "RWEventDetailFragment"
                    )
                    .addToBackStack(null)
                    .commit()
//            findNavController().navigate(R.id.action_global_RWEventDetailFragment, bundle)
        }

        val llm = LinearLayoutManager(recycler_view_event_list.context)
        recycler_view_event_list.apply {
            this.adapter = adapter
            this.layoutManager = llm
        }

        eventViewModel.getEventListFromFB().observe(viewLifecycleOwner, Observer {
            cardViewEmptyList.visibility = when {
                it.isEmpty() -> View.VISIBLE
                else -> View.GONE
            }
            adapter.submitList(it)
        })
    }
}