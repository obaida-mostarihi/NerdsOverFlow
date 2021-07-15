/*
 *
 * Created by Obaida Al Mostarihi on 7/15/21, 6:14 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/15/21, 6:14 PM
 *
 */

package com.yoron.nerdsoverflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.PostAnswersAdapter.ViewHolder
import kotlinx.android.synthetic.main.text_row_item.view.*

class PostAnswersAdapter(val data: List<String>):
    RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val textView: TextView
        init {
            textView = view.testing_text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_row_item, parent, false)

        return ViewHolder(view)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = "loooooooool $position"
    }

    override fun getItemCount(): Int {
        return 50
    }
}