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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp

data class HomePostModel(
    @DocumentId
    val postId: String? = "",
    val uid: String? = "",
    val question: String? = "",
    @ServerTimestamp
    val timestamp: Timestamp? = null,
    val user: UserModel? = UserModel(),
    val documentSnapshot: DocumentSnapshot? = null,
    val code: String? = null
    ){
}
