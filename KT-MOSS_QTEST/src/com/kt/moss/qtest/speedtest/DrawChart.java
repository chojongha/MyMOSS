package com.kt.moss.qtest.speedtest;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.kt.moss.qtest.R;

public class DrawChart {
}

class Pin extends View {

    private int width = 0;
    private int height = 0;

    View vi = this;
    boolean button = false;

    int angle = 1;

    int rotate = 0;
    int value = 0;
    int save = 0;

    private int imgWidth = 0;
    private int imgHeight = 0;

    private int imgWidth2 = 0;
    private int capHeight2 = 0;

    private int capWidth = 0;
    private int capheight = 0;

    private Bitmap downimg = null;
    private Bitmap upimg = null;

    private Bitmap downimg0 = null;
    private Bitmap upimg0 = null;

    private Bitmap downimg1 = null;
    private Bitmap upimg1 = null;

    private Bitmap img = null;
    private Bitmap cap = null;

    private Timer timer = null;
    private PinTimer pinTask = null;

    private boolean imgSwitch = true;
    private boolean imgSwitch2 = true;

    public Pin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStat(boolean um) {
        imgSwitch2 = um;
    }

    public void start(int value) {
        this.value = value + (value % angle);

        timer = new Timer();
        pinTask = new PinTimer();

        if (save + angle <= this.value) {
            button = true;
            timer.schedule(pinTask, 0, 10);
        }
        else if (save - angle >= this.value) {
            button = false;
            timer.schedule(pinTask, 0, 10);
        }

        if (imgSwitch) {
            if (imgSwitch2) {
                downimg = downimg1;
                upimg = upimg0;
            }
            else {
                downimg = downimg0;
                upimg = upimg1;
            }
            imgSwitch = false;
        }
        else {
            downimg = downimg0;
            upimg = upimg0;
            imgSwitch = true;
        }

        if (value == 0) {
            downimg = downimg0;
            upimg = upimg0;
        }
        vi.postInvalidate();

        save = this.value;
    }// start

    public void onDraw(Canvas canvas) {

        if (img != null) {
            if (!(this.getWidth() >= 720)) {
                canvas.drawBitmap(downimg, scale(width, 22), height - scale(height, 31), null);
                canvas.drawBitmap(upimg, width - scale(width, 37), height - scale(height, 31), null);

                canvas.rotate(rotate, imgWidth2, height - (imgHeight * 2.7f - imgHeight / 2));
                canvas.drawBitmap(img, imgWidth2 - imgWidth, height - imgHeight * 2.7f, null);
                canvas.rotate(-rotate, imgWidth2, height - (imgHeight * 2.7f - imgHeight / 2));

                canvas.drawBitmap(cap, imgWidth2 - capWidth / 2, height - capheight, null);
            }
            else {
                canvas.drawBitmap(downimg, scale(width, 14), height - scale(height, 34), null);
                canvas.drawBitmap(upimg, width - scale(width, 32), height - scale(height, 34), null);

                canvas.rotate(rotate, imgWidth2, height - (imgHeight * 4.1f - imgHeight / 2));
                canvas.drawBitmap(img, imgWidth2 - imgWidth, height - imgHeight * 4.1f, null);
                canvas.rotate(-rotate, imgWidth2, height - (imgHeight * 4.1f - imgHeight / 2));

                canvas.drawBitmap(cap, imgWidth2 - capWidth / 2, height - capheight * 1.2f, null);
            }

        }// if

    }// onDraw

    class PinTimer extends TimerTask {

        @Override
        public void run() {

            if (button) {
                rotate += angle;

                if (rotate >= value) {
                    this.cancel();
                }
                if (rotate > 180) {
                    rotate = 180;
                }
            }
            else {
                rotate -= angle;

                if (rotate <= value) {
                    this.cancel();
                }
                if (rotate < 0) {
                    rotate = 0;
                }
            }// else

            vi.postInvalidate();
        }// run

    }// class pinTimer

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        width = this.getWidth();
        height = this.getHeight();

