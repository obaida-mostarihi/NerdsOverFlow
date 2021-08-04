/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.activities.DetailsActivity
import com.yoron.nerdsoverflow.activities.PostingActivity
import com.yoron.nerdsoverflow.auth_activities.LoginActivity
import com.yoron.nerdsoverflow.classes.asCircle
import com.yoron.nerdsoverflow.main_fragments.HomeFragment
import com.yoron.nerdsoverflow.view_models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private  val homeFragment: HomeFragment = HomeFragment()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Check if the user is logged in if not intent to login activity
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            return
        }
        setContentView(R.layout.activity_main)

        mainViewModel.checkForDetails()
        mainViewModel.hasDetails.observe(this){ dataOrException ->
            if (dataOrException.e != null) {
                val intent = Intent(this, DetailsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }else{
                dataOrException.data?.let { user ->
                    mainUserImage.asCircle()
                    mainUserImage.setImageURI(user.image)
                }
            }


        }


        mainNotificationsButton.setOnClickListener {
            Toast.makeText(this , "Coming soon!", Toast.LENGTH_LONG).show()
        }

        supportFragmentManager.beginTransaction().add(mainActivityContainer.id, homeFragment)
            .commit()




    }




}