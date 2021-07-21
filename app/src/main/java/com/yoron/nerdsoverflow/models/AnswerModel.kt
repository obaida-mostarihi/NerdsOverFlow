/*
 *
 * Created by Obaida Al Mostarihi on 7/18/21, 5:09 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/18/21, 5:09 AM
 *
 */

package com.yoron.nerdsoverflow.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp

data class AnswerModel(
    val user: UserModel? = null,
    val answer: String? = "",
    val code: String? = "",
    @ServerTimestamp
    val date: Timestamp? = null,
    val documentSnapshot: DocumentSnapshot? = null

)
