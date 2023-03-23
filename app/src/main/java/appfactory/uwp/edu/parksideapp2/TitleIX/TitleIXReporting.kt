package appfactory.uwp.edu.parksideapp2.TitleIX

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import appfactory.uwp.edu.parksideapp2.R

/**
 * @author Zoli
 * @version 1.0
 * @since 12.21.2021
 * TitleIX reporting
 */

//https://www.uwp.edu/live/offices/studentaffairs sexual-misconduct/reporting-sexual-misconduct.cfm

class TitleIXReporting : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_title_ix_reporting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val card = getView()?.findViewById<CardView>(R.id.reporting_misconduct)

        // Directly takes to webpage, should be its own webview within the app; idk how to do that. Rushing to finish
        card?.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.uwp.edu/live/offices/studentaffairs/sexual-misconduct/reporting-sexual-misconduct.cfm"));
            startActivity(intent);
            // Do some work here
        })
    }

}