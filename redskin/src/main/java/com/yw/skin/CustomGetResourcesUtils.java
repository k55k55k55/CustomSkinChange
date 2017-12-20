package com.yw.skin;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * @author yw
 * @time 2017/9/13  14:57
 * @desc ${TODD}
 */

public class CustomGetResourcesUtils {
    //CustomCircleView中的圆的颜色
    public static int Circle_solid_color(Resources resources) {
        return resources.getColor( R.color.reset_blist1_color );
    }
    //CustomCircleView中的圆的大小
    public static float Circle_radius(Resources resources) {
        return resources.getInteger( R.integer.skin_tv_circle_radues );
    }
    //矩形框的drawable
    public static Drawable skin_bg_drawable(Resources resources) {
        return resources.getDrawable( R.drawable.layer_rl_bg );
    }
 /*   //最外层的背景图片
    public static Drawable skin_bg_mipmap(Resources resources) {
        return resources.getDrawable( R.mipmap.rl_noad_blist21_nor_ft );
    }*/
    //获取文字的颜色
    public static int skin_text_color(Resources resources) {
        return resources.getColor( R.color.reset_blist1_color );
    }
    //获取文字的位置
    public static float skin_text_magin(Resources resources) {
        return   resources.getDimension( R.dimen.text_margin );
    }

    //获取文字
    public static String skin_text_name(Resources resources) {
        return resources.getString( R.string.skin_color_name );
    }


}
