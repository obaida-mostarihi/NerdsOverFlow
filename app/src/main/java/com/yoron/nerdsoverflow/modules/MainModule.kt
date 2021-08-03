/*
 *
 * Created by Obaida Al Mostarihi on 8/2/21, 8:41 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/2/21, 8:41 AM
 *
 */

package com.yoron.nerdsoverflow.modules

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object MainModule {


    @Named(value = "usersCollection")
    @Provides
    fun provideUsersCollection(): CollectionReference = Firebase.firestore.collection("Users")
}