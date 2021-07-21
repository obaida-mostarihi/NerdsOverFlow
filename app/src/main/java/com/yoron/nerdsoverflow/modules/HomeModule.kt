/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 2:15 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:15 PM
 *
 */

package com.yoron.nerdsoverflow.modules

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object HomeModule {

    @Named(value = "postsRef")
    @Provides
    fun provideFirestorePostRef(): Query = Firebase.firestore.collection("Posts").limit(12)

    @Named(value = "answersCollection")
    @Provides
    fun provideFirestorePostsCollectionRef(): CollectionReference =
        Firebase.firestore.collection("Posts")
}