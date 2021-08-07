/*
 *
 * Created by Obaida Al Mostarihi on 8/2/21, 8:47 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/2/21, 8:47 AM
 *
 */

package com.yoron.nerdsoverflow.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import com.yoron.nerdsoverflow.models.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class MainRepository @Inject constructor(
    @Named("usersCollection")
    val usersCollection: CollectionReference
) {
    fun hasDetails(uid: String, dataOrException: (DataOrException<UserModel, Exception>) -> Unit) {
        val dataOrExceptionVar = DataOrException<UserModel, Exception>()
        usersCollection.document(uid).addSnapshotListener { value, error ->
            if (value == null)
                return@addSnapshotListener

            if (!value.exists()) {
                dataOrExceptionVar.e = Exception("Not Found.")
                dataOrException(dataOrExceptionVar)
            }

            if (value.contains("username") && value.contains("selectedLanguages")) {
                dataOrExceptionVar.data =
                    value.toObject(UserModel::class.java)?.copy(uid = value.id)
                dataOrException(dataOrExceptionVar)
            } else {
                dataOrExceptionVar.e = Exception("Not Found.")
                dataOrException(dataOrExceptionVar)
            }
        }


    }


    suspend fun setDetails(
        username: String? = null,
        selectedLanguages: ArrayList<ProgrammingLanguageModel>
    ): DataOrException<Boolean, Exception> {
        val dataOrException = DataOrException<Boolean, Exception>()
        Firebase.auth.currentUser?.let { user ->

            val details = if (username != null) {
                hashMapOf(
                    "username" to username.lowercase().trim(),
                    "selectedLanguages" to selectedLanguages
                )
            } else {
                hashMapOf(
                    "selectedLanguages" to selectedLanguages
                )
            }

            if (username != null)
                if (username.contains(" ")) {
                    dataOrException.data = false
                    dataOrException.e = Exception("Spaces are not allowed in usernames.")
                    return dataOrException
                }

            if (selectedLanguages.isEmpty()) {
                dataOrException.data = false
                dataOrException.e = Exception("Please select one or more programming languages.")
                return dataOrException
            }

            if (username != null) {
                if (
                    usersCollection.whereEqualTo("username", username.lowercase()).get()
                        .await().isEmpty && username.lowercase().length > 4 && username.isNotBlank()
                ) {
                    try {
                        usersCollection.document(user.uid).set(details, SetOptions.merge()).await()
                        dataOrException.data = true
                    } catch (e: Exception) {
                        dataOrException.e = e
                        dataOrException.data = false

                    }
                } else {
                    if (username.length > 4)
                        dataOrException.e = Exception("This username is used by another user.")
                    else
                        dataOrException.e = Exception("Username must be 5 characters or more.")

                    dataOrException.data = false
                }

            }else{
                try {
                    usersCollection.document(user.uid).set(details, SetOptions.merge()).await()
                    dataOrException.data = true
                } catch (e: Exception) {
                    dataOrException.e = e
                    dataOrException.data = false

                }
            }
        }

        return dataOrException
    }


    suspend fun setUserImage(uri: Uri) {
        Firebase.auth.currentUser?.let { user ->

            val uploadResult = Firebase.storage.reference.child("UsersImages").putFile(uri).await()

            val imageUrl = uploadResult.storage.downloadUrl.await().toString()

            usersCollection.document(user.uid)
                .set(hashMapOf("image" to imageUrl), SetOptions.merge()).await()
        }
    }

}