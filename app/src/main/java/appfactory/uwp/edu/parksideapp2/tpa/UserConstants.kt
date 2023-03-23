package appfactory.uwp.edu.parksideapp2.tpa

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData.components
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData.tiles
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData.user
import appfactory.uwp.edu.parksideapp2.tpa.data.Datasource
import appfactory.uwp.edu.parksideapp2.tpa.database.UserConstantsDao
import appfactory.uwp.edu.parksideapp2.tpa.database.UserConstantsDatabase
import appfactory.uwp.edu.parksideapp2.tpa.model.NotificationCard
import appfactory.uwp.edu.parksideapp2.tpa.model.Tile
import appfactory.uwp.edu.parksideapp2.tpa.model.Topic
import appfactory.uwp.edu.parksideapp2.tpa.model.User
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

/**
 * This class keeps track of all the user preferences data. This data includes  the topics the
 * user has chosen to subscribe to, the arrangement of tiles on the main page/ main recycler view for the user
 * and other user info including email, display name, initial registration, fine location, background location, etc.
 *
 * @author Reece Hooker
 * @version 2.0
 *
 */

/**
 * Kotlin and java have differences in making a variable static (global).
 * This object class makes the variables in the class static (global) (aka accessible througout the entire app).
 * I had tried to do this with a companion object within the UserConstants class itself and was having
 * troubles accessing in other classes. This is why I created it as an object.
 *
 */
object UserConstantsData{
    // static user object
    var user = User(
        "",
        "",
        "",
        "",
        initialRegistration = true,
        fineLocation = false,
        backgroundLocation = false,
        loggedIn = false,
        rwAuthorized = false
    )

    // static lists (access position of item of list and change the item)
    var components = Datasource().componentList()
    var tiles = Datasource().tileList()
    lateinit var notificationCards: MutableList<NotificationCard>
}

class UserConstants {

    private lateinit var functions: FirebaseFunctions
    private val topicsThatDontHaveAComponent = Datasource().topicsThatDontHaveAComponentList()
    private lateinit var topicsList: MutableList<String>
    private lateinit var topicsMap: MutableMap<String, List<String>>
    private lateinit var landingTileCards: MutableList<Tile>

    /**
     * The save function is used to save user preferences when a user updates default or existing user
     * preferences. The data is saved to a local database (Room database, for easier retrieval, rather than having
     * to make a network call to a faraway server like firebase) The save function will update user preferences
     * data including topics the user has chosen to subscribe to, the arrangement of tiles on the main page/ main recycler
     * view for the user and other user info including email, display name, initial registration, fine location, background location, etc.
     *
     * Context is passed in because it is needed for various reasons, otherwise the code will not work.
     */
    fun save(context: Context) {
        // do not run background thread here: instead run when around the call to save method.

        // for all the components, if the component is subscribeable and subscribed = true then create notification for the component
        for (component in components) {
            if (component.subscribed == true && component.topic != null) {
                createNotification(component.topic, context)

                // add topic to topic list to be later sent to firebase
                topicsList.add(context.resources.getString(component.topic.channelId))
            }
        }

        //TODO
        // did I do the user thing right: my plan of action was replacing default varibales
        // for all topics that don't have a component (student and staff) create notification
        // actually im pretty sure I did this right because user.email will be filled in (i think by the time this method
        // is called)

        // This block of code determines if the user is a student or staff by using user.email. The reason why the topicsThatDontHaveAComponent
        // list is used is to make the notification once the type of user (student or staff) is determined.
        for (topic in topicsThatDontHaveAComponent) {
            // if the user's email is a student's email create a student notification channel
            if (user.email.split("@")[1].equals("rangers.uwp.edu", ignoreCase = true)) {
                createNotification(topic, context)

            // if the user's email is not a student's email (must be a staff) then make a staff notification channel
            } else {
                createNotification(topic, context)
            }

            // add the notification channel to the topicsList to be later sent to firebase (literally in the next block)
            topicsList.add(context.resources.getString(topic.channelId))
        }

        // add a user's subscription preferences information into firebase
        topicsMap.put("topics", topicsList)
        functions = Firebase.functions
        functions
            .getHttpsCallable("setsubscriptions")
            .call(topicsMap)
            .continueWith { task ->
                // TODO is this correct?
                val result = task.result?.data
                result
            }.addOnCompleteListener {
                // TODO is this correct? Should I change to success and failure?

                // addOnCompleteListener means once this task (once the code has communicated with the server) whether
                // it was successful (onSucess, or onFailure) or not, turn the initial registration to false.
                user.initialRegistration = false
            }

        // inserting / updating information into room database
        val dao: UserConstantsDao = UserConstantsDatabase.getInstance(context).userConstantsDao

        // user topics

        // TODO how to properly initialize
        var topicsTempList: MutableList<appfactory.uwp.edu.parksideapp2.tpa.database.topic.Topic>? =
            null
        for (component in components) {

            // create a topic instance
            //TODO how to grab UID
            val topic = appfactory.uwp.edu.parksideapp2.tpa.database.topic.Topic(
                context.resources.getString(component.name), "123", component.subscribed
            )

            // add to list of topics
            topicsTempList!!.add(topic)
        }

        // insert data into room database

        // user tile position

        // other user info for user

    }

    /**
     * This function is used to create the notification channel and its settings. This code came from
     * Android documentation. The method is private because it is only used in this class.
     */
    private fun createNotification(topic: Topic, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // context is needed for getting the string value from the string resources (which is stored as an int)
            val resources = context?.resources
            val name = resources?.getString(topic.channelName)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(resources?.getString(topic.channelId), name, importance)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * The load function is used to load default or custom user preferences from Room Database. The load function
     * load user preferences data (default, if not changed since initial registration, or custom) including topics the
     * user has chosen to subscribe to, the arrangement of tiles on the main page/ main recycler view for the user
     * and other user info including email, display name, initial registration, fine location, background location, etc.
     */
    fun load(context: Context) {
        // if user preferences data exists within room database
        // TODO figure out how to tell if a user has data in the database
        if (false) {

            // grab data from data base (will be other user info)

            // assign topic subscriptions

            // assign tile arrangement
        }
        // if no user data exists yet
        else {
            // default tile positions
            for (tile in tiles) {
                landingTileCards.add(tile)
            }
        }
    }
}