package com.android.customchart.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.customchart.R;

/**
 * Created by arlong
 *
 * 图表View
 *
 */
public class CustomChartView extends FrameLayout implements Runnable{
    //文字颜色
    private static final int TEXT_COLOR=0xFF98999A;
    //表格线颜色
    private static final int GRID_COLOR=0xFFE1E2E3;
    //bar的背景bar颜色
    private static final int BAR_BG_COLOR=0xFFEDEEEF;

    private Context context;
    private Paint paint;
    //计算出来的滚动区域距离表格左侧文字的距离
    private float leftMargin;
    //计算出来的滚动区域距离底部坐标文字的距离
    private float bottomMargin;
    //纵坐标文字大小
    private float textSizeY;
    //横坐标文字大小
    private float textSizeX;
    //计算出来的表格左侧文字的宽度
    private float textWidth;
    //当前绘制区域
    private Rect bound;
    //每一个bar的宽度
    private int barWidth;

    //每一个bar的颜色，从这里面顺序循环取
    private static int[] BAR_COLORS = new int[]{0xFFFFA82C,0xFF8FC320,0xFF38C7F1,0xFF2B8EFB,0xFF8D91F2};
    private static BarDrawable[] barDrawables;
    private static StaticBarDrawable[] bgDrawables;
    public CustomChartView(Context context) {
        super(context);
        init(context);
    }

    public CustomChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        textSizeY = context.getResources().getDimension(R.dimen.bar_chart_text_size_y);
        textSizeX = context.getResources().getDimension(R.dimen.bar_chart_text_size_x);
        paint.setTextSize(textSizeY);
        textWidth = paint.measureText("很好");
        leftMargin = textWidth + context.getResources().getDimensionPixelSize(R.dimen.bar_chart_text_padding_y)*2;
        barWidth = context.getResources().getDimensionPixelOffset(R.dimen.bar_chart_bar_width);
        bound = new Rect();
        LayoutInflater.from(context).inflate(R.layout.custom_chart_content, this, true);
        setWillNotDraw(false);
    }
    public void init(int[] values,String[] axisText){
        bgDrawables = new StaticBarDrawable[values.length];
        barDrawables = new BarDrawable[values.length];
        LinearLayout bgLayout = (LinearLayout) findViewById(R.id.chart_bg);
        LinearLayout barLayout = (LinearLayout) findViewById(R.id.chart_bar);
        LinearLayout axisLayout = (LinearLayout) findViewById(R.id.chart_axis);
        bgLayout.removeAllViews();
        barLayout.removeAllViews();
        int paddingX = getResources().getDimensionPixelSize(R.dimen.bar_chart_text_padding_x);
        for(int i=0;i<values.length;i++){
            //添加背景bar
            View bg = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(barWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            bgDrawables[i] = new StaticBarDrawable(BAR_BG_COLOR,100,i==0,i == values.length-1);
            bg.setBackgroundDrawable(bgDrawables[i]);
            bgLayout.addView(bg,params);
            //添加前景bar
            View bar = new View(context);
            params = new LinearLayout.LayoutParams(barWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            barDrawables[i] = new BarDrawable(BAR_COLORS[i%BAR_COLORS.length],values[i],i==0,i == values.length-1);
            bar.setBackgroundDrawable(barDrawables[i]);
            barLayout.addView(bar,params);
            //添加x坐标文字
            TextView tv = new TextView(context);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            tv.setMaxLines(2);
            tv.setGravity(Gravity.CENTER);
            tv.setText(axisText[i]);
            tv.setPadding(paddingX,0,paddingX,0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeX);
            params = new LinearLayout.LayoutParams(barWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            axisLayout.addView(tv,params);
        }
        post(this);
    }
    public void update(int index,int value){
        if(barDrawables != null && index<barDrawables.length){
            barDrawables[index].update(value);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(GRID_COLOR);
        getDrawingRect(bound);
        bound.set(0,0, bound.width(), (int) (bound.height()-bottomMargin));
        //四周边框
        canvas.drawRect(0, 0, bound.width(), bound.height(), paint);
        //三条横线
        canvas.drawLine(0,bound.height()*0.2f,bound.width(),bound.height()*0.2f,paint);
        canvas.drawLine(0,bound.height()*0.5f,bound.width(),bound.height()*0.5f,paint);
        canvas.drawLine(0,bound.height()*0.8f,bound.width(),bound.height()*0.8f,paint);
        //一条竖线
        canvas.drawLine(leftMargin, 0, leftMargin, bound.height(), paint);
        paint.setColor(TEXT_COLOR);
        canvas.drawText("很好",(leftMargin-textWidth)/2,bound.height()*.2f-paint.descent(),paint);
        canvas.drawText("一般",(leftMargin-textWidth)/2,bound.height()*.5f-paint.descent(),paint);
        canvas.drawText("较差", (leftMargin - textWidth) / 2, bound.height() * .8f - paint.descent(), paint);
        super.onDraw(canvas);
    }

    @Override
    public void run() {
        LayoutParams params = (LayoutParams) getChildAt(0).getLayoutParams();
        params.width = (int) (getWidth()-leftMargin);
        params.gravity = Gravity.RIGHT;
        bottomMargin = findViewById(R.id.chart_axis).getHeight();
        requestLayout();
    }
}
