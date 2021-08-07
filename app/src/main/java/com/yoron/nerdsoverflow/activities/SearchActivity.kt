/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 7:16 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 7:16 PM
 *
 */

package com.yoron.nerdsoverflow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.OnUserClickListener
import com.yoron.nerdsoverflow.adapters.UsersSearchAdapter
import com.yoron.nerdsoverflow.models.UserModel
import com.yoron.nerdsoverflow.view_models.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*
@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), TextWatcher {
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        searchBackButton.setOnClickListener {
            onBackPressed()
        }

        val adapter = UsersSearchAdapter()

        searchRecyclerView.adapter = adapter

        searchViewModel.users.observe(this){
            adapter.setUsersList(it)
        }


        searchEt.addTextChangedListener(this)


        adapter.setOnUserClickListener(object: OnUserClickListener{
            override fun onUserClicked(user: UserModel) {
                val intent = Intent(this@SearchActivity , UserProfileActivity::class.java)
                intent.putExtra("uid" , user.uid)
                startActivity(intent)
            }

        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        searchViewModel.getUsersByName(s.toString())

    }

    override fun afterTextChanged(s: Editable?) = Unit
}