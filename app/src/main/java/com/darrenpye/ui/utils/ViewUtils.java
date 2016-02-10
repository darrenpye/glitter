package com.darrenpye.ui.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;

/**
 * Created by darrenpye on 16-02-06.
 */
public class ViewUtils {

    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public static void setScale(View view, float scale) {
        view.setScaleX(scale);
        view.setScaleY(scale);
    }


    /** An animation for button presses
     *
     * @param buttonView The button to be animated
     */
    public static void animateButtonPress(View buttonView) {
        ViewUtils.setScale(buttonView, 1);
        Animation scale_in =  new ScaleAnimation(.5f, 1f, .5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale_in.setDuration(500);
        scale_in.setFillAfter(true);
        scale_in.setInterpolator(new BounceInterpolator());
        buttonView.startAnimation(scale_in);
    }

    public static void animateButtonShow(View buttonView, long duration) {
        ViewUtils.setScale(buttonView, 1);
        Animation scale_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale_in.setDuration(duration);
        scale_in.setFillAfter(true);
        scale_in.setInterpolator(new BounceInterpolator());
        buttonView.startAnimation(scale_in);
    }
}
