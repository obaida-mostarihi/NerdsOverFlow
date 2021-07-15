/*
 *
 * Created by Obaida Al Mostarihi on 7/15/21, 4:21 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/15/21, 4:21 PM
 *
 */

package com.yoron.nerdsoverflow.interfaces

import android.app.Activity
import android.content.Context
import com.yoron.nerdsoverflow.models.HomePostModel

interface HomePostListeners {

    fun onPostClicked(context: Context, post: HomePostModel){}

}