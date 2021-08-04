/*
 *
 * Created by Obaida Al Mostarihi on 7/26/21, 1:25 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/26/21, 1:25 AM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yoron.nerdsoverflow.MainActivity
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.DetailsHeaderAdapter
import com.yoron.nerdsoverflow.adapters.ProgrammingLanguagesAdapter
import com.yoron.nerdsoverflow.dialogs.LoadingDialog
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import com.yoron.nerdsoverflow.view_models.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*
import org.aviran.cookiebar2.CookieBar


@AndroidEntryPoint
class DetailsActivity : FragmentActivity(), ProgrammingLanguagesAdapter.SelectedLanguagesListener {

    private val detailsViewModel: DetailsViewModel by viewModels()
    private lateinit var programmingLanguagesAdapter: ProgrammingLanguagesAdapter
    private lateinit var headerAdapter: DetailsHeaderAdapter

    private lateinit var loadingDialog: LoadingDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            flexWrap = FlexWrap.WRAP
        }
        loadingDialog = LoadingDialog(this, false)

        programmingLanguagesRecyclerView.layoutManager = layoutManager

        programmingLanguagesAdapter = ProgrammingLanguagesAdapter(ArrayList())
        headerAdapter = DetailsHeaderAdapter()
        programmingLanguagesRecyclerView.adapter =
            ConcatAdapter(headerAdapter, programmingLanguagesAdapter)

        detailsViewModel.languages.observe(this) {
            programmingLanguagesAdapter.addAllItems(it)
        }

        programmingLanguagesRecyclerView.addOnScrollListener(recyclerviewScrollListener())



        detailsSubmitFab.setOnClickListener {
            detailsViewModel.registerDetails()
        }

        programmingLanguagesAdapter.setSelectionListener(this)

        headerAdapter.setSearchTextChangeListener(searchTextWatcher())
        headerAdapter.setUsernameTextChangeListener(usernameTextWatcher())


        detailsViewModel.dataOrException.observe(this) { dataOrException ->
            if (dataOrException.data == true) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                dataOrException.e?.let {
                    CookieBar.build(this)
                        .setTitle("Error")
                        .setMessage(it.localizedMessage)
                        .setBackgroundColor(R.color.darkErrorColor)
                        .setDuration(3000)
                        .show()

                }
            }
        }

        detailsViewModel.loading.observe(this) { isLoading ->
            if (isLoading && !loadingDialog.isShowing)
                loadingDialog.show()
            else
                loadingDialog.dismiss()

        }
    }


    private fun recyclerviewScrollListener() = object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 || dy < 0 && detailsSubmitFab.isExtended) {
                detailsSubmitFab.shrink()

            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                detailsSubmitFab.extend()
            }
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

    private fun searchTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            programmingLanguagesAdapter.filter.filter(s)
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private fun usernameTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            detailsViewModel.setUsername(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }


    override fun selectedList(selectedLanguages: ArrayList<ProgrammingLanguageModel>) {
        headerAdapter.setNumberOfSelectedLanguages(selectedLanguages.size)
        detailsViewModel.setSelectedLanguages(selectedLanguages)

    }


}