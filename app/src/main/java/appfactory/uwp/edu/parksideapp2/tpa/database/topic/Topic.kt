package appfactory.uwp.edu.parksideapp2.tpa.database.topic

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (primaryKeys = ["uid", "topicName"], tableName = "topic")
data class Topic (
    val topicName: String,
    val uid: String,
    var value: Boolean?
    )