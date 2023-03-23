package appfactory.uwp.edu.parksideapp2.tpa.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = false)
    val uid: String
    )