/*
 *
 * Created by Obaida Al Mostarihi on 7/18/21, 9:57 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/18/21, 9:57 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import com.facebook.litho.annotations.Event;
import com.facebook.litho.sections.SectionContext;
import com.yoron.nerdsoverflow.models.HomePostModel;

@Event
public class OnPostClickedEvent {
    public HomePostModel post;
    public SectionContext context;
}