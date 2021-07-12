/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.yoron.nerdsoverflow.R

class LoadingDialog (context: Context, cancelable: Boolean = true): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_loading)
    }
    init {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }
}