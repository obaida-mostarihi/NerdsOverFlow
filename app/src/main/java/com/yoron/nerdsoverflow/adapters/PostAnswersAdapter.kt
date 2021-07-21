/*
 *
 * Created by Obaida Al Mostarihi on 7/15/21, 6:14 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/15/21, 6:14 PM
 *
 */

package com.yoron.nerdsoverflow.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.getCircleRoundParams
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.models.UserModel
import kotlinx.android.synthetic.main.full_post_answer_row.view.*

class PostAnswersAdapter(var data: List<AnswerModel?>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val POST_TYPE = 0
    private val ANSWER_TYPE = 1

    init {
        data = listOf(null,
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(UserModel(), "I think if you just do this this will fix it", null),
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "I think if you just do this this will fix it", """try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}
            |try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "I think if you just do this this will fix it", """try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "I think if you just do this this will fix it", """try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "I think if you just do this this will fix it", """try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "This is how you solve the problem", """private void main(){
        |//this is comment
        |}""".trimMargin()
            ),
            AnswerModel(
                UserModel(), "I think if you just do this this will fix it", """try{
            |//your code
            |}catch(e: Exception){
            |//handle Errors Here
            |}""".trimMargin()
            )
        )
    }


    override fun getItemViewType(position: Int): Int {

        return if(data[position] == null){POST_TYPE}else{ANSWER_TYPE}

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ANSWER_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.full_post_answer_row, parent, false)

            return AnswerViewHolder(view, context)
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.full_post_top_layout, parent, false)

        return PostViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AnswerViewHolder){

            data[position]?.let { holder.setUserDetails(it) }
            holder.setCode(data[position]?.code)
            Log.v("loool" , "$position")

        }else if (holder is PostViewHolder){

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }




    class AnswerViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

        val usernameTv: TextView
        val userImage: SimpleDraweeView
        val userAnswer: TextView
        val answerDate: TextView
        val codeView: WebView

        init {
            usernameTv = view.answerUsername
            userImage = view.answerUserImage
            userAnswer = view.answerTv
            answerDate = view.answerDate
            codeView = view.answerCode
        }


        fun setUserDetails(answerModel: AnswerModel) {
            usernameTv.text = answerModel.user?.username
            userImage.setImageURI(answerModel.user?.image)
            userImage.hierarchy.roundingParams = getCircleRoundParams()
            userAnswer.text = answerModel.answer
        }

        fun setCode(code: String?) {
            if (code == null) {
                codeView.visibility = View.GONE
            } else {
                codeView.visibility = View.VISIBLE
//                Codeview.with(context)
//                    .withCode(code)
//                    .setStyle(Settings.WithStyle.GITHUB)
//                    .into(codeView)

            }
        }
    }

    class PostViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {



        init {

        }



    }

}