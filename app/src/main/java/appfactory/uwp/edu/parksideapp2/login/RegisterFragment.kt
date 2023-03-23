package appfactory.uwp.edu.parksideapp2.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.utils.BUNDLE_PRIVACY_POLICY
import appfactory.uwp.edu.parksideapp2.utils.PRIVACY_POLICY_HTML_URL
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 *
 */
class RegisterFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let {
            ViewModelProviders.of(it).get(LoginViewModel::class.java)
        } ?: throw Exception("Activity Missing")

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.loginFragment, true)
        }

        /**
         * Text to speech i'm guessing??
         */
        textTosPp.setOnClickListener {
            findNavController().navigate(R.id.action_global_webViewFragment, bundleOf(BUNDLE_PRIVACY_POLICY to PRIVACY_POLICY_HTML_URL))
        }

        /**
         * Register new user
         */
        buttonRegister.setOnClickListener {
            viewModel.registerOnClick(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString(),
                    editTextConfirmPassword.text.toString(),
                    editTextFirstName.text.toString(),
                    editTextLastName.text.toString())
        }

        /**
         * Sends user back to home screen after log in
         */
        viewModel.getLoginEvent.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.getContentIfNotHandled()?.let {
                    if (it == LoginEvent.LOGGED_IN) {
                        findNavController().navigate(R.id.action_navigation_login_graph_pop)
                    }else if(it == LoginEvent.FAILED){
                        Toast.makeText(context, R.string.error_registration_failed, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

        viewModel.getErrorMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { pair ->
                when (pair.first){
                    "email" -> if (pair.second) editTextEmail.error = null else editTextEmail.error = getString(R.string.error_invalid_email)
                    "password" -> if (pair.second) editTextPassword.error = null else editTextPassword.error = getString(R.string.error_invalid_password)
                    "passwordConfirm" -> if (pair.second) editTextConfirmPassword.error = null else editTextConfirmPassword.error = getString(R.string.error_password_mismatch)
                    "firstName" -> if (pair.second) editTextFirstName.error = null else editTextFirstName.error = getString(R.string.error_field_required)
                    "lastName" -> if (pair.second) editTextLastName.error = null else editTextLastName.error = getString(R.string.error_field_required)
                    "all" -> arrayOf(editTextEmail, editTextPassword, editTextConfirmPassword, editTextFirstName, editTextLastName).forEach { it.error = null }
                }
            }
        })

    }

}
