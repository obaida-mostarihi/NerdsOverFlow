/*
 *
 * Created by Obaida Al Mostarihi on 7/20/21, 6:08 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:08 PM
 *
 */

package com.yoron.nerdsoverflow.java.home;

import com.facebook.litho.annotations.Event;
import com.yoron.nerdsoverflow.classes.DataOrException;
import com.yoron.nerdsoverflow.models.HomePostModel;

import java.util.List;

@Event
public class HomePostsEvent {
    public DataOrException<List<HomePostModel>, Exception> posts;
    public boolean isEmpty;
    public boolean isFirstLoad;
}


