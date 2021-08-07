/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.yoron.nerdsoverflow.view_models.AuthDataOrException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepository @Inject constructor(
    @Named(value = "auth")
    private val auth: FirebaseAuth
) {


    /**
     * @return Data of type AuthResult or Exception
     */
    suspend fun loginUserToAnAccount(email: String, password: String): AuthDataOrException {

        val dataOrException = AuthDataOrException()
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            dataOrException.data = authResult
        } catch (e: FirebaseAuthException) {
            dataOrException.e = e
        } catch (e: Exception) {
            dataOrException.e = e
        }

        return dataOrException
    }


    suspend fun registerANewAccount(email: String, password: String): AuthDataOrException {
        val dataOrException = AuthDataOrException()

        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            dataOrException.data = authResult
        } catch (e: FirebaseAuthException) {
            dataOrException.e = e
        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }


}