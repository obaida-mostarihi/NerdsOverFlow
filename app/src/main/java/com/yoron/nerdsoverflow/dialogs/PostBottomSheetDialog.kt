/*
 *
 * Created by Obaida Al Mostarihi on 7/15/21, 4:46 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/15/21, 4:46 PM
 *
 */

package com.yoron.nerdsoverflow.dialogs

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentTree
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.models.HomePostModel
import kotlinx.android.synthetic.main.full_post_layout.view.*


class PostBottomSheetDialog() : BottomSheetDialogFragment() {

    private lateinit var post: HomePostModel
    private var mComponentTree: ComponentTree? = null

    private var mBehavior: BottomSheetBehavior<*>? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.full_post_layout, null)

//        dialog.setOnShowListener { dia ->
//            val linearLayout = view.findViewById<LinearLayout>(R.id.fullPostRoot)
//            val params = linearLayout.layoutParams
//            params.height = getScreenHeight()
//            linearLayout.layoutParams = params
//            val bottomSheet = dialog
//                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
//
//            if (bottomSheet != null) {
//                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//                behavior.isDraggable = false
//
//                view.fullPostTopView.setOnTouchListener { v, event ->
//                    if(v.id == view.fullPostTopView.id )
//                    behavior.isDraggable = true
//                    false
//                }
//
//            }
//        }
//
//
//        val c = ComponentContext(requireContext())
//        mComponentTree =
//            ComponentTree.create(
//                c, RecyclerCollectionComponent.create(c)
//                    .section(
//                        SimpleDiffSectionSection.create(SectionContext(c))
//                            .data(listOf("loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy" ))
//                    )
//                    .clipToPadding(false)
//                    .clipChildren(false)
//                    .focusable(true)
//                    .refreshProgressBarColor(ContextCompat.getColor(requireActivity(), R.color.greenColor))
//                    .build()
//            )
//                .build()
//
//
//
//
//
//        view.lithoView.componentTree = mComponentTree
//

        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)

        return dialog
    }


    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
//
//        val view = inflater.inflate(R.layout.full_post_layout, container, false)
//
//
//        if (this::post.isInitialized)
//            post.let { post ->
////                view.postQuestionTv.text = post.title
////
////                view.fullPostDesc.text = post.question
////
////                checkIfAnswered(view)
////
////                showUserProvidedCode(view, post)
////
////                setUsernameAndDate(view)
////
////                setUserImage(view)
//
//
////                view.postAnswersRv.isNestedScrollingEnabled = false
////                view.postAnswersRv.adapter = PostAnswersAdapter(emptyList(), requireContext())
////                view.postAnswersRv.setOnTouchListener { v, event ->
////                    v.parent.requestDisallowInterceptTouchEvent(true);
////                    v.onTouchEvent(event)
////                     true
////                }
//
//
//            }
//        val c = ComponentContext(requireContext())
//
//
//
//
//
//        mComponentTree =
//            ComponentTree.create(
//                c, RecyclerCollectionComponent.create(c)
//                    .section(
//                      SimpleDiffSectionSection.create(SectionContext(c))
//                          .data(listOf("loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy","loool" , "heyyy" ))
//                    )
//                    .clipToPadding(false)
//                    .clipChildren(false)
//                    .focusable(true)
//                    .refreshProgressBarColor(ContextCompat.getColor(requireActivity(), R.color.greenColor))
//                    .build()
//            )
//                .build()
//
//
//
//
//
//        view.lithoView.componentTree = mComponentTree
//
//        return view
//    }
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
//    }
//    /**
//     * If the post is answered set the visibility to the textView to VISIBLE else to GONE.
//     */
//    private fun checkIfAnswered(view: View) {
//        view.fullPostAnsweredTv.visibility = if (post.answered == true) {
//            View.VISIBLE
//        } else {
//            View.GONE
//        }
//    }
//
//    /**
//     * Set the user's username and formatted date in a new thread.
//     */
//    private fun setUsernameAndDate(view: View) {
//        view.fullPostUsername.text = post.user?.username
//        Thread {
//            run {
//                val yearFormat = SimpleDateFormat("MM/dd", Locale.US)
//                val hourFormat = SimpleDateFormat("HH:mm", Locale.US)
//                var year = ""
//                var hour = ""
//                post.timestamp?.let {
//                    year = yearFormat.format(it.toDate())
//                    hour = hourFormat.format(it.toDate())
//                }
//
//                val date = String.format("%s at %s", year, hour)
//
//                requireActivity().runOnUiThread {
//                    view.fullPostDate.text = date
//                }
//            }
//        }.start()
//    }
//
//
//    /**
//     * Set the user's image with circle shaped.
//     */
//    private fun setUserImage(view: View) {
//        view.fullPostUserImage.setImageURI(post.user?.image)
//        view.fullPostUserImage.hierarchy.roundingParams = getCircleRoundParams()
//    }
//
//
//    /**
//     * Show the code view if the user provided any code.
//     */
//    private fun showUserProvidedCode(view: View, post: HomePostModel) {
//        if (post.code == null) {
//            view.codeView.visibility = View.GONE
//        } else {
//            view.codeView.visibility = View.VISIBLE
//            Codeview.with(requireContext())
//                .withCode(post.code)
//                .setStyle(Settings.WithStyle.GITHUB)
//                .into(view.codeView)
//        }
//    }


    /**
     * Set the post data.
     * @param post is the HomePostModel of the post.
     */
    fun setPostDetails(post: HomePostModel) {
        this.post = post
    }
//
//    override fun getCornerRadius(): Float {
//        return 30f.toPx()
//    }
//
//
//    override fun getExpandedHeight(): Int {
//        return -1
//    }
//
//    override fun animateCornerRadius(): Boolean {
//        return false
//    }
//
//    override fun isSheetAlwaysExpanded(): Boolean {
//        return true
//    }
//
//    override fun animateStatusBar(): Boolean {
//        return true
//    }



}