/*
 *
 * Created by Obaida Al Mostarihi on 7/18/21, 9:07 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/18/21, 9:07 PM
 *
 */

package com.yoron.nerdsoverflow.mainFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.facebook.litho.*
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.java.OnPostClickedEvent
import com.yoron.nerdsoverflow.java.fullPost.FullPostDiffSection
import com.yoron.nerdsoverflow.java.home.HomePostsDiffSectionSection
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.models.UserModel
import com.yoron.nerdsoverflow.viewModels.AnswersViewModel
import com.yoron.nerdsoverflow.viewModels.HomePostsViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

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

        val hasEventDispatcher = HasEventDispatcher { getPostEventDispatcher(view , c) }

        val onPostClickEventEventHandler: EventHandler<OnPostClickedEvent> =
            EventHandler(hasEventDispatcher, 1, null)



        mComponentTree =
            ComponentTree.create(
                c, RecyclerCollectionComponent.create(c)
                    .section(
                        HomePostsDiffSectionSection.create(SectionContext(c))
                            .viewModel(homePostsViewModel)
                            .onPostClickedEventHandler(onPostClickEventEventHandler)

                    )
                    .clipToPadding(false)
                    .clipChildren(false)
                    .refreshProgressBarColor(ContextCompat.getColor(requireActivity(), R.color.greenColor))
                    .build()
            )
                .stateHandler(homePostsViewModel.getStateHandler())
                .build()





        view.lithoView.componentTree = mComponentTree
        return view
    }

    /**
     * This triggers when the post is clicked.
     */
    fun getPostEventDispatcher(view: View , c: ComponentContext) =
        EventDispatcher { eventHandler, eventState ->
            val onClickEvent = eventState as OnPostClickedEvent
            view.customBottomSheet.animateToTop()
            mFullPostComponentTree =
                ComponentTree.create(
                    c, RecyclerCollectionComponent.create(c)
                        .section(
                            FullPostDiffSection.create(SectionContext(c))
                                .post(onClickEvent.post)
                                .viewModel(answersViewModel)
                        )
                        .clipToPadding(false)
                        .clipChildren(false)
                        .focusable(true)
                        .disablePTR(true)
                        .refreshProgressBarColor(ContextCompat.getColor(requireActivity(), R.color.greenColor))
                        .build()
                )
                    .build()

            view.fullPostLithoView.componentTree = mFullPostComponentTree
            null
        }


    override fun onDestroy() {
        super.onDestroy()
        homePostsViewModel.updateStateHandler(mComponentTree)
    }

}