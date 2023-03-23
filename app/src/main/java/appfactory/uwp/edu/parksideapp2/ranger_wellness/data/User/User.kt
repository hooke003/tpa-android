package appfactory.uwp.edu.parksideapp2.ranger_wellness.data.User


import java.lang.reflect.Array


data class User(


        val UID: String?,

        val email: String?,

        val eFlagged: Array?,

        var steps: Int?,

        var totalSteps: Int?,

        val wFlagged: Array?
)
