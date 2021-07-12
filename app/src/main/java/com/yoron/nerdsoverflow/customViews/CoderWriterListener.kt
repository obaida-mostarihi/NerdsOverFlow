/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.customViews

interface CoderWriterListener {
    fun onTypingStart(text: String?)

    fun onTypingEnd(text: String?)

    fun onCharacterTyped(text: String?, position: Int)

    fun onTypingRemoved(text: String?)
}