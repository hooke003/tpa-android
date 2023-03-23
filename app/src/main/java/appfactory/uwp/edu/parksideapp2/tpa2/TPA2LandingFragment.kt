package appfactory.uwp.edu.parksideapp2.tpa2

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import appfactory.uwp.edu.parksideapp2.ComputerLab.LabFragment
import appfactory.uwp.edu.parksideapp2.Event.EventsActivity
import appfactory.uwp.edu.parksideapp2.IndoorNav.IndoorNavMain
import appfactory.uwp.edu.parksideapp2.Map.MainMap
import appfactory.uwp.edu.parksideapp2.News.NewsActivity
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.TitleIX.TitleIXMain
import appfactory.uwp.edu.parksideapp2.ranger_wellness.RWDashboardActivity
import appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments.RWInfoFragment
import appfactory.uwp.edu.parksideapp2.tpa2.auth.TPA2LoginActivity
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.*


/**
 * This class inflates the landing screen and contains the logic for transitions.
 *
 * @author Allen E Rocha
 * @version 1.0
 * @since 1.2.16
 */
class TPA2LandingFragment : Fragment() {
    private val TAG = "TPA2LandingFragment"

    //<editor-fold desc="CardViews">
    private lateinit var tileCardViews: ArrayList<CardView>
    private lateinit var cardViewIcons: ArrayList<ImageView>
    private lateinit var cardViewTitles: ArrayList<TextView>

    //</editor-fold>
    private var messageOfTheDayCardView: CardView? = null
    private var messageOfTheDayTitleTextView: TextView? = null
    private var messageOfTheDayBodyTextView: TextView? = null
    private var motdTitle: String? = ""
    private var motdBody: String? = ""
    private fun initUI(view: View) {
        //<editor-fold desc="CardView Assignments">
        tileCardViews = ArrayList()
        tileCardViews.add(view.findViewById(R.id.cardView00))
        tileCardViews.add(view.findViewById(R.id.cardView01))
        tileCardViews.add(view.findViewById(R.id.cardView10))
        tileCardViews.add(view.findViewById(R.id.cardView11))
        tileCardViews.add(view.findViewById(R.id.cardView20))
        tileCardViews.add(view.findViewById(R.id.cardView21))
        tileCardViews.add(view.findViewById(R.id.cardView30))
        tileCardViews.add(view.findViewById(R.id.cardView31))
        tileCardViews.add(view.findViewById(R.id.cardView40))

        cardViewIcons = ArrayList()
        cardViewIcons.add(view.findViewById(R.id.cardView00Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView01Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView10Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView11Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView20Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView21Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView30Icon))
        cardViewIcons.add(view.findViewById(R.id.cardView31Icon));
        cardViewIcons.add(view.findViewById(R.id.cardView40Icon));

        cardViewTitles = ArrayList()
        cardViewTitles.add(view.findViewById(R.id.cardView00Title))
        cardViewTitles.add(view.findViewById(R.id.cardView01Title))
        cardViewTitles.add(view.findViewById(R.id.cardView10Title))
        cardViewTitles.add(view.findViewById(R.id.cardView11Title))
        cardViewTitles.add(view.findViewById(R.id.cardView20Title))
        cardViewTitles.add(view.findViewById(R.id.cardView21Title))
        cardViewTitles.add(view.findViewById(R.id.cardView30Title))
        cardViewTitles.add(view.findViewById(R.id.cardView31Title));
        cardViewTitles.add(view.findViewById(R.id.cardView40Title));

        //</editor-fold>
        messageOfTheDayCardView = view.findViewById(R.id.messageOfTheDayCardView)
        messageOfTheDayTitleTextView = view.findViewById(R.id.messageOfTheDayTitleTextView)
        messageOfTheDayBodyTextView = view.findViewById(R.id.messageOfTheDayBodyTextView)
        assignCardViewProperties()
        val tpa2LandingScrollView = view.findViewById<ScrollView>(R.id.tpa2LandingScrollView)
        tpa2LandingScrollView.isVerticalScrollBarEnabled = false
        tpa2LandingScrollView.isHorizontalScrollBarEnabled = false
    }

