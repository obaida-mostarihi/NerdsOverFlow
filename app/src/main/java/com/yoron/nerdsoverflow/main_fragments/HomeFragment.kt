/*
 *
 * Created by Obaida Al Mostarihi on 7/18/21, 9:07 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/18/21, 9:07 PM
 *
 */

package com.yoron.nerdsoverflow.main_fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.facebook.litho.*
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.facebook.yoga.YogaEdge
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.activities.AnsweringActivity
import com.yoron.nerdsoverflow.activities.FullScreenCodeActivity
import com.yoron.nerdsoverflow.activities.PostingActivity
import com.yoron.nerdsoverflow.classes.BetterActivityResult
import com.yoron.nerdsoverflow.java.fullPost.FullPostDiffSection
import com.yoron.nerdsoverflow.java.home.HomePostsDiffSectionSection
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.view_models.AnswersViewModel
import com.yoron.nerdsoverflow.view_models.FullAnswerListeners
import com.yoron.nerdsoverflow.view_models.HomePostListeners
import com.yoron.nerdsoverflow.view_models.HomePostsViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.full_post_layout.view.*


class HomeFragment(val activityLauncher: BetterActivityResult<Intent, ActivityResult>) : Fragment(), HomePostListeners, FullAnswerListeners {

    private var mComponentTree: ComponentTree? = null
    private var mFullPostComponentTree: ComponentTree? = null

    private val homePostsViewModel: HomePostsViewModel by activityViewModels()
    private val answersViewModel: AnswersViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val c = ComponentContext(requireContext())

        homePostsViewModel.setHomePostListeners(this)
        answersViewModel.setFullAnswerListeners(this)

        mComponentTree =
            ComponentTree.create(
                c, RecyclerCollectionComponent.create(c)
                    .section(
                        HomePostsDiffSectionSection.create(SectionContext(c))
                            .viewModel(homePostsViewModel)
                            .onPostClickedEventHandler(homePostsViewModel.onPostClickEventEventHandler)

                    )
                    .marginDip(YogaEdge.TOP , 30f)
                    .clipToPadding(false)
                    .clipChildren(false)
                    .bottomPaddingDip(100f)
                    .topPaddingDip(15f)
                    .refreshProgressBarColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.greenColor
                        )
                    )
                    .build()
            )
                .stateHandler(homePostsViewModel.getStateHandler())
                .build()


        view.mainPostingFab.setOnClickListener{

            val intent = Intent(requireContext(), PostingActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.bottom_up, R.anim.nothing);

        }


        view.lithoView.componentTree = mComponentTree

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        homePostsViewModel.updateStateHandler(mComponentTree)
        answersViewModel.updateStateHandler(mFullPostComponentTree)
    }


    override fun onCodeClicked(code: String) {
        super.onCodeClicked(code)
        val intent = Intent(requireContext() , FullScreenCodeActivity::class.java)
        intent.putExtra("code" , code)
        requireActivity().startActivity(intent)
    }

    override fun onPostClicked(post: HomePostModel) {
        super.onPostClicked(post)
        view?.let { view ->
            val c = ComponentContext(requireContext())
            view.customBottomSheet.animateToTop()
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
                                requireActivity(),
                                R.color.greenColor
                            )
                        )
                        .build()
                )
                    .stateHandler(answersViewModel.getStateHandler())
                    .build()
            view.fullPostLithoView.componentTree = mFullPostComponentTree

            view.homeAnswerFab.setOnClickListener {
                val intent = Intent(requireContext() , AnsweringActivity::class.java)
                intent.putExtra("postId" , post.postId)
                post.postId?.let { id ->   startAnsweringActivity(intent , id)}

                requireActivity().overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
            }
        }
    }


    private fun startAnsweringActivity(intent: Intent , postId: String) = activityLauncher.launch(intent){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let{
                if(it.getBooleanExtra("answered" , false)){
                    answersViewModel.loadAnswers(postId)
                }
            }
        }
    }



}