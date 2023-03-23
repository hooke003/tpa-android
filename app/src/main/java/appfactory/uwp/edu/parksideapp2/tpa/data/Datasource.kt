package appfactory.uwp.edu.parksideapp2.tpa.data

import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.tpa.model.Component
import appfactory.uwp.edu.parksideapp2.tpa.model.Tile
import appfactory.uwp.edu.parksideapp2.tpa.model.Topic
import appfactory.uwp.edu.parksideapp2.tpa.model.User

class Datasource {

    /**
     * This function returns the list of all components in app
     */
    fun componentList(): List<Component> {
        return listOf<Component>(
            Component(R.string.map, R.drawable.maps_icon, R.drawable.ic_maps_small, R.color.landingMapCardView, subscribable = true, subscribed = false, Topic(R.string.mapId, R.string.map)),
            Component(R.string.rangerWellness, R.drawable.ic_bigbear, R.drawable.ic_ranger_wellness_small, R.color.landingRWCardView, subscribable = true, subscribed = false, Topic(R.string.wellnessId, R.string.rangerWellness)),
            Component(R.string.news, R.drawable.news_icon, R.drawable.ic_news_small, R.color.landingNewsCardView, subscribable = true, subscribed = false, Topic(R.string.newsId, R.string.news)),
            Component(R.string.navigate, R.drawable.ic_naviagte, R.drawable.ic_navigate_small, R.color.landingNavigateCardView, subscribable = true, subscribed = false, Topic(R.string.navigateId, R.string.navigate)),
            Component(R.string.events, R.drawable.ic_events2, R.drawable.ic_events_small, R.color.landingEventsCardView, subscribable = true, subscribed = false, Topic(R.string.eventsId, R.string.events)),
            Component(R.string.eAccounts, R.drawable.ic_eaccounts, R.drawable.ic_eaccounts_small, R.color.landingeAccountsCardView, subscribable = true, subscribed = false, Topic(R.string.eaccountsId, R.string.eAccounts)),
            Component(R.string.computerLabs, R.drawable.ic_computer, R.drawable.ic_computer_labs_small, R.color.landingComputerLabsCardView, subscribable = true, subscribed = false, Topic(R.string.computerId, R.string.computerLabs)),
            // TODO: convert minimap to drawable?
            Component(R.string.titleIX, R.drawable.ic_titleix, R.drawable.ic_titleix, R.color.landingTitleIXCardView, subscribable = false, subscribed = null, null),
            Component(R.string.indoorNav, R.drawable.ic_baseline_explore_24, R.drawable.ic_indoornav_small,R.color.landingIndoorNavCardView, subscribable = false, subscribed = null, null)
        )
    }

    /**
     * This function returns the subscribable topics not included in the component list
     */
    fun topicsThatDontHaveAComponentList(): List<Topic> {
        return listOf<Topic> (
            Topic(R.string.studentId, R.string.student),
            Topic(R.string.staffId, R.string.staff)
        )
    }

    fun userList(): List<User> {
        return listOf<User> (
            User("", "", "", "", initialRegistration = true, fineLocation = false, backgroundLocation = false, loggedIn = false, rwAuthorized = false)
        )
    }

    fun tileList(): List<Tile> {
        return listOf<Tile> (
            Tile("Maps", 0, R.drawable.ic_maps_small),
            Tile("Ranger Wellness", 1, R.drawable.ic_ranger_wellness_small),
            Tile("News", 2, R.drawable.ic_news_small),
            Tile("Navigate", 3, R.drawable.ic_navigate_small),
            Tile("Events", 4, R.drawable.ic_events),
            Tile("eAccounts", 5, R.drawable.ic_eaccounts_small),
            Tile("Computer Labs", 6, R.drawable.ic_computer_labs_small),
            Tile("Title IX", 7, R.drawable.ic_titleix),
            Tile("Indoor Navigation", 8, R.drawable.ic_indoornav_small)
        )
    }
}