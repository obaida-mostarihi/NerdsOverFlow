/*
 *
 * Created by Obaida Al Mostarihi on 7/21/21, 1:06 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/21/21, 1:06 AM
 *
 */

package com.yoron.nerdsoverflow.java.fullPost;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.SolidColor;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.yoron.nerdsoverflow.R;
import com.yoron.nerdsoverflow.java.UserImageNameComponent;
import com.yoron.nerdsoverflow.models.AnswerModel;
import com.yoron.nerdsoverflow.models.UserModel;

@LayoutSpec
class AnswerComponentSpec {



    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop AnswerModel answerModel) {

        Column.Builder builder = Column.create(c);

        builder.child(
                        UserImageNameComponent.create(c)
                                .timestamp(answerModel.getDate())
                                .user(answerModel.getUser() == null ? new UserModel() : answerModel.getUser() )
                                .widthPercent(100)
                );
        builder.child(
                        Text.create(c)
                                .text(answerModel.getAnswer())
                                .marginDip(YogaEdge.TOP, 10)
                                .textColorRes(R.color.darkColor)
                                .textSizeSp(14)
                                .alpha(0.9f)
                                .extraSpacingDip(3)
                );
        if (answerModel.getCode() != null)
        builder.child(
                        Text.create(c)
                                .text("See Code..")
                                .textColorRes(R.color.greenColor)
                                .paddingDip(YogaEdge.VERTICAL , 16)
                                .paddingDip(YogaEdge.HORIZONTAL , 10)
                                .clickable(true)
                                .focusable(true)
                                .backgroundAttr(android.R.attr.selectableItemBackground)
                );
        builder.child(
                        SolidColor.create(c)
                                .colorRes(R.color.darkColor)
                                .widthPercent(100)
                                .heightDip(0.5f)
                                .alpha(0.3f)
                                .marginDip(YogaEdge.TOP , answerModel.getCode() != null ? 0 : 16)
                );
                return builder.build();
    }
}