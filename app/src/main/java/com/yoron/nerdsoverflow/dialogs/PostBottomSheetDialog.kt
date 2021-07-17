/*
 *
 * Created by Obaida Al Mostarihi on 7/15/21, 4:46 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/15/21, 4:46 PM
 *
 */

package com.yoron.nerdsoverflow.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.protectsoft.webviewcode.Codeview
import com.protectsoft.webviewcode.Settings
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.PostAnswersAdapter
import com.yoron.nerdsoverflow.classes.toPx
import com.yoron.nerdsoverflow.getCircleRoundParams
import com.yoron.nerdsoverflow.models.HomePostModel
import kotlinx.android.synthetic.main.full_post_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostBottomSheetDialog : SuperBottomSheetFragment() {

    private lateinit var post: HomePostModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.full_post_layout, container, false)

        post.let { post ->
            view.postQuestionTv.text = post.title

            view.fullPostDesc.text = post.question

            checkIfAnswered(view)

            showUserProvidedCode(view, post)

            setUsernameAndDate(view)

            setUserImage(view)

            view.postAnswersRv.adapter = PostAnswersAdapter(emptyList())
        }

        return view
    }

    /**
     * If the post is answered set the visibility to the textView to VISIBLE else to GONE.
     */
    private fun checkIfAnswered(view: View) {
        view.fullPostAnsweredTv.visibility = if (post.answered == true){View.VISIBLE}else{View.GONE}
    }

    /**
     * Set the user's username and formatted date in a new thread.
     */
    private fun setUsernameAndDate(view: View) {
        view.fullPostUsername.text = post.user?.username
        Thread {
            run {
                val yearFormat = SimpleDateFormat("MM/dd", Locale.US)
                val hourFormat = SimpleDateFormat("HH:mm", Locale.US)
                var year = ""
                var hour = ""
                post.timestamp?.let {
                    year = yearFormat.format(it.toDate())
                    hour = hourFormat.format(it.toDate())
                }

                val date = String.format("%s at %s", year, hour)

                requireActivity().runOnUiThread {
                    view.fullPostDate.text = date
                }
            }
        }.start()
    }

    /**
     * Set the user's image with circle shaped.
     */
    private fun setUserImage(view: View) {
        view.fullPostUserImage.setImageURI(post.user?.image)
        view.fullPostUserImage.hierarchy.roundingParams = getCircleRoundParams()
    }


    /**
     * Show the code view if the user provided any code.
     */
    private fun showUserProvidedCode(view: View, post: HomePostModel) {
        if (post.code == null) {
            view.codeView.visibility = View.GONE
        } else {
            view.codeView.visibility = View.VISIBLE
            Codeview.with(requireContext())
                .withCode(post.code)
                .setStyle(Settings.WithStyle.GITHUB)
                .into(view.codeView)
        }
    }


    /**
     * Set the post data.
     * @param post is the HomePostModel of the post.
     */
    fun setPostDetails(post: HomePostModel) {
        this.post = post
    }

    override fun getCornerRadius(): Float {
        return 30f.toPx()
    }


    override fun getExpandedHeight(): Int {
        return -1
    }

    override fun animateCornerRadius(): Boolean {
        return false
    }

    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

    override fun animateStatusBar(): Boolean {
        return true
    }
}