package appfactory.uwp.edu.parksideapp2.login

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appfactory.uwp.edu.parksideapp2.extensions.isValidEmailAddress
import appfactory.uwp.edu.parksideapp2.extensions.isValidPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    /**
     * Firebase auth implemented by Nick Claffey 12/20/2019
     */

    private val auth = FirebaseAuth.getInstance()

    //This will return null if user not logged in, otherwise it will give you the current user.
    var user: FirebaseUser? = auth.currentUser

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("pref", Context.MODE_PRIVATE)

    private val errorMessage = MutableLiveData<SingleEvent<Pair<String, Boolean>>>()

    private val loginEvent = MutableLiveData<SingleEvent<LoginEvent>>()

    private val pwResetEvent = MutableLiveData<SingleEvent<PassWordResetEvent>>()

    val getErrorMessage: LiveData<SingleEvent<Pair<String, Boolean>>>
        get() = errorMessage

    val getLoginEvent: LiveData<SingleEvent<LoginEvent>>
        get() = loginEvent

    val getPwResetEvent: LiveData<SingleEvent<PassWordResetEvent>>
        get() = pwResetEvent


    fun loginOnClick(email: String, password: String) {
        when {
            email.isValidEmailAddress() && password.isValidPassword() -> {
                // post a navigate event
                login(email, password)
                return
            }
        }
        loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
        displayErrorMessage("email" to email.isValidEmailAddress())
        displayErrorMessage("password" to password.isValidPassword())
    }

    fun registerOnClick(email: String, password: String, passwordConfirm: String, first_name: String = "", last_name: String = "") {
        when {
            email.isValidEmailAddress()
                    && password.isValidPassword()
                    && password.contentEquals(passwordConfirm)
                    && first_name.isNotBlank()
                    && last_name.isNotBlank() -> {
                register(email, password, "$first_name $last_name")
                return
            }
        }
        displayErrorMessage(("email" to email.isValidEmailAddress()))
        displayErrorMessage(("password" to password.isValidPassword()))
        displayErrorMessage(("passwordConfirm" to password.contentEquals(passwordConfirm)))
        displayErrorMessage(("firstName" to first_name.isNotBlank()))
        displayErrorMessage(("lastName" to last_name.isNotBlank()))
    }
    fun passwordResetOnClick(email: String){
        when{
            email.isValidEmailAddress() -> {
                passwordReset(email)
                return
            }
        }
        displayErrorMessage(("email" to email.isValidEmailAddress()))

    }





    fun logOut() {
        Timber.d("logOut if() is called")
//        FirebaseAuth.getInstance().signOut()
        loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_OUT))
//        user = auth.currentUser
    }

    private fun login(email: String, password: String) {
        loginEvent.value = SingleEvent(LoginEvent.VERIFYING)
        scope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "SignInWithEmail: Success")
                            user = auth.currentUser!!
                            loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_IN))
                        } else {
                            Log.w(TAG, "signInWithEmail: failure", task.exception)
                            loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
                        }
                    }
        }
    }

    private fun register(email: String, password: String, name: String) {
        loginEvent.value = SingleEvent(LoginEvent.VERIFYING)
        scope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail: success")
                             user = auth.currentUser!!
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                            user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d(TAG, "User profile updated.")
                                        }
                                    }


                            loginEvent.postValue(SingleEvent(LoginEvent.LOGGED_IN))
                            login(email, password)
                        } else {
                            loginEvent.postValue(SingleEvent(LoginEvent.FAILED))
                            Log.w(TAG, "createUserWithEmail: failure", task.exception)
                        }
                    }
        }
    }

    private fun passwordReset(email: String){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("emailsent", "Email sent.")
                            pwResetEvent.postValue(SingleEvent(PassWordResetEvent.SUCCESS))
                        }else{
                            Log.d("emailnotsent", "Email not sent")
                            pwResetEvent.postValue(SingleEvent(PassWordResetEvent.FAILED))
                        }
                    }
    }



    private fun displayErrorMessage(isValid: Pair<String, Boolean>) {
        errorMessage.value = SingleEvent(isValid)
    }


    override fun onCleared() {
        super.onCleared()
        displayErrorMessage(isValid = "all" to true)
    }

}