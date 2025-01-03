package com.potent.util;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject MoTKAPP
 * @description: TODO< 动画相关的函数 >
 * @date: 2014/10/21 19:41
 */
public class BaseAnimation {
    public static Animation inFromTopAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,  0.0f, Animation.RELATIVE_TO_SELF,  0.0f,
                Animation.RELATIVE_TO_SELF,  -1.0f, Animation.RELATIVE_TO_SELF,   0.0f
        );
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }
    public static Animation outToBottonAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,  0.0f, Animation.RELATIVE_TO_SELF,  0.0f,
                Animation.RELATIVE_TO_SELF,  0.0f, Animation.RELATIVE_TO_SELF,   -1.0f
        );
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
}
