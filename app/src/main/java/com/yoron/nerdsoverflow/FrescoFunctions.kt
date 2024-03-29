/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:32 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:32 PM
 *
 */

package com.yoron.nerdsoverflow

import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.generic.RoundingParams


fun getCircleRoundParams(): RoundingParams = RoundingParams.fromCornersRadius(5f).setRoundAsCircle(true)


fun getImageController(imageUrl : String): AbstractDraweeController<Any, Any> = Fresco.newDraweeControllerBuilder()
    .setUri(imageUrl)
    .build()