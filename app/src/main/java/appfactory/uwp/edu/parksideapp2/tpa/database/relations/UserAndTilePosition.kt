package appfactory.uwp.edu.parksideapp2.tpa.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import appfactory.uwp.edu.parksideapp2.tpa.database.tileposition.TilePosition
import appfactory.uwp.edu.parksideapp2.tpa.database.user.User

data class UserAndTilePosition(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid"
    )
    val tilePosition: TilePosition,
)