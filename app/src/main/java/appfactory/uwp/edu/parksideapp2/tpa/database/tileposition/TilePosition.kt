package appfactory.uwp.edu.parksideapp2.tpa.database.tileposition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tile_position")
data class TilePosition (
    @PrimaryKey (autoGenerate = false)
    val tileCardTextView: String,
    var position: Int,
    var iconRes: Int,
    val uid: String,
    )
