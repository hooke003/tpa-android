package appfactory.uwp.edu.parksideapp2.TitleIX

import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import appfactory.uwp.edu.parksideapp2.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @author Zoli
 * @version 1.0
 * @since 12.21.2021
 * TitleIX main, where all the magic happens
 */


class TitleIXMain : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_title_ix_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, TitleIXHome(), "TitleIXHome")
                .commit()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        when(item.itemId) {
            R.id.titleIXHome -> {
                if (!supportFragmentManager.fragments.get(0).tag.equals("TitleIXHome", true)) {
                    fragmentTransaction.replace(R.id.container, TitleIXHome(), "TitleIXHome").commit()
                }
            }
            R.id.titleIXReporting -> {
                if (!supportFragmentManager.fragments.get(0).tag.equals("TitleIXReporting", true)) {
                    fragmentTransaction.replace(R.id.container, TitleIXReporting(), "TitleIXReporting").commit()
                }
            }
            R.id.titleIXResources -> {
                if (!supportFragmentManager.fragments.get(0).tag.equals("TitleIXResources", true)) {
                    fragmentTransaction.replace(R.id.container, TitleIXResources(), "TitleIXResources").commit()
                }
            }
        }

        return true

    }

}