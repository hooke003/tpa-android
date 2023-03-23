package appfactory.uwp.edu.parksideapp2.tpa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import appfactory.uwp.edu.parksideapp2.tpa.database.otheruserinfo.OtherUserInfo
import appfactory.uwp.edu.parksideapp2.tpa.database.tileposition.TilePosition
import appfactory.uwp.edu.parksideapp2.tpa.database.topic.Topic
import appfactory.uwp.edu.parksideapp2.tpa.database.topic.TopicDao
import appfactory.uwp.edu.parksideapp2.tpa.database.user.User

@Database(
    entities = [
        User::class,
        OtherUserInfo::class,
        TilePosition::class,
        Topic::class
    ],
    version = 1
)
abstract class UserConstantsDatabase: RoomDatabase() {

    abstract val userConstantsDao: UserConstantsDao

    companion object {
        // volatile helps to prevent race conditions
        @Volatile
        private var INSTANCE: UserConstantsDatabase? = null

        fun getInstance(context: Context): UserConstantsDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserConstantsDatabase::class.java,
                    "user_constants_db",
                ).build().also {
                    INSTANCE = it
                }

            }
        }
    }

}