package com.yoron.nerdsoverflow.authActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.customViews.CoderWriterView
import com.yoron.nerdsoverflow.viewModels.AuthDataOrException
import com.yoron.nerdsoverflow.viewModels.Error
import com.yoron.nerdsoverflow.viewModels.ErrorType
import com.yoron.nerdsoverflow.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Animate the title
        animateTitle()

        //Listen for login clicks
        loginButtonClickListener()

        //Listen for the ViewModel observer
        viewModelObservers()

    }

    private fun viewModelObservers() {
        //Listen for edit texts errors
        viewModel.loginError.observe(this, loginObserver())

        //Listen for login await data or exception
        viewModel.dataOrException.observe(this, dataOrExceptionObserver())
    }


    /**
     * listen for the login button clicks
     */
    private fun loginButtonClickListener() {

        loginButton.setOnClickListener {
            loginEmailErrorTv.visibility = View.GONE
            loginPasswordErrorTv.visibility = View.GONE

            val email = loginEmailEt.text.toString()
            val password = loginPasswordEt.text.toString()

            if (viewModel.checkEditTexts(email, password)) {
                viewModel.loginToAccount(email, password)
            }
        }


    }


    private fun dataOrExceptionObserver() = Observer<AuthDataOrException> { dataOrException ->
        dataOrException.e?.let {
            when(it.errorCode){
                "ERROR_INVALID_EMAIL" -> setTvError(loginEmailErrorTv , "The email address is badly formatted.")
                "ERROR_INVALID_CREDENTIAL" -> setTvError(loginEmailErrorTv , "The supplied auth credential is malformed or has expired.")
                "ERROR_USER_DISABLED" -> setTvError(loginEmailErrorTv , "The user account has been disabled by an administrator.")
                "ERROR_USER_NOT_FOUND" -> setTvError(loginEmailErrorTv , "There is no user record corresponding to this identifier. The user may have been deleted.")
                "ERROR_WRONG_PASSWORD" -> setTvError(loginPasswordErrorTv , "The password is invalid or the user does not have a password.")
                else -> setTvError(loginEmailErrorTv , it.localizedMessage)

            }
            Log.v("looool", it.errorCode)
            return@Observer
        }


    }

    private fun loginObserver() = Observer<Error> { error ->
        when (error.type) {
            ErrorType.EMAIL -> {
                setTvError(loginEmailErrorTv,error.exception.localizedMessage)
            }
            ErrorType.PASSWORD -> {
                setTvError(loginPasswordErrorTv,error.exception.localizedMessage)
            }
        }
    }


    private fun setTvError(textView: CoderWriterView, error: String){
        textView.setWithMusic(false)
        textView.removeAnimation()
        textView.loopCursor = false
        textView.mDelay = 10
        textView.animateText(error)
        textView.visibility = View.VISIBLE
    }
    private fun animateTitle() {
        loginTitle.setWithMusic(true)
        loginTitle.animateText("NerdsOverFlow")
    }


    override fun onPause() {
        super.onPause()
        loginTitle.removeAnimation()
    }
}//Class