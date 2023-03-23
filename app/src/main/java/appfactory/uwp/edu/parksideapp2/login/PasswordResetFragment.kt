package appfactory.uwp.edu.parksideapp2.login


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import appfactory.uwp.edu.parksideapp2.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_password_reset.*

/**
 * Password Reset Fragment
 * @author Nick Claffey
 */
class PasswordResetFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let {
            ViewModelProviders.of(it).get(LoginViewModel::class.java)
        } ?: throw Exception("Activity Missing")

        cancel_button.setOnClickListener {
            findNavController().navigate(R.id.action_passwordResetFragment_to_loginFragment)
        }

        /**
         * Send the user's input email to the viewmodel's passwordResetOnclick function
         */
        pw_reset_button.setOnClickListener {
            Log.d("emailinput", "email input = ${editText_pwReset.text.toString()}")
            viewModel.passwordResetOnClick(
                    editText_pwReset.text.toString()
            )

        }

        /**
         * Check to see if the email was sent or not
         */
        viewModel.getPwResetEvent.observe(viewLifecycleOwner, Observer {
            it?.let{
                it?.getContentIfNotHandled().let{
                    if(it == PassWordResetEvent.SUCCESS){
                        Snackbar.make(requireView(), "Email Sent!", Snackbar.LENGTH_SHORT).show()
                        editText_pwReset.text!!.clear()
                    }else if(it == PassWordResetEvent.FAILED){
                        Snackbar.make(requireView(), "Unable to Send Email.", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })

        /**
         * Check to see if the email is properly formatted
         */
        viewModel.getErrorMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { pair ->
                when (pair.first){
                    "email" -> if (pair.second) editText_pwReset.error = null else editText_pwReset.error = getString(R.string.error_invalid_email)

                }
            }
        })
    }


}
