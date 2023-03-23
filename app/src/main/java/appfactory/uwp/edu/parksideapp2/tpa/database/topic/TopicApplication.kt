package appfactory.uwp.edu.parksideapp2.tpa.database.topic

import android.app.Application
import appfactory.uwp.edu.parksideapp2.tpa.database.UserConstantsDatabase

class TopicApplication : Application() {
    val database: UserConstantsDatabase by lazy {UserConstantsDatabase.getDatabase(this)}
}