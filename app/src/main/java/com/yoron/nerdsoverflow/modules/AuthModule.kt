package com.yoron.nerdsoverflow.modules

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {

    @Named(value = "auth")
    @Provides
    fun provideAuth() = Firebase.auth

}