/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 2:27 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 2:27 PM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.yoron.nerdsoverflow.MainActivity
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.custom_views.CoderWriterView
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.view_models.ErrorType
import com.yoron.nerdsoverflow.view_models.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()
    lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadingDialog = LoadingDialog(this, false)
        registerTitle.animateText("Create a new account")
        registerButton.setOnClickListener {
            val email = registerEmailEt.text.toString().trim()
            val password = registerPasswordEt.text.toString()
            val retypePassword = registerRetypePassword.text.toString()

            registerPasswordErrorTv.visibility = View.GONE
            registerEmailErrorTv.visibility = View.GONE
            registerRePasswordErrorTv.visibility = View.GONE
            if (registerViewModel.checkEditTexts(email, password, retypePassword)) {
                registerViewModel.registerAnAccount(email, password)
            }
        }




        registerViewModel.loading.observe(this) { isLoading ->
            if (isLoading && !loadingDialog.isShowing) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }


        registerViewModel.registerError.observe(this){ error ->
            when (error.type) {
                ErrorType.EMAIL -> error.exception.nonErrorMsg(registerEmailErrorTv)

                ErrorType.PASSWORD -> error.exception.nonErrorMsg(registerPasswordErrorTv)

                ErrorType.RETYPE_PASSWORD -> error.exception.nonErrorMsg(registerRePasswordErrorTv)

            }

        }

        registerViewModel.dataOrException.observe(this) { authResult ->
            authResult.e?.let {
                try {
                    it as FirebaseAuthException
                    Log.v("lool" , it.message.toString())
                    when (it) {
                        is FirebaseAuthWeakPasswordException -> {
                            setTvError(registerPasswordErrorTv , "The chosen password is too weak.")
                        }
                        is FirebaseAuthUserCollisionException -> {
                            setTvError(registerEmailErrorTv , "The email address is already in use by another account.")

                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            setTvError(registerEmailErrorTv , "The email address is badly formatted.")
                        }
                        else -> {

                            setTvError(registerEmailErrorTv , "Something went wrong.")
                        }
                    }
                } catch (e: Exception) {
                    it.nonErrorMsg(registerEmailErrorTv)
                }

                return@observe
            }


            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


        }


    }

    private fun Exception.nonErrorMsg(textView: CoderWriterView){
        this.localizedMessage?.let { msg ->
            setTvError(textView, msg)
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


    override fun onStop() {
        super.onStop()
        registerTitle.removeAnimation()
    }
}