/*
 *
 * Created by Obaida Al Mostarihi on 8/2/21, 8:47 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/2/21, 8:47 AM
 *
 */

package com.yoron.nerdsoverflow.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class MainRepository @Inject constructor(
    @Named("usersCollection")
    val usersCollection: CollectionReference
) {
    suspend fun hasDetails(): Boolean {
        Firebase.auth.currentUser?.let { user ->
            val userDetails = usersCollection.document(user.uid).get().await()
            if (!userDetails.exists())
                return false
            if (userDetails.contains("username") && userDetails.contains("selectedLanguages"))
                return true
        }

        return false
    }


    suspend fun setDetails(
        username: String,
        selectedLanguages: ArrayList<ProgrammingLanguageModel>
    ): DataOrException<Boolean, Exception> {
        val dataOrException = DataOrException<Boolean, Exception>()
        Firebase.auth.currentUser?.let { user ->
            val details = hashMapOf(
                "username" to username.lowercase().trim(),
                "selectedLanguages" to selectedLanguages
            )

            if(username.contains(" ")){
                dataOrException.data = false
                dataOrException.e = Exception("Spaces are not allowed in usernames.")
                return dataOrException
            }

            if (selectedLanguages.isEmpty()){
                dataOrException.data = false
                dataOrException.e = Exception("Please select one or more programming languages.")
                return dataOrException
            }


            if (
                usersCollection.whereEqualTo("username", username.lowercase()).get()
                    .await().isEmpty && username.lowercase().length > 4 && username.isNotBlank()
            ) {
                try {
                    usersCollection.document(user.uid).set(details).await()
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

        }

        return dataOrException
    }


}