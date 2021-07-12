package com.yoron.nerdsoverflow.repositories

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Named

class AuthRepository @Inject constructor(
    @Named(value = "auth")
    private val auth: FirebaseAuth
) {


    suspend fun loginUserToAnAccount(){

    }


}