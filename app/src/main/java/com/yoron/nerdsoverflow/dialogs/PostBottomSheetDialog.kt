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
import com.yoron.nerdsoverflow.models.HomePostModel
import kotlinx.android.synthetic.main.full_post_layout.view.*

class PostBottomSheetDialog (val post: HomePostModel): SuperBottomSheetFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.full_post_layout, container, false)
        view.postQuestionTv.text = post.question
        //your string code
        if(post.code == null){
            view.codeView.visibility  = View.GONE
        }else{
            view.codeView.visibility  = View.VISIBLE
            Codeview.with(requireContext())
                .withCode(post.code)
                .setStyle(Settings.WithStyle.GITHUB)
                .into(view.codeView)
        }

        view.postAnswersRv.adapter = PostAnswersAdapter(emptyList())


        return view
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