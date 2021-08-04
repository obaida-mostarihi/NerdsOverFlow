/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.auth_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuthException
import com.yoron.nerdsoverflow.MainActivity
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.custom_views.CoderWriterView
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.view_models.AuthDataOrException
import com.yoron.nerdsoverflow.view_models.Error
import com.yoron.nerdsoverflow.view_models.ErrorType
import com.yoron.nerdsoverflow.view_models.LoginViewModel
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


    /**
     * Setup all view model observers
     */
    private fun viewModelObservers() {
        val loadingDialog = LoadingDialog(this, false)

        //Listen for edit texts errors
        viewModel.loginError.observe(this, loginErrorObserver())

        //Listen for login await data or exception
        viewModel.dataOrException.observe(this, dataOrExceptionObserver())

        //Listen for the loading event
        viewModel.loading.observe(this) { loading ->
            if (loading) {
                loadingDialog.show()
            } else {
                if (loadingDialog.isShowing)
                    loadingDialog.dismiss()
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
                viewModel.loginToAccount(email, password)
            }
        }


    }

    /**
     * Check if the observer returns error or data if error then show the error else intent to MainActivity.class.
     */
    private fun dataOrExceptionObserver() = Observer<AuthDataOrException> { dataOrException ->
        dataOrException.e?.let {
            try {
                it as FirebaseAuthException
                when (it.errorCode) {
                    "ERROR_INVALID_EMAIL" -> setTvError(
                        loginEmailErrorTv,
                        "The email address is badly formatted."
                    )
                    "ERROR_INVALID_CREDENTIAL" -> setTvError(
                        loginEmailErrorTv,
                        "The supplied auth credential is malformed or has expired."
                    )
                    "ERROR_USER_DISABLED" -> setTvError(
                        loginEmailErrorTv,
                        "The user account has been disabled by an administrator."
                    )
                    "ERROR_USER_NOT_FOUND" -> setTvError(
                        loginEmailErrorTv,
                        "There is no user record corresponding to this identifier. The user may have been deleted."
                    )
                    "ERROR_WRONG_PASSWORD" -> setTvError(
                        loginPasswordErrorTv,
                        "The password is invalid or the user does not have a password."
                    )
                    else -> it.nonErrorMsg(loginEmailErrorTv)


                }
            } catch (e: Exception) {
                it.nonErrorMsg(loginEmailErrorTv)
            }

            return@Observer
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    private fun Exception.nonErrorMsg(textView: CoderWriterView){
        this.localizedMessage?.let { msg ->
            setTvError(textView, msg)

        }
    }
    private fun loginErrorObserver() = Observer<Error> { error ->
        when (error.type) {
            ErrorType.EMAIL -> error.exception.nonErrorMsg(loginEmailErrorTv)

            ErrorType.PASSWORD -> error.exception.nonErrorMsg(loginPasswordErrorTv)

        }
    }


    private fun setTvError(textView: CoderWriterView, error: String) {
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