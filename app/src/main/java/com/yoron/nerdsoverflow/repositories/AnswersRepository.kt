/*
 *
 * Created by Obaida Al Mostarihi on 7/21/21, 4:43 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/21/21, 4:43 AM
 *
 */

package com.yoron.nerdsoverflow.repositories

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.view_models.answersDataOrException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class AnswersRepository @Inject constructor(
    @Named(value = "answersCollection")
    private val postsCollection: CollectionReference
): Repository(){

    suspend fun getAnswers(
        documentSnapshot: DocumentSnapshot? = null,
        postId: String,
        dataOrException: (answersDataOrException) -> (Unit)
    ) {

        val dataOrExceptionVar = answersDataOrException()
        val query = if (documentSnapshot == null) {
            postsCollection.document(postId).collection("Answers").orderBy("date" , Query.Direction.DESCENDING).limit(10)
        } else {
            postsCollection.document(postId).collection("Answers").orderBy("date" , Query.Direction.DESCENDING)
                .startAfter(documentSnapshot).limit(10)
        }


        withContext(Dispatchers.Main) {
            async {
                try {
                    dataOrExceptionVar.data = query.get().await().mapNotNull { document ->
                        val model = document.toObject(AnswerModel::class.java)
                        model.copy(documentSnapshot = document , user = getUserDetails(model.userReference))
                    }
                } catch (e: Exception) {
                    dataOrExceptionVar.e = e
                }
            }.invokeOnCompletion {
                if (it == null)
                    dataOrException(dataOrExceptionVar)
                else
                    it.localizedMessage?.let { msg ->
                        Log.v("loool", msg)

                    }

            }
        }

    }


    suspend fun postAnswer(postId: String, answerModel: AnswerModel) {
        postsCollection.document(postId).collection("Answers").document().set(answerModel).await()
    }
}