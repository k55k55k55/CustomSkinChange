# 前言
##### 上篇文章介绍了Android-skin-support的缺陷([Android-skin-support的缺陷](http://www.jianshu.com/p/cfb7fef0ff46))，那么这篇文章就来介绍一下如何解决这个缺陷。我们这里就通过一个自定义的换肤框架和Android-skin-support一起配合使用就可以满足各种常规和非常规的换肤需求。CustomSkinChange是一款Android自定义的换肤框架，功能强大，可以配合Android-skin-support一起使用，它通过反射换肤的skin包（其实就是APK包）获取其中的资源动态的设置需要换肤的app。它配置有点麻烦，但是功能强大，它不仅可以完成Android-skin-support的功能，比如换控件的背景颜色、透明度、图片，它还可以完成Android-skin-support不能完成的功能，比如坐标、自定义属性、字体内容、控件的宽高等，它几乎可以通过获取包括皮肤包在Android项目资源文件下的任何资源进行换肤。 
[Android-skin-support的github地址](https://github.com/ximsfei/Android-skin-support.git)
####  这款框架还可以：
- 尤其对于某些自定义的控件的自定义属性也可以切换皮肤包中的资源,比如圆的颜色以及圆的半径都都可以根据换肤包切换。
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-d75bf3a2a599347a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 

 - 字体的颜色、字体的内容、字体的坐标都难以通过换肤包切换
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-f87706062a3a57f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  

- 控件的宽高、坐标位置也能根据皮肤包中的资源进行切换
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-e38c756fd04e8326.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ##### 由于它需要通过反射来获取皮肤包中的资源进行动态换肤，所以需要换肤包提供反射的方法，配置也就会比Android-skin-support
 ##### 麻烦，但是它可以完成Android-skin-support不能完成的需求，所以可以用Android-skin-support完成大部分可以完成
 ##### 的换肤需求，然后通过CustomSkinChange去解决那些非常规的需求。
# 正文
#### 先看看换肤的动画效果
![2017-12-20_20_30_05.gif](http://upload-images.jianshu.io/upload_images/9374751-867d686b493d5286.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
#### 1.点击按钮切换皮肤包，并对换肤名称进行持久化
```
   @Event(value = {R.id.skin_reset_tv, R.id.skin_red_tv, R.id.skin_color_tv})
    private void onClick(View view) {

        switch (view.getId()) {
            case R.id.skin_reset_tv://默认的蓝色皮肤
                Log.i( "skin_reset", "###重置" );
                saveZhutiName( CustomViewSkinUtils.RESET_SKIN );
                setSkinResource();
                skinReFresh();
                break;
            case R.id.skin_red_tv://红色皮肤
                Log.i( "skin_reset", "###红色" );
                saveZhutiName( CustomViewSkinUtils.RED_SKIN );
                CustomViewSkinUtils.copySkinApk( this, CustomViewSkinUtils.RED_SKIN );
                setSkinResource();
                skinReFresh();
                break;
            case R.id.skin_color_tv://多彩的图片皮肤
                saveZhutiName( CustomViewSkinUtils.COLOR_SKIN );
                CustomViewSkinUtils.copySkinApk( this, CustomViewSkinUtils.COLOR_SKIN );
                setSkinResource();
                skinReFresh();
                break;
        }
    }
```
利用sp对换肤的名称进行持久化
```
SpUtils.getInstance( this ).saveSkinName( highZhuTiName.substring( 0, len ) );
```
#### 2.因为Android-skin-support的换肤包都是放在assets/skins的文件夹下面的，CustomSkinChange是配合Android-skin-support使用，那么它的换肤包也是从assets/skins文件夹下面取，取同样的换肤包存入sd卡中（如果只想利用CustomSkinChange换肤，不用Android-skin-support框架，那么就可以把换肤包放在sd卡中）。
```
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
```
#### 3.在调用setSkinResource()获取换肤的资源
```
    //设置换肤资源
    public void setSkinResource() {
        mSkinClzz = CustomViewSkinUtils.customViewChangeSkin( this );
        mSkinResources = CustomViewSkinUtils.addOtherResourcesToMain( this );
    }
```
```
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
```
```
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
```
#### 4.调用skinReFresh()进行换肤
```
 //开始换肤
    public void skinReFresh() {
        mSkinTextViewName = CustomViewSkinUtils.getCustomViewSkin( this, "skin_text_name", mSkinClzz, mSkinResources, mSkinTextViewNameCp );
        mSkin_tv.setText( mSkinTextViewName );

        mSkinTextViewMargin = CustomViewSkinUtils.getCustomViewSkin( this, "skin_text_magin", mSkinClzz, mSkinResources, mSkinTextViewMarginCp );
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSkin_tv.getLayoutParams();
        layoutParams.leftMargin = (int) mSkinTextViewMargin;
        mSkin_tv.setLayoutParams( layoutParams );

        mSkinTextViewColor = CustomViewSkinUtils.getCustomViewSkin( this, "skin_text_color", mSkinClzz, mSkinResources, mSkinTextViewColorCp );
        mSkin_tv.setTextColor( mSkinTextViewColor );

        mSkinRlbg = CustomViewSkinUtils.getCustomViewSkin( this, "skin_bg_drawable", mSkinClzz, mSkinResources, mSkinRlbgCp );
        mSkin_content_ll.setBackground( mSkinRlbg );

        mSkinAllRlbg = CustomViewSkinUtils.getCustomViewSkin( this, "skin_bg_mipmap", mSkinClzz, mSkinResources, mSkinAllRlbgCp );
        mSkin_all_rl.setBackground( mSkinAllRlbg );

        mSkin_cricle.setSkinRefresh( mSkinClzz, mSkinResources );
    }
```
#### 5.调用CustomViewSkinUtils.getCustomViewSkin()获取换肤包总的资源
如果换肤包不存在或者换肤包的内容没有此资源的反射方法，那么就返回默认皮肤的资源。
```
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
```
#### 6.对于自定义控件的自定义属性的处理
radius和solid_color是自定义圆的半径和颜色
```
            <com.yw.skin.ywskin.wiget.CustomCircleView
                android:id="@+id/skin_cricle"
                android:layout_width="@dimen/skin_tv_circle_with"
                android:layout_height="@dimen/skin_tv_circle_height"
                android:layout_centerInParent="true"
                app:radius="@integer/skin_tv_circle_radues"
                app:solid_color="@color/reset_blist1_color"

            />
```
获取自定义属性，并且再用一个变量存储这个默认皮肤（本app）的资源
```
private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray array = getContext().obtainStyledAttributes( attrs, R.styleable.Circle );
        Circle_solid_color = array.getColor( R.styleable.Circle_solid_color, Color.BLUE );
        Circle_radius = array.getInteger( R.styleable.Circle_radius, 50 ); //半径
        Circle_radiusCp = Circle_radius;//默认皮肤的资源
        Circle_solid_colorCp = Circle_solid_color;//默认皮肤的资源
        array.recycle();
        mPaint.setDither( true ); //防抖
        mPaint.setAntiAlias( true ); //抗锯齿
        mPaint.setStrokeWidth( 50 ); //设置画笔的线宽
        mPaint.setColor( Circle_solid_color );

    }
```
开始换肤,如果相关的皮肤包不存在或者资源的反射方法也不存在，那么就返回默认（本app）的资源
```
   public void setSkinRefresh(Class clz, Resources res) {
        Circle_solid_color = CustomViewSkinUtils.getCustomViewSkinColor( mContext, "Circle_solid_color", clz, res, Circle_solid_colorCp );
        Circle_radius = CustomViewSkinUtils.getCustomViewSkinSize( mContext, "Circle_radius", clz, res, Circle_radiusCp );
        mPaint.setColor( Circle_solid_color );
        this.invalidate();//调用此方法进行刷新布局
    }
```
调用invalidate()就会重新执行一次ondraw()方法，利用获取的新资源重新画圆
```
 @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        int sizeX = getWidth() / 2;
        int sizeY = getHeight() / 2;
        canvas.drawCircle( sizeX, sizeY, Circle_radius, mPaint );
        canvas.save();
    }
```
##### 这是github的源码[CustomSkinChange的demon](https://github.com/k55k55k55/CustomSkinChange)

