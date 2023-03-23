package appfactory.uwp.edu.parksideapp2.tpa.database

import androidx.room.*
import appfactory.uwp.edu.parksideapp2.tpa.database.otheruserinfo.OtherUserInfo
import appfactory.uwp.edu.parksideapp2.tpa.database.relations.UserAndOtherUserInfo
import appfactory.uwp.edu.parksideapp2.tpa.database.relations.UserAndTilePosition
import appfactory.uwp.edu.parksideapp2.tpa.database.tileposition.TilePosition
import appfactory.uwp.edu.parksideapp2.tpa.database.topic.Topic
import appfactory.uwp.edu.parksideapp2.tpa.database.user.User

@Dao
interface UserConstantsDao {

    // on conflict used to prevent duplicate inserts, if duplicate then replace
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherUserInfo(otherUserInfo: OtherUserInfo)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: Topic)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTilePosition(tilePosition: TilePosition)

    @Transaction // to prevent multithreading issues with joins
    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun getUserAndOtherUserInfoWithUid(uid: String) : List<UserAndOtherUserInfo>

    @Transaction
    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun getUserAndTilePosition(uid: String) : List<UserAndTilePosition>

    // return a list of multiple users which will return a list of all topics that user is subscribed to
    @Transaction
    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun getUserWithTopics(uid: String)

}