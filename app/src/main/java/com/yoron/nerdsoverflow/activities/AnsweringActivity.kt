/*
 *
 * Created by Obaida Al Mostarihi on 8/5/21, 10:33 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/5/21, 10:33 AM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.view_models.AnswersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_answering.*

@AndroidEntryPoint
class AnsweringActivity : AppCompatActivity() {

    private val answersViewModel: AnswersViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answering)

        val postId = intent.getStringExtra("postId")
        loadingDialog = LoadingDialog(this , false)


        answersViewModel.loading.observe(this){isLoading ->
            if (isLoading && !loadingDialog.isShowing){
                loadingDialog.show()
            }else{
                loadingDialog.dismiss()
                val data = Intent()
                data.putExtra("answered" , true)
                setResult(RESULT_OK , data)
                onBackPressed()
            }
        }

        answeringFab.setOnClickListener { _ ->
            if (checkValidation()) {
                postId?.let {
                    val answerModel = AnswerModel(
                        userReference = Firebase.auth.currentUser?.uid?.let { uid ->
                            Firebase.firestore.collection("Users").document(
                                uid
                            )
                        },
                        answer = answeringAnswerEt.text.toString().trim(),
                        code = answeringCode.getText()
                    )

                    answersViewModel.postAnswer(it, answerModel)

                }
            }
        }

        answerBackButton.setOnClickListener {
            onBackPressed()
        }
    }


    private fun checkValidation(): Boolean {
        var isValid = true

        if (answeringAnswerEt.text.isBlank()) {
            isValid = false
            answeringAnswerEt.error = ""
        }

        return isValid
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
    }
}//Class