package appfactory.uwp.edu.parksideapp2


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

class MainNavigationActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        Timber.d("onCreated() is called")

        navController = findNavController(R.id.navHostFragment)

        val toolbar = findViewById<Toolbar>(R.id.toolbarMain)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val menuDrawer = findViewById<NavigationView>(R.id.drawer_navigation)
        when (navController.currentDestination) {
            navController.graph[R.id.homeFragment] -> toolbar.title = ""
        }
        setSupportActionBar(toolbar)
        val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.RWDashboardFragment, R.id.homeFragment, R.id.navigation_login_graph),
                drawerLayout
        )
        toolbar.setupWithNavController(navController, appBarConfiguration)
        menuDrawer.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val logo = findViewById<ImageView>(R.id.image_logo)
            logo.visibility = when (destination.id) {
                R.id.homeFragment, R.id.loginFragment, R.id.registerFragment, R.id.passwordResetFragment -> {
                    toolbar.title = ""
                    View.VISIBLE
                }
                else -> View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
