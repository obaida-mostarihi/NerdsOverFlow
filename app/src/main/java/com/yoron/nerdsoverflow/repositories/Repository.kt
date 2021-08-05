/*
 *
 * Created by Obaida Al Mostarihi on 8/5/21, 11:08 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/5/21, 11:08 AM
 *
 */

package com.yoron.nerdsoverflow.repositories

import com.google.firebase.firestore.DocumentReference
import com.yoron.nerdsoverflow.models.UserModel
import kotlinx.coroutines.tasks.await

open class Repository {
    suspend fun getUserDetails(userReference: DocumentReference?): UserModel? {

        userReference?.let { ref ->
            return ref.get().await().toObject(UserModel::class.java)
        }
        return UserModel()
    }
}