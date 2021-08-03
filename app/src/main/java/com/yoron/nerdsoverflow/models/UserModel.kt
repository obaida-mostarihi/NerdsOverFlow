/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:37 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:37 PM
 *
 */

package com.yoron.nerdsoverflow.models

data class UserModel(
    val uid: String? = "",
    val image: String? = "https://kstu.edu.tr/kstu-file/uploads/default-user-image.png",
    val username: String? = "Nerd",
    val selectedLanguages: ArrayList<ProgrammingLanguageModel>? = null
)
