package appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event

data class Event(
        val description: String,
        val displayDate: String,
        val endDate: String,
        val flag: String,
        val goal: Int,
        val id: String,
        val location: String,
        val startDate: String,
        val title: String
)