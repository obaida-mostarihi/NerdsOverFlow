/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.authActivities.LoginActivity
import com.yoron.nerdsoverflow.dialogs.LoadingDialog

class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Check if the user is logged in if not intent to login activity
        if(auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return
        }


        setContentView(R.layout.activity_main)



    }
}