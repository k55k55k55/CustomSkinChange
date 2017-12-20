package com.yw.skin.ywskin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP保存数据
 */

public class SpUtils {

    private static final String SETTING = "setting";

    private static final String SKIN_NAME = "skin_name";

    private static SharedPreferences mSp;
    private static SpUtils spUtils;
    private static SharedPreferences.Editor mEdit;


    private SpUtils(Context context) {
        mSp = context.getSharedPreferences( SETTING, Context.MODE_PRIVATE );
    }

    public static SpUtils getInstance(Context context) {
        if (spUtils == null) {
            spUtils = new SpUtils( context.getApplicationContext() );
        }
        mEdit = mSp.edit();
        return spUtils;
    }


    /**
     * 保存皮肤名称
     */
    public void saveSkinName(String skinName) {
        mEdit.putString( SKIN_NAME, skinName );
        mEdit.commit();
    }

    /**
     * 获取皮肤名称
     */
    public String getSkinName() {
        return mSp.getString( SKIN_NAME, "default" );
    }


}
