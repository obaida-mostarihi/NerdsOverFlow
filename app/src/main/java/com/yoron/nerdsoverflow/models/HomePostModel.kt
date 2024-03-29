/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 2:01 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:01 PM
 *
 */

package com.yoron.nerdsoverflow.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp

data class HomePostModel(
    @DocumentId
    val postId: String? = "",
    val userReference: DocumentReference? = null,
    val title: String? = "",
    val question:String? = "",
    val code: String? = null,
    val language:String? = "Java",
    val answered: Boolean? = false,
    @ServerTimestamp
    val timestamp: Timestamp? = null,
    val user: UserModel? = UserModel(),
    val documentSnapshot: DocumentSnapshot? = null
    ){
}
