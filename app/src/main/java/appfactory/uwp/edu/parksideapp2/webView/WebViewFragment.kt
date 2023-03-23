package appfactory.uwp.edu.parksideapp2.webView


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.utils.BUNDLE_PRIVACY_POLICY
import kotlinx.android.synthetic.main.fragment_web_view.*

/**
 * A simple [Fragment] subclass.
 */
class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(BUNDLE_PRIVACY_POLICY)?.let { webView.loadUrl(it) }
    }
}
