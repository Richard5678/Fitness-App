package com.example.fitness.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import android.util.AttributeSet;


import androidx.core.content.ContextCompat;

import com.example.fitness.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class CustomedBottomNagivationView extends BottomNavigationView {
    Path path;
    Paint paint;

    public CustomedBottomNagivationView(Context context) {
        super(context);
        start();
    }

    public CustomedBottomNagivationView(Context context, AttributeSet attr) {
        super(context, attr);
        start();
    }

    public CustomedBottomNagivationView(Context context, AttributeSet attr, int strAttr) {
        super(context, attr, strAttr);
        start();
    }

    private void start() {
        path = new Path();
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /* Point setup */
        double NavigationBarW = getWidth();
        double NavigationBarH = getHeight();

        // three points
        // P1 is the start of the curve on the left end
        Point P1 = new Point();
        //P2 is the middle of the curve at the bottom
        Point P2 = new Point();
        //P3 is the end of the curve on the right end
        Point P3 = new Point();

        P1.set((int) (NavigationBarW / 2 - 150), 0);
        P2.set((int) (NavigationBarW / 2 ), 60);
        P3.set((int) (NavigationBarW / 2 + 150), 0);

        //Both points are between P1 and P2 that controls the curve from P1 to P2
        Point controlPoint1 = new Point();
        Point controlPoint2 = new Point();

        controlPoint1.set((P1.x + 85), P1.y);
        controlPoint2.set((P2.x - 64), P2.y);

        //Both points are between P2 and P3 that controls the curve from P2 to P3
        Point controlPoint3 = new Point();
        Point controlPoint4 = new Point();

        controlPoint3.set((P2.x + 64), P2.y);
        controlPoint4.set((P3.x - 85), P3.y);

        /* Draw curve*/
        path.reset();
        path.moveTo(0,0);

        //Staight line to P1
        path.lineTo(P1.x, P1.y);

        //Curve from P1 to P2
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, P2.x, P2.y);

        //Curve from P2 to P3
        path.cubicTo(controlPoint3.x, controlPoint3.y, controlPoint4.x, controlPoint4.y, P3.x, P3.y);

        //Line from p3 to the top right corner of the bottom view bar
        path.lineTo((float) NavigationBarW, 0);

        //Line from top right corner to bottom right corner of the bottom view bar
        path.lineTo((float)NavigationBarW, (float)NavigationBarH);

        //Line from bottom right corner to bottom left corner of the bottom view bar
        path.lineTo(0, (float)NavigationBarH);

        //End the path
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
