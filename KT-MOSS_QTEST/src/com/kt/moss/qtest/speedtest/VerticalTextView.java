package com.kt.moss.qtest.speedtest;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalTextView extends TextView {
    final boolean topDown;
    
    private int width;
    public VerticalTextView(Context context, AttributeSet attrs, int _width) {
        super(context, attrs);
        
        width = _width;
        
        final int gravity = getGravity();
        if (Gravity.isVertical(gravity)
                && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
                    | Gravity.TOP);
            topDown = false;
        }
        else
            topDown = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (topDown) {
        	 int ipWidth = 10;
             
             if(width  == 240){
                 ipWidth = 10;
             }else if(width  == 320){
                 ipWidth = 15;
             }else if(width  == 480){
                 ipWidth = 20;
             }
          
            canvas.translate(getWidth() / 2 + ipWidth, 0);
            canvas.rotate(90);
        }
        else {
            canvas.translate(0, getHeight() / 2);
            canvas.rotate(-90);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }
}
