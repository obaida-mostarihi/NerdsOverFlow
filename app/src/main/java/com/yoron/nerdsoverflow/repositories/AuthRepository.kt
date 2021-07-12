package com.yoron.nerdsoverflow.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.yoron.nerdsoverflow.viewModels.AuthDataOrException
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
    suspend fun loginUserToAnAccount(email: String , password: String): AuthDataOrException{

        val dataOrException =  AuthDataOrException()
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            dataOrException.data = authResult
        }catch (e: FirebaseAuthException){
            dataOrException.e = e
        }

        return dataOrException
    }


}