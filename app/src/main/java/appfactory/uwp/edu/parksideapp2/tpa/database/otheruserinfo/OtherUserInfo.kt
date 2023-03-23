package appfactory.uwp.edu.parksideapp2.tpa.database.otheruserinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "otherUserInfo")
data class OtherUserInfo (
    @PrimaryKey (autoGenerate = false)
    val uid: String,
    var rwAuthorized: Boolean,
    var lastDate: Boolean,
    var fineLocation: String,
    var backgroundLocation: String,
    var initialRegistration: Boolean,
    var lastNotificationTimeStamp: String,
)