    private fun assignCardViewProperties() {
        for (index in UserConstants.LANDING_TILE_CARDS.indices) {
            Timber.d("Index: %d", index)
            Timber.d("Size: %d", UserConstants.LANDING_TILE_CARDS.size)
            // Return if index is greater than the size of the arraylist
            if (index == UserConstants.LANDING_TILE_CARDS.size) { return }
            val tileCard = UserConstants.LANDING_TILE_CARDS[index]
            Log.d(TAG, "***** Reached Landing Fragment - Loop")
            val currentCard = tileCardViews!![index]
            val currentIcon = cardViewIcons!![index]
            val currentTitle = cardViewTitles!![index]
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            when (tileCard.tileCardTextView.lowercase(Locale.getDefault())) {
                "maps" -> {
                    Log.d(TAG, "***** Reached Landing Fragment - maps")
                    with(currentCard) { setCardBackgroundColor(resources.getColor(R.color.landingMapCardView)) }
                    currentIcon.setImageResource(R.drawable.ic_maps)
                    currentTitle.text = "Map"
                    currentCard.setOnClickListener { view: View? ->
                        fragmentTransaction.replace(
                            R.id.tpa2_landing_fragment_container,
                            MainMap(),
                            "MainMap"
                        )
                            .addToBackStack(null)
                            .commit()
                    }
                }
                "ranger wellness" -> {
                    Log.d(TAG, "***** Reached Landing Fragment - RW")
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingRWCardView))
                    currentIcon.setImageResource(R.drawable.ic_bigbear)
                    currentTitle.text = "Ranger Wellness"
                    currentCard.setOnClickListener { view: View? ->
                        if (UserConstants.LOGGED_IN) {
                            if (UserConstants.RW_AUTHORIZED) {
                                val intent =
                                    Intent(requireActivity(), RWDashboardActivity::class.java)
                                startActivity(intent)
                            } else {
                                fragmentTransaction.replace(
                                    R.id.tpa2_landing_fragment_container,
                                    RWInfoFragment(),
                                    "RWInfoFragment"
                                )
                                    .addToBackStack(null)
                                    .commit()
                            }
                        } else {
                            userNotLoggedInPopUp()
                        }
                    }
                }
                "news" -> {
                    Log.d(TAG, "***** Reached Landing Fragment - news")
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingNewsCardView))
                    currentIcon.setImageResource(R.drawable.news_icon)
                    currentTitle.text = "News"
                    currentCard.setOnClickListener { view: View? ->
                        val intent = Intent(requireActivity(), NewsActivity::class.java)
                        startActivity(intent)
                    }
                }
                "navigate" -> {
                    Log.d(TAG, "***** Reached Landing Fragment - navigate")
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingNavigateCardView))
                    currentIcon.setImageResource(R.drawable.ic_naviagte)
                    currentTitle.text = "Navigate"
                    currentCard.setOnClickListener { view: View? ->
                        val launchIntent =
                            requireActivity().packageManager.getLaunchIntentForPackage(
                                "com.eab.se"
                            )
                        if (launchIntent != null) {
                            startActivity(launchIntent)
                        } else {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=com.eab.se")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.eab.se")
                                    )
                                )
                            }
                        }
                    }
                }
                "events" -> {
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingEventsCardView))
                    currentIcon.setImageResource(R.drawable.ic_events2)
                    currentTitle.text = "Events"
                    currentCard.setOnClickListener { view: View? ->
                        val intent = Intent(requireActivity(), EventsActivity::class.java)
                        startActivity(intent)
                    }
                }
                "eaccounts" -> {
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingeAccountsCardView))
                    currentIcon.setImageResource(R.drawable.ic_eaccounts)
                    currentTitle.text = "eAccounts"
                    currentCard.setOnClickListener { view: View? ->
                        val launchIntent =
                            requireActivity().packageManager.getLaunchIntentForPackage(
                                "com.blackboard.transact.android.v2"
                            )
                        if (launchIntent != null) {
                            startActivity(launchIntent)
                        } else {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=com.blackboard.transact.android.v2")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.blackboard.transact.android.v2")
                                    )
                                )
                            }
                        }
                    }
                }
                "computer labs" -> {
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingComputerLabsCardView))
                    currentIcon.setImageResource(R.drawable.ic_computer)
                    currentTitle.text = "Computer Labs"
                    currentCard.setOnClickListener { view: View? ->
                        fragmentTransaction.replace(
                            R.id.tpa2_landing_fragment_container,
                            LabFragment(),
                            "LabFragment"
                        )
                        .addToBackStack(null)
                        .commit()
                    }
                }
                "title ix" -> {
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingTitleIXCardView))
                    currentIcon.setImageResource(R.mipmap.ic_titleix)
                    currentTitle.text = "Title IX"
                    currentCard.setOnClickListener { view: View? ->
                        val intent = Intent(requireActivity(), TitleIXMain::class.java)
                        startActivity(intent)
                    }
                }
                "indoor navigation" -> {
                    currentCard.setCardBackgroundColor(resources.getColor(R.color.landingIndoorNavCardView))
                    currentIcon.setImageResource(R.drawable.ic_baseline_explore_24)
                    currentTitle.text = "Indoor Navigation"
                    currentCard.setOnClickListener {
                        val intent = Intent(requireActivity(), IndoorNavMain::class.java)
                        startActivity(intent)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun userNotLoggedInPopUp() {
        val notLoggedInPopUp = AlertDialog.Builder(requireContext())
        notLoggedInPopUp
            .setTitle(
                Html.fromHtml(
                    String.format(
                        "<font color='#000000'>%s</font>",
                        "User not logged in."
                    )
                )
            )
            .setMessage(
                Html.fromHtml(
                    String.format(
                        "<font color='#000000'>%s</font>",
                        "You must be logged in to use this feature."
                    )
                )
            )
            .setPositiveButton("Login") { dialogInterface: DialogInterface?, i: Int ->
                val intent = Intent(requireContext(), TPA2LoginActivity::class.java)
                startActivity(intent)
            }
            .setNeutralButton("Ok") { dialog: DialogInterface?, which: Int -> }
            .setIcon(R.drawable.ic_bigbear)
        notLoggedInPopUp.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imm =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(requireContext())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tpa2_landing, container, false)
        initUI(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("misc").document("otherItems")
        docRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    motdTitle = document.getString("motdTitle")
                    motdBody = document.getString("motdBody")
                    if (motdTitle!!.length > 0 && motdBody!!.length > 0) {
                        messageOfTheDayTitleTextView!!.text = motdTitle
                        messageOfTheDayBodyTextView!!.text = motdBody
                    }
                } else {
                    Timber.d("No such document")
                    messageOfTheDayCardView!!.visibility = View.GONE
                }
            }
        }
    }
}