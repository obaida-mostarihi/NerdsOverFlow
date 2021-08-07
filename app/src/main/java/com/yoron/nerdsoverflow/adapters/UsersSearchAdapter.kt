/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 7:56 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 7:56 PM
 *
 */

package com.yoron.nerdsoverflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yoron.nerdsoverflow.R
import com.yoron.nerdsoverflow.adapters.UsersSearchAdapter.*
import com.yoron.nerdsoverflow.classes.asCircle
import com.yoron.nerdsoverflow.models.UserModel
import kotlinx.android.synthetic.main.user_row_layout.view.*

class UsersSearchAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var users: List<UserModel> = listOf()
    private lateinit var onUserClickListener: OnUserClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row_layout, parent, false)

        return ViewHolder(view,onUserClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUser(users[position])

    }

    override fun getItemCount(): Int {
        return users.size
    }


    fun setUsersList(users: List<UserModel>) {
        this.users = users
        notifyDataSetChanged()
    }


    fun setOnUserClickListener(onUserClickListener: OnUserClickListener) {
        this.onUserClickListener = onUserClickListener
    }


    class ViewHolder(val view: View,  val onUserClickListener: OnUserClickListener) :
        RecyclerView.ViewHolder(view) {



        fun setUser(user: UserModel) {
            view.searchImage.apply {
                asCircle()
                setImageURI(user.image)
            }

            view.searchUsername.text = user.username
            itemView.setOnClickListener {
                onUserClickListener.onUserClicked(user)
            }

        }
    }


}

interface OnUserClickListener {
    fun onUserClicked(user: UserModel)
}