package appfactory.uwp.edu.parksideapp2.ApplicationClass

/**
 * Created by kyluong09 on 3/19/18.
 */

import android.app.Application
import appfactory.uwp.edu.parksideapp2.BuildConfig
import appfactory.uwp.edu.parksideapp2.ComputerLab.AsyncTaskLab
import appfactory.uwp.edu.parksideapp2.Event.AsyncTaskEvent
import appfactory.uwp.edu.parksideapp2.News.AsyncTaskNews
import appfactory.uwp.edu.parksideapp2.R
import com.facebook.stetho.Stetho
import io.realm.Realm
import io.realm.RealmConfiguration
import org.xml.sax.SAXException
import timber.log.Timber
import javax.xml.parsers.ParserConfigurationException


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Init Realm and set configuration
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("Parkside App").deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
        Stetho.initializeWithDefaults(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

//        try {
//            AsyncTaskEvent().execute(getString(R.string.event_url));
//            AsyncTaskNews().execute(getString(R.string.news_url));
//            AsyncTaskLab().execute(*resources.getStringArray(R.array.lab_equipment_url));
//        } catch (e: ParserConfigurationException) {
//            e.printStackTrace();
//        } catch (e: SAXException) {
//            e.printStackTrace();
//        }
    }
}
