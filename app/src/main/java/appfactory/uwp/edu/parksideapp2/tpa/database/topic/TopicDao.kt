package appfactory.uwp.edu.parksideapp2.tpa.database.topic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.jetbrains.annotations.Async

@Dao
// working with the database is a time consuming operation and therefore
// needs to be done in the background thread, use flow (coroutines)
interface TopicDao {

    @Query("SELECT * FROM topic WHERE topic_name = :topicName")
    fun getByTopicName(topicName: String): List<Topic>

    // im not quite sure how to order rn
    @Query("SELECT * FROM topic ORDER BY topic_name ASC")
    fun getTopics(): List<Topic>

    // suspend keyword makes this main safe (hopefully prevents race conditions)
    @Insert
    suspend fun insert(topic: Topic)

    @Query("DELETE FROM topic")
    fun deleteAll()



}