        if (!(this.getWidth() >= 720)) {
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speedometer_needle), scale(width, 35), scale(height, 5), true);
            cap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speedometer_needle_cap), scale(height, 20), scale(height, 20), true);

            downimg0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_down_off), scale(width, 15), scale(height, 18), true);
            upimg0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_up_off), scale(width, 15), scale(height, 18), true);

            downimg1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_down_on), scale(width, 15), scale(height, 18), true);
            upimg1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_up_on), scale(width, 15), scale(height, 18), true);
        }
        else {
            img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speedometer_needle), scale(width, 40), scale(height, 4), true);
            cap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speedometer_needle_cap), scale(height, 20), scale(height, 20), true);

            downimg0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_down_off), scale(width, 18), scale(height, 16), true);
            upimg0 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_up_off), scale(width, 18), scale(height, 16), true);

            downimg1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_down_on), scale(width, 18), scale(height, 16), true);
            upimg1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_speedometer_up_on), scale(width, 18), scale(height, 16), true);
        }
        downimg = downimg0;
        upimg = upimg0;

        imgHeight = img.getHeight();
        imgWidth = img.getWidth();

        capWidth = cap.getWidth();
        capheight = cap.getHeight();

        imgWidth2 = width / 2;
        capHeight2 = capheight / 2;

        this.invalidate();

    }

    public int scale(int num, int per) {
        return num * per / 100;
    }

}// pin

class BarChart extends View {

    private int width = 0;
    private int height = 0;

    private int spaceX = 0;
    private int spaceY = 0;

    private int marginLeft = 0;
    // info
    public int max = 100;

    private int cell = 10;

    private int[] data = new int[20];
    private Paint pnt = new Paint();

    public void setLine(int[] line, int num) {
        data = line;
        max = num / 1000;
        this.postInvalidate();
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            pnt.setColor(getResources().getColor(R.color.olleh_blackGray));

    }// BarChart

    public void onDraw(Canvas c) {

        int[] step = data;

        for (int i = 0; i < step.length * 2; i++) {

            if (i % 2 == 0) {
                int x2 = height - (step[i / 2] * height / max);

                c.drawRect((i * spaceX) + marginLeft, x2 + marginLeft, ((i + 1.6f) * spaceX) + marginLeft, height + marginLeft, pnt);
            }

        }// for

    }// onDraw

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        width = scale(this.getWidth(), 89);
        height = scale(this.getHeight(), 80);

        marginLeft = scale(this.getWidth(), 10);

        spaceX = width / (data.length * 2);
        spaceY = height / cell;

        this.invalidate();

    }

    public int scale(int num, int per) {
        return (int) (num * per / 100.f);
    }

}// View

class Capsule extends View {

    private int width = 0;
    private int height = 0;

    int imgWidth = 0;
    int imgHeight = 0;

    // Paint set
    public Paint bgRect = new Paint();

    // info
    private int value = 0;

    Bitmap img = null;

    public Capsule(Context context, AttributeSet attrs) {
        super(context, attrs);

        value = 0;

        bgRect.setAntiAlias(true);

    }

    public void setValue(int value) {
        this.value = value;
        this.postInvalidate();
    }

    public void onDraw(Canvas c) {

        if (img != null) {
            c.drawRect(width - (imgWidth * 0.95f), height - imgHeight, scale(imgWidth, value) + (width - (imgWidth * 0.95f)), imgHeight, bgRect);
            c.drawBitmap(img, scale(imgWidth, 5), scale(imgHeight, 2), null);
        }

    }// onDraw

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        width = this.getWidth();
        height = this.getHeight();

        imgWidth = scale(width, 97);
        imgHeight = scale(height, 95);

        img = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_device_status_graph), imgWidth, imgHeight, true);

        this.invalidate();

    }

    public int scale(int num, int per) {
        return num * per / 100;
    }

}// View

class LineChart extends View {

    private int width;
    private int height;

    private float spaceX;
    private int spaceY;

    private float xx = 0;
    private float xx2 = 0;
    private float yy = 0;

    int[] step = new int[100];

    // Paint set
    private Paint bgRect = new Paint();
    private Paint pnt = new Paint();
    private Paint pnt2 = new Paint();

    private Paint textpnt = new Paint();
    private Paint textpnt2 = new Paint();

    private String mins = "";
    private String maxs = "";
    private String losts = "";

    // info
    private int max = 100;
    private int cell = 3;

    public void setLine(int[] line, String min, String max, String lost) {
        step = line;
        mins = min;
        maxs = max;
        losts = lost;

        this.postInvalidate();
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        bgRect.setAntiAlias(true);
        bgRect.setColor(Color.GRAY);
        bgRect.setStrokeWidth((float) 0.2);

        pnt.setAntiAlias(true);
        pnt.setColor(Color.BLUE);

        pnt2.setAntiAlias(true);
        pnt2.setColor(Color.RED);

        textpnt.setAntiAlias(true);

        textpnt2.setAntiAlias(true);
        textpnt2.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

    }// LineCHart

