package com.android.customchart.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * 带有文字的柱状图形
 */
public class BarDrawable extends BaseBarDrawable implements ValueAnimator.AnimatorUpdateListener {
    private String text;

    public BarDrawable(int color,int num,boolean leftCorner,boolean rightCorner) {
        super(color,num,leftCorner,rightCorner);
        updateText();
    }
    private void updateText(){
        text = percent +"%";
        invalidateSelf();
    }
    public void update(int num){
        if(num != this.percent) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(this.percent * 10, num * 10);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setDuration(2000).start();
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //绘制圆圈
        paint.setColor(0xFFFFFFFF);
        float innerRadios = unit * 0.9f;
        canvas.drawCircle(unit * 2, unit + top, innerRadios, paint);
        //绘制文字
        paint.setColor(color);
        paint.setTextSize(unit * 0.7f);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        float textWidth = rect.width();
        canvas.drawText(text, 0, text.length(), unit + (unit * 2 - textWidth) / 2, top + unit - rect.top - rect.height() / 2, paint);

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
        return 0;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        percent = ((Integer) animation.getAnimatedValue() / 10);
        fractor = ((Integer) animation.getAnimatedValue());
        updateText();
    }
}
