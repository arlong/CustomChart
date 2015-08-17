package com.android.customchart.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by arlong
 *
 * 柱状图形背景
 */
public class BaseBarDrawable extends Drawable {
    /**
     * 当前绘制用的颜色
     */
    protected int color;
    /**
     * 画笔
     */
    protected Paint paint;
    /**
     * 一个用于复用的RectF，用于arcTo函数
     */
    protected RectF rectF;
    /**
     * 整个bar的轮廓
     */
    protected Path path;
    /**
     * 当前的显示百分比（0~100）
     */
    public int percent;
    /**
     * 内部的千分比(0~1000)，用于绘制，保证绘制精度
     */
    protected int fractor;
    /**
     * 绘制的单位长度，绘制时将整个绘制区域划分为单位长度为unit的区域
     */
    protected int unit;
    /**
     * 上半个圆弧的右侧点y坐标
     */
    protected int top;

    private boolean leftCorner;
    private boolean rightCorner;
    public BaseBarDrawable(int color, int num, boolean leftCorner, boolean rightCorner) {
        super();
        this.color = color;
        this.fractor = num*10;
        this.percent = num;
        this.leftCorner = leftCorner;
        this.rightCorner = rightCorner;
        rectF = new RectF();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        path = new Path();
    }
    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();
        drawBar(canvas, rect.width(), rect.height(),leftCorner, rightCorner);
    }
    private void drawBar(Canvas canvas,int width,int height,boolean leftCorner,boolean rightCorner){
        //绘制背景
        paint.setColor(color);
        unit = width/4;
        top = height - ((height - unit*2)* fractor /1000+unit*2);
        path.moveTo(unit * 3, top);
        path.lineTo(unit * 3, height - unit);
        if(rightCorner){
            rectF.set(unit,height-unit*2,unit*3,height);
            path.arcTo(rectF,0,90);
        }else {
            rectF.set(unit * 3, height - unit * 2, unit * 5, height);
            path.arcTo(rectF, 180, -90);
        }
        path.lineTo(0, height);
        if(leftCorner){
            rectF.set(unit,height-unit*2,unit*3,height);
            path.arcTo(rectF, 90, 90);
        }else{
            rectF.set(-unit,height-unit*2,unit,height);
            path.arcTo(rectF,90,-90);
        }
        path.lineTo(unit, top);
        rectF.set(unit, top, unit * 3, unit * 2+top);
        path.arcTo(rectF, 180, 180);
        canvas.drawPath(path, paint);
        path.reset();
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }
}
