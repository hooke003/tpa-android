package appfactory.uwp.edu.parksideapp2.IndoorNav

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import appfactory.uwp.edu.parksideapp2.R

class IndoorNavMain : AppCompatActivity() {

    /**
     * This is the launcher that works with the new contract based permission requests that handle a
     * callback for the permissions response instead.
     */
    private val requestPermissionLauncher = registerForActivityResult(RequestMultiplePermissions()) { grantedPerms: Map<String, Boolean> ->
        if (grantedPerms[Manifest.permission.ACCESS_COARSE_LOCATION] == true && grantedPerms[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            geolocationPermissionsCallback?.invoke(geolocationPermissionsOrigin, true, false)
        } else {
            Toast.makeText(this, "Geolocation not enabled", Toast.LENGTH_SHORT)
        }
    }

    private lateinit var webView: WebView

    // references for permissions in WebView when permissions are granted.
    // Left null to match type from the callback for permissions pop up.
    private var geolocationPermissionsOrigin: String? = null
    private var geolocationPermissionsCallback: GeolocationPermissions.Callback? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indoor_nav_main)

        // binding
        webView = findViewById(R.id.indoorNav_web)
        with(webView) {

            // enable javascript
            settings.javaScriptEnabled = true
            settings.setGeolocationEnabled(true)

            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.
            webViewClient = WebViewClient()

            webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String?,
                    callback: GeolocationPermissions.Callback?
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            callback?.invoke(origin, true, false)
                        } else {
                            geolocationPermissionsOrigin = origin
                            geolocationPermissionsCallback = callback
                            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                        }
                    } else {
                        Toast.makeText(context, "Must be API version ${Build.VERSION_CODES.O} or above to add location services in for this app.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // load the url
            loadUrl("https://indoornav.appfactoryuwp.com/")
        }
    }
}