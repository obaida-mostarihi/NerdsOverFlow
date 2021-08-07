/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 7:46 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 7:46 PM
 *
 */

package com.yoron.nerdsoverflow.repositories

import com.google.firebase.firestore.CollectionReference
import com.yoron.nerdsoverflow.models.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class SearchUsersRepository @Inject constructor(
    @Named("usersCollection")
    private val usersCollection: CollectionReference
) {


    suspend fun getUsersList(username: String): List<UserModel> {
        return usersCollection.whereGreaterThanOrEqualTo("username", username)
            .whereLessThanOrEqualTo("username", "$username\uF7FF")
            .limit(12)
            .get().await().mapNotNull { queryDocumentSnapshot ->
                queryDocumentSnapshot.toObject(UserModel::class.java).copy(uid = queryDocumentSnapshot.id)
            }
    }
}