package com.yw.skin.ywskin.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 专为自定义换肤的方案工具类
 *
 * @author yw
 * @time 2017/9/13  14:14
 * @desc ${TODD}
 */

public class CustomViewSkinUtils {
    public static final String DEFAULT_SKIN = "default";
    public static final String RESET_SKIN = DEFAULT_SKIN;
    public static final String RED_SKIN = "redskin";
    public static final String COLOR_SKIN = "colorskin";

    /**
     * 从sdk中获取皮肤包，然后通过DexClassLoader获取皮肤包中的设置色值、尺寸的类的Class
     * @param context
     * @return
     */
    public static Class customViewChangeSkin(Context context) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        Log.i("skin_reset", "###"+skinName);
        if (!DEFAULT_SKIN.equals( skinName )) {
            try {
                File dex_in = new File( Environment.getExternalStorageDirectory().toString() + File.separator + skinName + ".apk" );
                String optimizedDirectory = context.getCacheDir() + File.separator;
                Log.i("skin_reset", dex_in.getAbsolutePath()+"###"+optimizedDirectory+"  "+dex_in.getName());
                ClassLoader classLoader = new DexClassLoader( dex_in.getAbsolutePath(), optimizedDirectory, null, context.getClassLoader() );
                Class clazz = classLoader.loadClass( "com.yw.skin.CustomGetResourcesUtils" );
                return clazz;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 因为Android-skin-support的皮肤包是放在assets中的，所以我们这里也从assets中获取皮肤包
     * 并保存在sdk中（这里只是为了兼容Android-skin-support ，其实可以直接把皮肤包放在sdk中的）
     * @param context
     * @param skinName 皮肤包的名称（不包括后缀名）
     */
    public static void copySkinApk(Context context, String skinName) {
        try {
            File dex = new File( Environment.getExternalStorageDirectory().toString() + File.separator + skinName + ".apk" );
            InputStream input = context.getAssets().open( "skins/" + skinName + ".skin" );
            dex.createNewFile();
            FileOutputStream fo = new FileOutputStream( dex );
            int a = 0;
            byte bf[] = new byte[1024];
            while ((a = input.read( bf )) != -1) {
                fo.write( bf, 0, a );
                fo.flush();
            }
            fo.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void copyNetSkinApk(Context context, String skinName,String path) {
        try {
            File dex = new File( Environment.getExternalStorageDirectory().toString() + File.separator + skinName + ".apk" );
//            InputStream input = context.getAssets().open( "skins/" + skinName + ".skin" );
            dex.createNewFile();
            FileInputStream inputStream = new FileInputStream(path);//读入原文件
            FileOutputStream fo = new FileOutputStream( dex );
            int a = 0;
            byte bf[] = new byte[1024];
            while ((a = inputStream.read( bf )) != -1) {
                fo.write( bf, 0, a );
                fo.flush();
            }
            fo.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getCustomViewSkin(Context context, String name, Class clazz, Resources resources, T normalParam) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        if ((clazz != null && resources != null) && !DEFAULT_SKIN.equals( skinName )) {
            try {
                Method method = clazz.getMethod( name, Resources.class );
                T newSkinName = (T) method.invoke( null, resources );
                return newSkinName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return normalParam;
    }

    public static String getCustomViewSkinString(Context context, String name, Class clazz, Resources resources, String normalString) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        if ((clazz != null && resources != null) && !DEFAULT_SKIN.equals( skinName )) {
            try {
                Method method = clazz.getMethod( name, Resources.class );
                String newSkinName = (String) method.invoke( null, resources );
                return newSkinName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return normalString;
    }

    public static int getCustomViewSkinColor(Context context, String name, Class clazz, Resources resources, int normalDayColor) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        if ((clazz != null && resources != null) && !DEFAULT_SKIN.equals( skinName )) {
            try {
                Method method = clazz.getMethod( name, Resources.class );
                int SkinColor = (int) method.invoke( null, resources );
                return SkinColor;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return normalDayColor;
    }

    public static float getCustomViewSkinSize(Context context, String name, Class clazz, Resources resources, float normalSize) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        if ((clazz != null && resources != null) && !"default".equals( skinName )) {
            try {
                Method method = clazz.getMethod( name, Resources.class );
                float SkinSize = (float) method.invoke( null, resources );
                return SkinSize;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return normalSize;
    }

    /**
     * 通过addAssetPath将皮肤包中的资源增加到本apk自己的path里面以后 就可以重新构建出resource对象了
     * 利用这个Resource就可以通过反射获取皮肤包中的资源了
     * @param context
     * @return
     */
    public static Resources addOtherResourcesToMain(Context context) {
        String skinName = SpUtils.getInstance( context ).getSkinName();
        if (!DEFAULT_SKIN.equals( skinName )) {
            File dex_in = new File( Environment.getExternalStorageDirectory().toString() + File.separator + skinName + ".apk" );
            AssetManager assetManager = null;
            try {
                assetManager = AssetManager.class.newInstance();
                //反射调用addAssetPath这个方法 就可以
                Method addAssetPath = assetManager.getClass().getMethod( "addAssetPath", String.class );
                addAssetPath.invoke( assetManager, dex_in.getAbsolutePath() );
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //把themeapk里的资源 通过addAssetPath 这个方法增加到本apk自己的path里面以后 就可以重新构建出resource对象了
            return new Resources( assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration() );
        }
        return null;
    }

}
