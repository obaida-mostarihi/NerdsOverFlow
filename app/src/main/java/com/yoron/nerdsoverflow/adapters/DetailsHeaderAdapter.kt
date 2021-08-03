/*
 *
 * Created by Obaida Al Mostarihi on 8/1/21, 10:31 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/1/21, 10:31 AM
 *
 */

package com.yoron.nerdsoverflow.adapters

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.DetailsHeaderAdapter.ViewHolder
import kotlinx.android.synthetic.main.details_header_layout.view.*

open class DetailsHeaderAdapter:
RecyclerView.Adapter<ViewHolder>() {

    private lateinit var searchTextWatcher: TextWatcher
    private lateinit var usernameTextWatcher: TextWatcher

    private var numberOfSelectedLanguages = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_header_layout, parent, false)

        return ViewHolder(view,searchTextWatcher , usernameTextWatcher)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setLanguagesNumber(numberOfSelectedLanguages)
    }

    override fun getItemCount(): Int {
        return 1
    }


    fun setNumberOfSelectedLanguages(number: Int){
        numberOfSelectedLanguages = number
        notifyDataSetChanged()
    }


    fun setSearchTextChangeListener(textWatcher: TextWatcher){
        searchTextWatcher = textWatcher
    }

    fun setUsernameTextChangeListener(textWatcher: TextWatcher){
        usernameTextWatcher = textWatcher
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

     class ViewHolder(val view: View, searchTextWatcher: TextWatcher, usernameTextWatcher: TextWatcher) : RecyclerView.ViewHolder(view){

        init {
            view.detailsCategorySearchEt.addTextChangedListener(searchTextWatcher)
            view.detailsUsernameEt.addTextChangedListener(usernameTextWatcher)

        }

         fun setLanguagesNumber(number: Int){
             view.selectedLanguagesNumber.text = number.toString()
         }


    }

}