    public void onDraw(Canvas c) {

        if (!(this.getWidth() >= 720)) {
            textpnt.setTextSize(15);
            textpnt2.setTextSize(17);

            width = this.getWidth();
            height = scale(this.getHeight(), 42);

            xx = scale(this.getWidth(), 7);
            xx2 = scale(this.getWidth(), 5);
            yy = scale(height, 70);

            spaceX = scale(width, 70) * 1 / 100f;
            spaceY = height / cell;

//            c.drawLine(xx2, height + yy, xx2 + scale(width, 74), height + yy, bgRect);
//            c.drawLine(xx2, yy, xx2 + scale(width, 74), yy, bgRect);
            
            for (int i = 1; i < cell; i++) {
                c.drawLine(xx2, spaceY * i + yy, xx2 + scale(width, 74), spaceY * i + yy, bgRect);
            }

            for (int i = 0; i < step.length; i++) {

                if (i < step.length - 1) {

                    int x1 = height - (step[i] * height / max);
                    int x2 = height - (step[i + 1] * height / max);

                    c.drawLine(xx + (i * spaceX), x1 + yy, xx + ((i + 1) * spaceX), x2 + yy, pnt);

                    if (step[i] >= 100) {
                        c.drawLine(xx + (i * spaceX), yy, xx + (i * spaceX), height + yy, pnt2);
                    }
                }// if
            }// for

            c.drawText("1000", scale(this.getWidth(), 80), scale(this.getHeight(), 36), textpnt);
            c.drawText("    50", scale(this.getWidth(), 80), scale(this.getHeight(), 54), textpnt);
            c.drawText("      0", scale(this.getWidth(), 80), scale(this.getHeight(), 73), textpnt);

            c.drawText("최소: " + mins + " ms", scale(this.getWidth(), 32), scale(this.getHeight(), 87), textpnt2);
            c.drawText("최대: " + maxs + " ms", scale(this.getWidth(), 5), scale(this.getHeight(), 87), textpnt2);
            c.drawText("손실율: " + losts + " %", scale(this.getWidth(), 57), scale(this.getHeight(), 87), textpnt2);

        }
        else {
            textpnt.setTextSize(20);
            textpnt2.setTextSize(23);

            width = this.getWidth();
            height = scale(this.getHeight(), 42);

            xx = scale(this.getWidth(), 7);
            xx2 = scale(this.getWidth(), 6);
            yy = scale(height, 70);

            spaceX = scale(width, 73) * 1 / 100f;
            spaceY = height / cell;

//            c.drawLine(xx2, height + yy, xx2 + scale(width, 74), height + yy, bgRect);
//            c.drawLine(xx2, yy, xx2 + scale(width, 74), yy, bgRect);

            for (int i = 1; i < cell; i++) {
                c.drawLine(xx2, spaceY * i + yy, xx2 + scale(width, 74), spaceY * i + yy, bgRect);
            }

            for (int i = 0; i < step.length; i++) {

                if (i < step.length - 1) {

                    int x1 = height - (step[i] * height / max);
                    int x2 = height - (step[i + 1] * height / max);

                    c.drawLine(xx + (i * spaceX), x1 + yy, xx + ((i + 1) * spaceX), x2 + yy, pnt);

                    if (step[i] >= 100) {
                        c.drawLine(xx + (i * spaceX), yy, xx + (i * spaceX), height + yy, pnt2);
                    }
                }// if
            }// for

            c.drawText("1000", scale(this.getWidth(), 84), scale(this.getHeight(), 30), textpnt);
            c.drawText("    50", scale(this.getWidth(), 84), scale(this.getHeight(), 52), textpnt);
            c.drawText("      0", scale(this.getWidth(), 84), scale(this.getHeight(), 73), textpnt);

            c.drawText("최소: " + mins + " ms", scale(this.getWidth(), 34), scale(this.getHeight(), 87), textpnt2);
            c.drawText("최대: " + maxs + " ms", scale(this.getWidth(), 7), scale(this.getHeight(), 87), textpnt2);
            c.drawText("손실율: " + losts + " %", scale(this.getWidth(), 59), scale(this.getHeight(), 87), textpnt2);

        }
    }// onDraw

    public int scale(int num, int per) {
        return num * per / 100;
    }// public

}// View

