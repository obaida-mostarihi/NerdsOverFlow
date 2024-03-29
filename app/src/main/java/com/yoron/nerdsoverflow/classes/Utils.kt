/*
 *
 * Created by Obaida Al Mostarihi on 7/17/21, 8:00 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/17/21, 8:00 PM
 *
 */

package com.yoron.nerdsoverflow.classes

import android.content.res.Resources
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView


fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)
fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)


private fun roundingParams() = RoundingParams.asCircle()

fun SimpleDraweeView.asCircle(){
    this.hierarchy.roundingParams = roundingParams()
}