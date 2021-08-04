/*
 *
 * Created by Obaida Al Mostarihi on 8/3/21, 1:17 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/3/21, 1:17 PM
 *
 */

package com.yoron.nerdsoverflow.repositories

import com.google.firebase.firestore.CollectionReference
import com.yoron.nerdsoverflow.models.HomePostModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class PostingRepository @Inject constructor(
    @Named("posts")
    private val postsCollection: CollectionReference
) {

    suspend fun publishPost(post: HomePostModel){

        postsCollection.document().set(post).await()

    }

}