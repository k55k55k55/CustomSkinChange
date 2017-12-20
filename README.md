# 简介
##### CustomSkinChange是一款Android自定义的换肤框架，功能强大，可以配合Android-skin-support一起使用，
##### 它通过反射换肤的skin包（其实就是APK包）获取其中的资源动态的设置需要换肤的app。它配置有点麻烦，但
##### 是功能强大，它不仅可以完成Android-skin-support的功能，比如换控件的背景颜色、透明度、图片，它还可
##### 以完成Android-skin-support不能完成的功能，比如坐标、自定义属性、字体内容、控件的宽高等，它几乎可
##### 以通过获取包括皮肤包在Android项目资源文件下的任何资源进行换肤。 
[Android-skin-support的githup地址](https://github.com/ximsfei/Android-skin-support.git)
####  这款框架还可以：
- 尤其对于某些自定义的控件的自定义属性也可以切换皮肤包中的资源,比如圆的颜色以及圆的半径都都可以根据换肤包切换。
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-d75bf3a2a599347a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 

 - 字体的颜色、字体的内容、字体的坐标都难以通过换肤包切换
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-f87706062a3a57f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  

- 控件的宽高、坐标位置也能根据皮肤包中的资源进行切换
 
![image.png](http://upload-images.jianshu.io/upload_images/9374751-e38c756fd04e8326.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
 ##### 由于它需要通过反射来获取皮肤包中的资源进行动态换肤，所以需要换肤包提供发射的方法，配置也就会比Android-skin-support
 ##### 麻烦，但是它可以完成Android-skin-support不能完成的需求，所以可以用Android-skin-support完成大部分可以完成
 ##### 的换肤需求，然后通过CustomSkinChange去解决那些非常规的需求。
