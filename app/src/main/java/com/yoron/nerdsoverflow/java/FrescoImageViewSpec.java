/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:31 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:31 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.fresco.FrescoImage;
import com.yoron.nerdsoverflow.FrescoFunctionsKt;

@LayoutSpec
class FrescoImageViewSpec {



    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c, @Prop String url , @Prop boolean isCircle
    ) {


        return Column.create(c).child(FrescoImage.create(c)
                .controller(FrescoFunctionsKt.getImageController(url))
                .fadeDuration(500)
                .roundingParams(isCircle ? RoundingParams.asCircle() : null)
                .actualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP))
                .build();
    }
}