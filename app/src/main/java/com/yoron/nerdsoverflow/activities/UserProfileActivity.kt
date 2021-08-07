/*
 *
 * Created by Obaida Al Mostarihi on 8/5/21, 10:05 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/5/21, 10:05 AM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentTree
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.facebook.yoga.YogaEdge
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.classes.BetterActivityResult
import com.yoron.nerdsoverflow.classes.asCircle
import com.yoron.nerdsoverflow.classes.toPx
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.java.fullPost.FullPostDiffSection
import com.yoron.nerdsoverflow.java.home.UserPostsDiffSectionSection
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import com.yoron.nerdsoverflow.view_models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.full_post_layout.*
import kotlinx.android.synthetic.main.full_post_layout.view.*


@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity(), HomePostListeners, View.OnClickListener {
    private val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)
    private val mainViewModel: MainViewModel by viewModels()
    private var mComponentTree: ComponentTree? = null
    private var mFullPostComponentTree: ComponentTree? = null

    private val homePostsViewModel: UserProfileViewModel by viewModels()
    private val answersViewModel: AnswersViewModel by viewModels()
    lateinit var loadingDialog: LoadingDialog
    lateinit var params: FlexboxLayout.LayoutParams

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(
            10f.toPx().toInt(),
            10f.toPx().toInt(),
            10f.toPx().toInt(),
            10f.toPx().toInt()
        )
        loadingDialog = LoadingDialog(this, false)
        val uid = intent.getStringExtra("uid")


        val userRef = uid?.let { Firebase.firestore.collection("Users").document(it) }
        userRef?.let { homePostsViewModel.loadData(it) }
        uid?.let { mainViewModel.checkForDetails(it) }
        mainViewModel.hasDetails.observe(this) {
            it.data?.let { user ->
                profileUserImage.apply {
                    asCircle()
                    setImageURI(user.image)
                }

                profileUsername.apply {
                    setWithMusic(false)
                    loopCursor = false
                    animateText("@${user.username}")
                }

                user.selectedLanguages?.let { languages ->
                    profileFlexBox.removeAllViews()
                    setLanguages(languages)


                }


            }

        }


        val c = ComponentContext(this)

        homePostsViewModel.setHomePostListeners(this)
        mComponentTree =
            ComponentTree.create(
                c, RecyclerCollectionComponent.create(c)
                    .section(
                        UserPostsDiffSectionSection.create(SectionContext(c))
                            .viewModel(homePostsViewModel)
                            .userRef(userRef)
                            .onPostClickedEventHandler(homePostsViewModel.onPostClickEventEventHandler)

                    )
                    .clipToPadding(false)
                    .clipChildren(false)
                    .marginDip(YogaEdge.TOP, -10f)
                    .disablePTR(true)
                    .refreshProgressBarColor(
                        ContextCompat.getColor(
                            this,
                            R.color.greenColor
                        )
                    )
                    .build()
            )
                .stateHandler(homePostsViewModel.getStateHandler())
                .build()


        profileLithoView.componentTree = mComponentTree

        profileBackButton.setOnClickListener {
            onBackPressed()
        }
        if (uid.equals(Firebase.auth.currentUser?.uid)) {
            profileUserImage.setOnClickListener(this)
            profileEditButton.setOnClickListener(this)
            profileLanguagesEditButton.setOnClickListener(this)
            profileUserImage.visibility = View.VISIBLE
            profileEditButton.visibility = View.VISIBLE
            profileLanguagesEditButton.visibility = View.VISIBLE
        } else {
            profileUserImage.visibility = View.GONE
            profileEditButton.visibility = View.GONE
            profileLanguagesEditButton.visibility = View.GONE
        }

        mainViewModel.loading.observe(this) {
            if (it && !loadingDialog.isShowing) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }


    }


    private fun setLanguages(languages: ArrayList<ProgrammingLanguageModel>) {
        repeat(languages.size) {
            val text = TextView(this)
            text.text = languages[it].language
            text.setBackgroundResource(R.drawable.chip_shape)
            text.setPadding(
                7f.toPx().toInt(),
                4f.toPx().toInt(),
                7f.toPx().toInt(),
                4f.toPx().toInt()
            )
            text.setTextColor(Color.BLACK)
            text.layoutParams = params
            profileFlexBox.addView(text)
        }
    }


    override fun onPostClicked(post: HomePostModel) {
        super.onPostClicked(post)
        profileBottomSheet.animateToTop()
        profileAppBarLayout.setExpanded(false, true)
        val c = ComponentContext(this)
        mFullPostComponentTree =
            ComponentTree.create(
                c, RecyclerCollectionComponent.create(c)
                    .section(
                        FullPostDiffSection.create(SectionContext(c))
                            .post(post)
                            .viewModel(answersViewModel)
                            .onCodeClickEventHandler(answersViewModel.onCodeClickEventEventHandler)
                    )
                    .clipToPadding(false)
                    .clipChildren(false)
                    .focusable(true)
                    .disablePTR(true)
                    .bottomPaddingDip(100f)
                    .refreshProgressBarColor(
                        ContextCompat.getColor(
                            this,
                            R.color.greenColor
                        )
                    )
                    .build()
            )
                .stateHandler(answersViewModel.getStateHandler())
                .build()
        fullPostLithoView.componentTree = mFullPostComponentTree
        homeAnswerFab.setOnClickListener {
            val intent = Intent(this, AnsweringActivity::class.java)
            intent.putExtra("postId", post.postId)
            post.postId?.let { id -> startAnsweringActivity(intent, id) }

            overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
        }

    }

    private fun startAnsweringActivity(intent: Intent, postId: String) =
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    if (it.getBooleanExtra("answered", false)) {
                        answersViewModel.loadAnswers(postId)
                    }
                }
            }
        }

    override fun onClick(v: View?) {
        if (v == profileUserImage || v == profileEditButton) {
            activityLauncher.launch(
                ImagePicker.with(this)
                    .galleryOnly()
                    .createIntent()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = it.data?.data!!
                    mainViewModel.setUserImage(uri)
                }

            }
        }


        if (v == profileLanguagesEditButton) {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("onlyLanguages", true)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }


    override fun onStop() {
        super.onStop()
        profileUsername.removeAnimation()

    }
}