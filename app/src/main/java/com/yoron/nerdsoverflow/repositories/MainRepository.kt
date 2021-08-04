/*
 *
 * Created by Obaida Al Mostarihi on 8/2/21, 8:47 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/2/21, 8:47 AM
 *
 */

package com.yoron.nerdsoverflow.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
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
    suspend fun hasDetails(): DataOrException<UserModel , Exception> {
        var dataOrException = DataOrException<UserModel , Exception>()
        Firebase.auth.currentUser?.let { user ->
            val userDetails = usersCollection.document(user.uid).get().await()
            if (!userDetails.exists()){
                dataOrException.e = Exception("Not Found.")
                return dataOrException
            }

            return if (userDetails.contains("username") && userDetails.contains("selectedLanguages")){
                dataOrException.data = userDetails.toObject(UserModel::class.java)
                dataOrException
            }else{
                dataOrException.e = Exception("Not Found.")
                dataOrException
            }
        }

        return dataOrException
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