/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 2:16 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:16 PM
 *
 */

package com.yoron.nerdsoverflow.repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.models.UserModel
import com.yoron.nerdsoverflow.view_models.HomePostsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class HomePostsRepository @Inject constructor(
    @Named(value = "postsRef")
    val homePostsQuery: Query
): Repository() {

    suspend fun getPostsData(
        documentSnapshot: DocumentSnapshot? = null,
        dataOrException: (DataOrException<HomePostsList, Exception>) -> Unit?
    ) {
        val dataOrExceptionVar = DataOrException<HomePostsList, Exception>()
        val query = if (documentSnapshot == null) {
            homePostsQuery
        } else {
            homePostsQuery.startAfter(documentSnapshot)
        }

        withContext(Dispatchers.Main){
            async {
                try {
                    dataOrExceptionVar.data = query.get().await().mapNotNull { document ->
                        val model = document.toObject(HomePostModel::class.java)
                        model.copy(documentSnapshot = document,user = getUserDetails(model.userReference))
                    }
                }catch (e: Exception){
                    dataOrExceptionVar.e = e
                    Log.v("loool" , e.localizedMessage)
                }
            }.invokeOnCompletion {
                if (it == null)
                    dataOrException(dataOrExceptionVar)
                else
                    Log.v("loool", it.localizedMessage)

            }
        }




    }

    suspend fun getSpecificUserPosts(
        documentSnapshot: DocumentSnapshot? = null,
        userRef: DocumentReference,
        dataOrException: (DataOrException<HomePostsList, Exception>) -> Unit?
    ) {
        val dataOrExceptionVar = DataOrException<HomePostsList, Exception>()
        val query = if (documentSnapshot == null) {
            homePostsQuery.whereEqualTo("userReference" , userRef)
        } else {
            homePostsQuery.whereEqualTo("userReference" , userRef).startAfter(documentSnapshot)
        }

        withContext(Dispatchers.Main){
            async {
                try {
                    dataOrExceptionVar.data = query.get().await().mapNotNull { document ->
                        val model = document.toObject(HomePostModel::class.java)
                        model.copy(documentSnapshot = document,user = getUserDetails(model.userReference))
                    }
                }catch (e: Exception){
                    dataOrExceptionVar.e = e
                    Log.v("loool" , e.localizedMessage)
                }
            }.invokeOnCompletion {
                if (it == null)
                    dataOrException(dataOrExceptionVar)
                else
                    Log.v("loool", it.localizedMessage)

            }
        }




    }

}