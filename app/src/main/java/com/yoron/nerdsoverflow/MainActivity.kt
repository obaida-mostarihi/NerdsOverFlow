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
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentTree
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.authActivities.LoginActivity
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.java.HomePostsDiffSectionSection
import com.yoron.nerdsoverflow.viewModels.HomePostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    private var mComponentTree: ComponentTree? = null
    private val homePostsViewModel: HomePostsViewModel by viewModels()
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

        val c = ComponentContext(this)

        mComponentTree =
            ComponentTree.create(
                c, RecyclerCollectionComponent.create(c)
                    .section(
                        HomePostsDiffSectionSection.create(SectionContext(c))
                            .viewModel(homePostsViewModel)
                    )

                    .clipToPadding(false)
                    .clipChildren(false)
                    .refreshProgressBarColor(ContextCompat.getColor(this ,R.color.greenColor))
                    .build()
            )
                .stateHandler(homePostsViewModel.getStateHandler())
                .build()




        setContentView(R.layout.activity_main)
        lithoView.componentTree = mComponentTree



    }

    override fun onDestroy() {
        super.onDestroy()
        homePostsViewModel.updateStateHandler(mComponentTree)

    }
}