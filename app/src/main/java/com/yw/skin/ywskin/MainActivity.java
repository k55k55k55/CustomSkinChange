package com.yw.skin.ywskin;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yw.skin.ywskin.utils.CustomViewSkinUtils;
import com.yw.skin.ywskin.utils.SpUtils;
import com.yw.skin.ywskin.wiget.CustomCircleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.yw.skin.ywskin.R.id.skin_all_rl;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.skin_reset_tv)
    TextView mSkin_reset_tv;
    @ViewInject(R.id.skin_red_tv)
    TextView mSkin_red_tv;
    @ViewInject(R.id.skin_color_tv)
    TextView mSkin_color_tv;
    @ViewInject(R.id.skin_click_ll)
    LinearLayout mSkin_click_ll;
    @ViewInject(R.id.skin_tv)
    TextView mSkin_tv;
    @ViewInject(R.id.skin_cricle)
    CustomCircleView mSkin_cricle;
    @ViewInject(R.id.skin_content_ll)
    RelativeLayout mSkin_content_ll;
    @ViewInject(skin_all_rl)
    RelativeLayout mSkin_all_rl;

    private Class mSkinClzz;
    private Resources mSkinResources;
    private String mSkinTextViewName;
    private String mSkinTextViewNameCp;
    private float mSkinTextViewMargin;
    private float mSkinTextViewMarginCp;
    private int mSkinTextViewColor;
    private int mSkinTextViewColorCp;
    private Drawable mSkinRlbg;
    private Drawable mSkinRlbgCp;
    private Drawable mSkinAllRlbg;
    private Drawable mSkinAllRlbgCp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        x.view().inject( this );
        init();
    }

    private void init() {
        mSkinTextViewName = getResources().getString( R.string.skin_color_name );
        mSkinTextViewNameCp = mSkinTextViewName;
        mSkinTextViewMargin =   getResources().getDimension( R.dimen.text_view_margin_left );
        mSkinTextViewMarginCp = mSkinTextViewMargin;
        mSkinTextViewColor = getResources().getColor( R.color.reset_blist1_color );
        mSkinTextViewColorCp = mSkinTextViewColor;
        mSkinRlbg = getResources().getDrawable( R.drawable.layer_rl_bg );
        mSkinRlbgCp = mSkinRlbg;
        mSkinAllRlbg = getResources().getDrawable( R.drawable.layer_all_rl_bg );
        mSkinAllRlbgCp = mSkinAllRlbg;
    }

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

    //保存主题名称
    private void saveZhutiName(String highZhuTiName) {
        int len = highZhuTiName.contains( "." ) == true ? highZhuTiName.indexOf( "." ) : highZhuTiName.length();
        SpUtils.getInstance( this ).saveSkinName( highZhuTiName.substring( 0, len ) );
    }

    //设置换肤资源
    public void setSkinResource() {
        mSkinClzz = CustomViewSkinUtils.customViewChangeSkin( this );
        mSkinResources = CustomViewSkinUtils.addOtherResourcesToMain( this );
    }

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
}
