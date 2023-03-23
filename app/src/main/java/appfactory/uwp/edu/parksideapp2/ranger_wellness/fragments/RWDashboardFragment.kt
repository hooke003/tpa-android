package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.extensions.snackbar
import appfactory.uwp.edu.parksideapp2.login.LoginViewModel
import appfactory.uwp.edu.parksideapp2.ranger_wellness.adapters.EventRecyclerViewAdapter
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.EventViewModel
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.TAG
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout.WorkoutViewModel
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants
import appfactory.uwp.edu.parksideapp2.utils.EVENT_ID
import appfactory.uwp.edu.parksideapp2.utils.REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE
import appfactory.uwp.edu.parksideapp2.utils.REQUEST_OAUTH_REQUEST_CODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_rwdashboard.*
import timber.log.Timber

/**
 * Dashboard rework
 * @author Nick Claffey
 */
private const val TAG = "RWDashboardFragment"

class RWDashboardFragment : Fragment(R.layout.fragment_rwdashboard) {
    private val dashboardViewModel by activityViewModels<RWDashboardViewModel>()
    private val eventViewModel by activityViewModels<EventViewModel>()
    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestDeviceFitPermissions()

        textViewUserName.text = "Welcome ${UserConstants.DISPLAY_NAME}"

        cardVeiwEvents.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                    .replace(
                            R.id.rw_landing_fragment_container,
                            RWEventListFragment(),
                            "RWEventListFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }
        cardViewWorkouts.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                    .replace(
                            R.id.rw_landing_fragment_container,
                            RWWorkoutLogFragment(),
                            "RWWorkoutLogFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }

        /**
         * Display the global step goal
         */
        dashboardViewModel.getsteps().observe(viewLifecycleOwner, Observer {
            textViewCurrentGoal.text = it

        })

        val adapter = EventRecyclerViewAdapter {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            val bundle = bundleOf(EVENT_ID to it.id)

            val rwEventDetailFragment = RWEventDetailFragment()
            rwEventDetailFragment.arguments = bundle
            fragmentTransaction
                    .replace(
                            R.id.rw_landing_fragment_container,
                            RWEventDetailFragment(),
                            "RWEventDetailFragment"
                    )
                    .addToBackStack(null)
                    .commit()
        }

        val layoutManager = LinearLayoutManager(recyclerViewDashboard.context)
        recyclerViewDashboard.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }

        /**
         * Display the User's subscribed events
         */
        eventViewModel.getSubbedEvents().observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(TAG, "Subbed List: $it")
                adapter.submitList(it)
                when {
                    it.isEmpty() -> {
                        cardViewEmptyList.visibility = View.GONE
                        textViewEmptyList.text = "No subscribed events found."
                    }
                    it.isNotEmpty() -> {
                        cardViewEmptyList.visibility = View.GONE
                    }
                }
            }
        })

        dashboardViewModel.getsDayteps().observe(viewLifecycleOwner, Observer {
            testViewDailyStepCount.text = it

        })

        /**
         * Total logged steps
         */
        workoutViewModel.getStepsTowardsGoal().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "value of it: $it")
            textViewStepsTowardGoal.text = "$it/"
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.rw_logout_button_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestDeviceFitPermissions(): Any {
        return if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE)
            requestAndSubscribeToFitApi()
        } else {
        }
    }

    private fun requestAndSubscribeToFitApi() {
        val fitnessOptions = FitnessOptions.builder()
//                .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .build()

        val account = GoogleSignIn.getAccountForExtension(requireContext(), fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    account,
                    fitnessOptions
            )
            trackPedometerSteps()
            getPedometerSteps()
        } else {
            trackPedometerSteps()
            getPedometerSteps()
        }
    }

    private fun trackPedometerSteps(): Task<Void> {
        return Fitness.getRecordingClient(requireActivity(), GoogleSignIn.getLastSignedInAccount(requireContext())!!)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.i("Successfully subscribed!")
                    } else {
                        Timber.i("There was a problem subscribing. ${task.exception}")
                    }
                }
    }

    private fun getPedometerSteps() {
        Fitness.getHistoryClient(requireContext().applicationContext, GoogleSignIn.getLastSignedInAccount(requireContext())!!)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener {
                    Log.d(TAG, "OnSucces(): it = $it")
                    try {
                        val dp = it.dataPoints
                        val data = dp.get(0).getValue(Field.FIELD_STEPS).toString().toInt()
                        Log.d(TAG, "dataPoint = $data")
                        dashboardViewModel.logpedoSteps(data)
                    } catch (e: Exception) {
                        Log.d(TAG, "daily steps log doesn't exist")
                        val data = 0
                        Log.d(TAG, "dataPoint = $data")
                        dashboardViewModel.logpedoSteps(data)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "onFailure")
                    val data = 0
                    Log.d(TAG, "dataPoint = $data")
                    dashboardViewModel.logpedoSteps(data)
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                trackPedometerSteps()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestAndSubscribeToFitApi()
                } else {
                    view?.snackbar("Permission Denied. Pedometer disabled.")
                }
            }
        }
    }
}
