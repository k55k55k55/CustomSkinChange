package com.yw.skin.ywskin.wiget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yw.skin.ywskin.R;
import com.yw.skin.ywskin.utils.CustomViewSkinUtils;

/**
 * @author yw
 * @time 2017/12/13  11:58
 * @desc ${TODD}
 */

public class CustomCircleView extends android.support.v7.widget.AppCompatTextView {
    private Paint mPaint = new Paint();
    private float Circle_radius;
    private float Circle_radiusCp;
    private Context mContext;
    private int Circle_solid_color;
    private int Circle_solid_colorCp;

    public CustomCircleView(Context context) {
        super( context );

    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        init( context, attrs );
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init( context, attrs );
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray array = getContext().obtainStyledAttributes( attrs, R.styleable.Circle );
        Circle_solid_color = array.getColor( R.styleable.Circle_solid_color, Color.BLUE );
        Circle_radius = array.getInteger( R.styleable.Circle_radius, 50 ); //半径
        Circle_radiusCp = Circle_radius;
        Circle_solid_colorCp = Circle_solid_color;
        array.recycle();
        mPaint.setDither( true ); //防抖
        mPaint.setAntiAlias( true ); //抗锯齿
        mPaint.setStrokeWidth( 50 ); //设置画笔的线宽
        mPaint.setColor( Circle_solid_color );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        int sizeX = getWidth() / 2;
        int sizeY = getHeight() / 2;
        canvas.drawCircle( sizeX, sizeY, Circle_radius, mPaint );
        canvas.save();
    }

    public void setSkinRefresh(Class clz, Resources res) {
        Circle_solid_color = CustomViewSkinUtils.getCustomViewSkinColor( mContext, "Circle_solid_color", clz, res, Circle_solid_colorCp );
        Circle_radius = CustomViewSkinUtils.getCustomViewSkinSize( mContext, "Circle_radius", clz, res, Circle_radiusCp );
        mPaint.setColor( Circle_solid_color );
        this.invalidate();
    }
}
