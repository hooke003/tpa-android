package appfactory.uwp.edu.parksideapp2

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import appfactory.uwp.edu.parksideapp2.ComputerLab.AsyncTaskLab
import appfactory.uwp.edu.parksideapp2.Event.AsyncTaskEvent
import appfactory.uwp.edu.parksideapp2.News.AsyncTaskNews
import appfactory.uwp.edu.parksideapp2.SSOAuth.SSOLoginScreenFragment
import appfactory.uwp.edu.parksideapp2.ranger_restart.updates.RecentUpdateCard
import appfactory.uwp.edu.parksideapp2.tpa2.auth.TPA2LoginActivity
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants
import appfactory.uwp.edu.parksideapp2.utils.CheckNetStatus
import appfactory.uwp.edu.parksideapp2.utils.IN_APP_UPDATE_REQUEST
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import org.xml.sax.SAXException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.ParserConfigurationException

/**
 *
 */
class LoadingActivity : AppCompatActivity() {
    private val TAG = "LoadingActivity"
    private val loadingTime = 5000

    // onCreate what is onCreate? It is called just after the activity is initialized. After
    // onCreate executes, the activity is considered created. So in other words, all the code in onCreate will execute and
    // after all the code has executed, the activity is considered created. Which from my understanding, created means
    // The UI shows on screen.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // calling load function from user constants will either set up the default app preferences and or load pre-set ones for each
        // users subscriptions, tile positions, preferences and other info.
        UserConstants.load(this)

        // if connected to network (why do they need to call this?) then call generate updates to get any updates
        if (CheckNetStatus.isConnected(this)) {
            Log.d(TAG, "Network reachable!")
            // use background thread for connecting and getting information/ data from the server
            GenerateUpdates(this).execute()
        }

        // what does setContentView do in Android? when using R.layout.xml, it will use that design to fill the UI window
        setContentView(R.layout.activity_loading)
        // this is currently my best definition for decor view. I currently still don't know why they use it.
        // The DecorView is the view that actually holds the window’s background drawable.
        // Calling getWindow().setBackgroundDrawable() from your Activity changes the background of the
        // window by changing the DecorView‘s background drawable. As mentioned before, this setup is
        // very specific to the current implementation of Android and can change in a future version or even
        // on another device. This was an answer posted in 2014 on stack overflow
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // check if app needs an update, if it does then prompt user to update the app
        checkForAppUpdate()

        // start a new activity after delayed time (not sure why they hardcode delayed time)
        delayedTime(SSOLoginScreenFragment::class.java, loadingTime)

        // Background Fetch (retrieving news and events and lab equipment data from urls) so yes they are all run on
        // background threads
        try {
            AsyncTaskEvent().execute(getString(R.string.event_url))
            AsyncTaskNews().execute(getString(R.string.news_url))
            AsyncTaskLab().execute(*resources.getStringArray(R.array.lab_equipment_url))
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
    }

    // this method checks if the app in the google play store was re-deployed and will propmt the user to update the app,
    // if update is available
    private fun checkForAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        IN_APP_UPDATE_REQUEST
                )
            }
        }
    }

    // on ActivityResult Im pretty sure corresponds with startUpdateFlowForResult. It is the code that executes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IN_APP_UPDATE_REQUEST) {
            if (resultCode != RESULT_OK) {
                checkForAppUpdate()
            }
        }
    }

    /**
     * Start new activity after delayed time
     * @param nextActivity
     * @param time
     */
    fun delayedTime(nextActivity: Class<*>, time: Int): Boolean {
        var action = false
        Log.d(TAG, "delaying in " + time + "miliseconds")
        // Init handler
        val handler = Handler()

        // Take action after delay time
        if (!action) {
            handler.postDelayed({
                startActivity(Intent(this, nextActivity))
                finish()
            }, time.toLong())
            action = true
        } else {
            action = false
        }
        return action
    }

    companion object {
        // havent seen an example of a "general update" so idk. also not sure what an interal constructor is. And also not
        // sure what an async task is and also not sure what doInBackground implies, or what vararg is.
        // Answer, because this is a call to the server, another thread needs to be used in order to keep the UI thread
        // in operation while gathering data from server at the same time. doInBackground code is the code that is executed on
        // the thread (that is not the main UI thread).
        class GenerateUpdates internal constructor(context: LoadingActivity) : AsyncTask<Void?, Void?, Void?>() {

            override fun doInBackground(vararg p0: Void?): Void? {
                val mFunctions = FirebaseFunctions.getInstance()
                mFunctions
                        .getHttpsCallable("updates")
                        .call()
                        .continueWith { task: Task<HttpsCallableResult?> ->
                            val result = task.result
                            result!!.data
                        }
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val recentUpdatesListMap = it.result as ArrayList<HashMap<String, String>>
                                for (recentUpdate in recentUpdatesListMap) {
                                    UserConstants.RECENT_UPDATE_CARDS.add(RecentUpdateCard(recentUpdate["title"], recentUpdate["body"], recentUpdate["date"]))
                                }
                            } else {
                                UserConstants.RECENT_UPDATE_CARDS.add(RecentUpdateCard("No Internet connection", "Please connect to the Internet", ""))
                            }
                            Timber.d(it.exception.toString())
                        }
                return null

            }
        }
    }
}
