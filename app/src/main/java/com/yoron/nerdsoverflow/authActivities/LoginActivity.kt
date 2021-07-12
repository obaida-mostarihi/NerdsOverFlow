package com.yoron.nerdsoverflow.authActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.viewModels.ErrorType
import com.yoron.nerdsoverflow.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val viewModel: LoginViewModel by viewModels()

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
        viewModel.loginError.observe(this) { error ->
            when (error.type) {
                ErrorType.EMAIL -> {
                    loginEmailErrorTv.setWithMusic(false)
                    loginEmailErrorTv.removeAnimation()
                    loginEmailErrorTv.loopCursor = false
                    loginEmailErrorTv.mDelay = 10
                    loginEmailErrorTv.animateText(error.exception.localizedMessage)
                    loginEmailErrorTv.visibility = View.VISIBLE

                }
                ErrorType.PASSWORD -> {
                    loginPasswordErrorTv.setWithMusic(false)
                    loginPasswordErrorTv.removeAnimation()
                    loginPasswordErrorTv.loopCursor = false
                    loginPasswordErrorTv.mDelay = 10
                    loginPasswordErrorTv.animateText(error.exception.localizedMessage)
                    loginPasswordErrorTv.visibility = View.VISIBLE
                }
            }
        }
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


            }
        }


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