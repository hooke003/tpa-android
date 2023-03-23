package appfactory.uwp.edu.parksideapp2.ranger_wellness.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.EventViewModel

class EventDetailDialog : DialogFragment() {

//    var title: String = ""
//    var positiveOnClick: () -> Unit = {}
//    var negativeOnClick: () -> Unit = {}
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder = AlertDialog.Builder(activity)
//
//        val view = activity?.layoutInflater?.inflate(R.layout.dialog_event_detail, null)
//        val eventViewModel = activity?.let {
//            ViewModelProviders.of(it).get(EventViewModel::class.java)
//        } ?: throw IllegalStateException()
//        val textViewDescription = view?.findViewById<TextView>(R.id.textViewDescription)
//        val textViewStartDate = view?.findViewById<TextView>(R.id.eventDateText2)
//        val textViewLocation = view?.findViewById<TextView>(R.id.eventLocationText2)
//        eventViewModel.getSelectedEvent.observe(this, Observer {
//            textViewDescription?.text = it.description
//            textViewStartDate?.text = it.startDate
//            textViewLocation?.text = "TBD"
//        })
//
//        builder.setView(view)//.setMessage(message)
//                .setTitle(title)
//                .setPositiveButton("Join") { _, _ ->
//                    positiveOnClick()
//                }
//                .setNegativeButton("Go Back") { _, _ -> negativeOnClick() }
//        return builder.create()
//    }
//}
//
//fun EventDetailDialog.newInstance(title: String = "", positiveOnClick: () -> Unit = {}, negativeOnClick: () -> Unit = {}): EventDetailDialog {
//    return EventDetailDialog().apply {
//        this.title = title
//        this.positiveOnClick = positiveOnClick
//        this.negativeOnClick = negativeOnClick
//    }
}