package appfactory.uwp.edu.parksideapp2.tpa.model

data class User (var email: String,
                 var displayName: String,
                 var todayDate: String,
                 var lastDate: String,
                 var initialRegistration: Boolean,
                 var fineLocation: Boolean,
                 var backgroundLocation: Boolean,
                 var loggedIn: Boolean,
                 var rwAuthorized: Boolean,
)