package appfactory.uwp.edu.parksideapp2.tpa.database.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import appfactory.uwp.edu.parksideapp2.tpa.database.topic.Topic
import appfactory.uwp.edu.parksideapp2.tpa.database.topic.TopicDao

class TopicsViewModel(private val topicDao: TopicDao) : ViewModel() {

        fun getByTopicName(topicName: String): List<Topic> = topicDao.getByTopicName(topicName)
}

// this class instantiates view model objects (instead of handling things directly in the fragments)
class TopicsViewModelFactory(private val topicDao: TopicDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopicsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopicsViewModel(topicDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}