/*
 *
 * Created by Obaida Al Mostarihi on 7/21/21, 5:13 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/21/21, 5:13 AM
 *
 */

package com.yoron.nerdsoverflow.java.fullPost;

import com.facebook.litho.annotations.Event;
import com.yoron.nerdsoverflow.classes.DataOrException;
import com.yoron.nerdsoverflow.models.AnswerModel;

import java.util.List;

@Event
public class AnswersEvent {
    public DataOrException<List<AnswerModel>, Exception> answers;
    public boolean isEmpty;
    public boolean isFirstLoad;
}