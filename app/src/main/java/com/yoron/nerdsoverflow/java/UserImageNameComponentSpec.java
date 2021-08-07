/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:30 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:30 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaEdge;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.yoron.nerdsoverflow.R;
import com.yoron.nerdsoverflow.models.UserModel;

@LayoutSpec
class UserImageNameComponentSpec {




    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop UserModel user,
                                    @Prop Timestamp timestamp
                                    ) {

        if (user != null)
        return Row.create(c)
                .child(
                        FrescoImageView.create(c).url(user.getImage())
                                .isCircle(true)
                                .heightDip(30)
                                .widthDip(30)
                                .marginDip(YogaEdge.END, 10)
                                .marginDip(YogaEdge.TOP, 10)
                )
                .child(
                        Column.create(c).child(Text.create(c)
                                .text(user.getUsername())
                                .textColorRes(R.color.darkColor)
                                .alpha(0.7f)
                                .marginDip(YogaEdge.TOP , 10)
                                .textSizeSp(12))
                        .child(
                                TimestampComponent.create(c).heightDip(20).widthPercent(100).timestamp(timestamp)
                                .marginDip(YogaEdge.START , 3)
                                .marginDip(YogaEdge.TOP , 1)

                        ).widthPercent(70)


                )
                .build();


        else
            return Row.create(c).build();
    }
}