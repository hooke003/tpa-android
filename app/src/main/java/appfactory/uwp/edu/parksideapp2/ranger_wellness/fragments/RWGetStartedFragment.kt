package appfactory.uwp.edu.parksideapp2.ranger_wellness.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.extensions.snackbar
import appfactory.uwp.edu.parksideapp2.ranger_wellness.RWDashboardActivity
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData
import appfactory.uwp.edu.parksideapp2.tpa.UserConstantsData.user
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants
import appfactory.uwp.edu.parksideapp2.tpa2.user.UserConstants.RW_AUTHORIZED
import appfactory.uwp.edu.parksideapp2.utils.REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE
import appfactory.uwp.edu.parksideapp2.utils.REQUEST_OAUTH_REQUEST_CODE
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.tasks.Task
import timber.log.Timber

class RWGetStartedFragment : Fragment() {
    private var rwGetStartedButton: Button? = null
    private fun initUI(view: View) {
        Timber.d("Initializing User Interface")
        rwGetStartedButton = view.findViewById(R.id.rwGetStartedButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rw_get_started, container, false)
        initUI(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rwGetStartedButton!!.setOnClickListener {
            if (Build.VERSION.SDK_INT < 29) {
                AlertDialog.Builder(requireContext())
                        .setTitle(Html.fromHtml("<font color='#000000'>Not supported!</font>"))
                        .setMessage(Html.fromHtml("<font color='#000000'>Your version of Android does not support the Google Fit API!</font>"))
                        .setNeutralButton("Ok") { dialogInterface: DialogInterface?, i: Int -> }
                        .create()
                        .show()
            }
            else
                requestDeviceFitPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestDeviceFitPermissions(): Any {
        Timber.d("requestDeviceFitPermissions: Called")
        return if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            Timber.d("requestDeviceFitPermissions: Requesting permission")
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE)
            requestAndSubscribeToFitApi()
        } else {
            Timber.d("requestDeviceFitPermissions: Permission granted")
            //user.rwAuthorized = true
            RW_AUTHORIZED = true
            UserConstants.save(requireActivity())
            val intent = Intent(requireActivity(), RWDashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestAndSubscribeToFitApi() {
        Timber.d("requestAndSubscribeToFitApi: Called")
        val fitnessOptions = FitnessOptions.builder()
                .addDataType(
                        DataType.TYPE_STEP_COUNT_DELTA
                )
                .build()

        val account = GoogleSignIn
                .getAccountForExtension(
                        requireContext(),
                        fitnessOptions
                )

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Timber.d("requestAndSubscribeToFitApi->GoogleSignIn: Called")
            GoogleSignIn
                    .requestPermissions(
                            requireActivity(),
                            REQUEST_OAUTH_REQUEST_CODE,
                            account,
                            fitnessOptions
                    )

        }
    }

    private fun trackPedometerSteps(): Task<Void> {
        Timber.d("trackPedometerSteps: Called")
        return Fitness.getRecordingClient(requireContext(), GoogleSignIn.getLastSignedInAccount(requireContext())!!)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("Successfully subscribed!")
                    } else {
                        Timber.d("There was a problem subscribing. ${task.exception}")
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult: Called")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                trackPedometerSteps()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Timber.d("onRequestPermissionsResult: Called")
        when (requestCode) {
            REQUEST_MANIFEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d("onRequestPermissionsResult: Permission granted for pedometer")
                    requestAndSubscribeToFitApi()
                } else {
                    view?.snackbar("Permission Denied. Pedometer disabled.")
                }
            }
        }
    }
}