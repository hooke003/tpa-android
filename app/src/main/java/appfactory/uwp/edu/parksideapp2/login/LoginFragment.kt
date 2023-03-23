package appfactory.uwp.edu.parksideapp2.login


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import appfactory.uwp.edu.parksideapp2.R
import appfactory.uwp.edu.parksideapp2.utils.BUNDLE_PRIVACY_POLICY
import appfactory.uwp.edu.parksideapp2.utils.PRIVACY_POLICY_HTML_URL
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 *
 */
const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        viewModel = activity?.let {
            ViewModelProviders.of(it).get(LoginViewModel::class.java)
        } ?: throw Exception("Activity Missing")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.homeFragment, true)
        }
        //viewModel.logOut()

        /**
         * Text to speach i am assuming?
         */
        textTosPp.setOnClickListener {
            findNavController().navigate(R.id.action_global_webViewFragment, bundleOf(BUNDLE_PRIVACY_POLICY to PRIVACY_POLICY_HTML_URL))
        }

        // Stubbed in method exits login and sends user back to previous screen.
        /**
         * logs user in
         */
        buttonSignIn.setOnClickListener {
            viewModel.loginOnClick(editTextEmail.text.toString(), editTextPassword.text.toString())

        }

        /**
         * Navigate to password reset fragment
         */
        reset_pw_button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordResetFragment)
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
                        Snackbar.make(requireView(), "Incorrect email or password", Snackbar.LENGTH_SHORT).show()
                        editTextPassword.text?.clear()

                    }
                }
            }

        })


        viewModel.getErrorMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { pair ->
                when (pair.first) {
                    "email" -> if (pair.second) editTextEmail.error = null else editTextEmail.error = getString(R.string.error_invalid_email)
                    "password" -> if (pair.second) editTextPassword.error = null else editTextPassword.error = getString(R.string.error_invalid_password)
                    "all" -> arrayOf(editTextEmail, editTextPassword).forEach { it.error = null }
                }
            }
        })

        buttonRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }

    }
}
