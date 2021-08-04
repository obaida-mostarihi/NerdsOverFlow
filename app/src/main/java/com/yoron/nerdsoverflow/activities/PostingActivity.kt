/*
 *
 * Created by Obaida Al Mostarihi on 8/3/21, 9:59 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/3/21, 9:59 AM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.ProgrammingLanguagesAdapter
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.view_models.DetailsViewModel
import com.yoron.nerdsoverflow.view_models.PostingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_posting.*
import me.testica.codeeditor.SyntaxHighlightRule
import org.w3c.dom.Text

@AndroidEntryPoint
class PostingActivity : AppCompatActivity() {

    private val detailsViewModel: DetailsViewModel by viewModels()
    private val postingViewModel: PostingViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var programmingLanguagesAdapter: ProgrammingLanguagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        loadingDialog = LoadingDialog(this, false)
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }
        postingLanguagesRv.layoutManager = layoutManager

        programmingLanguagesAdapter = ProgrammingLanguagesAdapter(ArrayList(), true)
        postingLanguagesRv.adapter = programmingLanguagesAdapter

        postingCodeEditor.setSyntaxHighlightRules(
            SyntaxHighlightRule("[0-9]*", "#00838f"),
            SyntaxHighlightRule("/\\\\*(?:.|[\\\\n\\\\r])*?\\\\*/|(?<!:)//.*", "#9ea7aa")
        )


        programmingLanguagesAdapter.setSelectionListener(object :
            ProgrammingLanguagesAdapter.SelectedLanguagesListener {
            override fun selectedItem(selectedItem: String) {
                super.selectedItem(selectedItem)
                postingProgrammingLanguage.setText(selectedItem)
                postingCategorySearchEt.hideKeyboard()
                postingBottomSheet.animateToBottom()

            }
        })


        detailsViewModel.languages.observe(this) {
            programmingLanguagesAdapter.addAllItems(it)
        }

        postingProgrammingLanguage.setOnTouchListener { v, event ->
            postingBottomSheet.animateToTop()
            true
        }

        postingCategorySearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                programmingLanguagesAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })


        postingTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postingTitleLength.text = String.format("%d/%d", s?.length, 100)
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })



        postingFab.setOnClickListener {
            if (checkValidation()) {
                val homePostModel = HomePostModel(
                    userReference = Firebase.auth.currentUser?.uid?.let { it1 ->
                        Firebase.firestore.collection("Users").document(
                            it1
                        )
                    },
                    title = postingTitleEt.text.toString().trim(),
                    question = postingQuestionEt.text.toString().trim(),
                    language = postingProgrammingLanguage.text.toString(),
                    code = postingCodeEditor.getText()
                )
                postingViewModel.publishPost(homePostModel)
            }
        }

        postingViewModel.loading.observe(this) { isLoading ->

            if (isLoading && !loadingDialog.isShowing)
                loadingDialog.show()
            else {
                loadingDialog.dismiss()
                onBackPressed()
            }

        }
    }

    private fun checkValidation(): Boolean {
        var isValid = true

        if (postingTitleEt.text.toString().isBlank()) {
            isValid = false
            postingTitleEt.error = ""
        }

        if (postingQuestionEt.text.toString().isBlank()) {
            isValid = false
            postingQuestionEt.error = ""
        }

        if (postingProgrammingLanguage.text.toString().isBlank()) {
            isValid = false
            postingProgrammingLanguage.error = ""
        }


        return isValid
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down)

    }
}