package com.zgs.turtle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.Math;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TurtleView extends View {
    private static final String TAG = "TurtleView";

    private int widthPixels;
    private int heightPixels;
    private Path path;
    private float lastAngle = 0;
    private float endPointX;
    private float endPointY;
    private float centerX;
    private float centerY;
    private int color;
    private Paint paint;
    private float x;
    private float y;
    private ValueAnimator disAnimator;
    private Queue<Action> queue;
    private int speed = 2000;
    private ValueAnimator valueAnimator;
    private ArrayList<Line> lines;
    private float goToX = 0;
    private float goToY = 0;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public TurtleView(Context context) {
        super(context);
        init();
    }

    public TurtleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TurtleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        heightPixels = displayMetrics.heightPixels;
        Log.d(TAG, "width:" + widthPixels + " height" + heightPixels);
        path = new Path();
        centerX = widthPixels >> 1;
        centerY = heightPixels >> 1;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        endPointX = centerX;
        endPointY = centerY;
        queue = new LinkedBlockingQueue<>();
        lines = new ArrayList<>();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawListLine(canvas);
        canvas.drawLine(endPointX, endPointY, endPointX + x, endPointY + y, paint);
        Log.i(TAG, "className: methodName:\tendPointX:" + endPointX + "\tendPointY:" + endPointY + "\tendPointX + x:" + (endPointX + x) + "\tendPointY + y:" + (endPointY + y));
    }

    private void drawListLine(Canvas canvas) {
        for (Line line : lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
//        if (isGoTo) {
//            isGoTo = false;
//            endPointX = goToX + centerX;
//            endPointY = goToY + centerY;
//        }
    }

    boolean forwardFinish = true;

    public void forward(float dis) {
        Action action = new Action();
        action.type = 0;
        action.dis = dis;
        Log.d(TAG, "lastAngle:" + lastAngle);
        action.angle = lastAngle;
        queue.add(action);
    }

    public void right(float angle) {
        Action action = new Action();
        action.type = 1;
        action.angle = angle;
        lastAngle = lastAngle + angle;
        queue.add(action);
    }

    public void left(float angle) {
        Action action = new Action();
        action.type = 1;
        action.angle = -angle;
        lastAngle = lastAngle - angle;
        queue.add(action);
    }

    boolean isGoTo = false;

    public void gotoPoint(float x, float y) {
        endPointX = centerX + x;
        endPointY = centerY + y;
        isGoTo = true;
        Log.d(TAG, "gotoPoint");
        Log.d(TAG, "endPointX:" + endPointX);
        Action action = new Action();
        action.type = 2;
        action.gotoPointX = endPointX;
        action.gotoPointy = endPointY;
        action.isGoToPoint = true;
        queue.add(action);

    }

    public void start() {
        if (queue.isEmpty()) {
            return;
        }

        Action poll = queue.poll();
        execute(poll);
    }

    void execute(final Action action) {
        switch (action.type) {
            case 0:
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    addLineHistory();
                }
                valueAnimator = ValueAnimator.ofFloat(0, action.dis);
                valueAnimator.setDuration(speed);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
//                        Log.d(TAG, "action.angle:" + action.angle);
                        float value360 = action.angle % 360;
                        float value90 = action.angle / 90;
                        double valueRadius = Math.toRadians(action.angle);
                        x = Math.abs((float) (animatedValue * Math.cos(valueRadius)));
                        y = Math.abs((float) (animatedValue * Math.sin(valueRadius)));
                        if (value360 > 270) {
                            x = x;
                            y = -y;
                        } else if (value360 > 180) {
                            if (value360 == 270) {
                                x = 0;
                            } else {
                                x = -x;
                            }
                            y = -y;
                        } else if (value360 > 90) {
                            if (value360 == 180) {
                                y = 0;
                            } else {
                                y = +y;
                            }
                            x = -x;
                        } else if (value360 > 0) {
                            if (value360 == 90) {
                                x = 0;
                            } else {
                                x = +x;
                            }
                            y = +y;
                        } else if (value360 == 0) {
                            x = x;
                            y = -y;
                        }
//                        Log.d(TAG, "onAnimationUpdate: x  y:" + x + "  " + y);
                        postInvalidate();
                        if (animatedValue == action.dis) {
                            nextAction();
                        }
                    }
                });
                valueAnimator.start();
                break;
            case 1:
                Log.d(TAG, "left");
//                float angle = action.angle;
//                lastAngle = lastAngle-angle;
                nextAction();
                break;
            case 2:
                nextAction();
                break;
        }

    }

    private void addLineHistory() {
        Line line = new Line();
        line.startX = endPointX;
        line.startY = endPointY;
        Log.d(TAG, "addLineHistory");
        Log.d(TAG, "endPointX:" + endPointX);
        endPointX = x + endPointX;
        endPointY = y + endPointY;
        line.endX = endPointX;
        line.endY = endPointY;
        lines.add(line);
        if (isGoTo) {
            isGoTo = false;
            endPointX = centerX + goToX;
            endPointY = centerY + goToY;
        }
    }

    private void nextAction() {
        if (queue.isEmpty()) {
            return;
        }
        execute(queue.poll());
    }


    class Action {
        int type = 0; //forward 0 left 1 gotoPoint 2
        float dis = 0;
        float angle = 0;
        float gotoPointX = 0;
        float gotoPointy = 0;
        boolean isGoToPoint = false;

    }

    class Line {
        float startX;
        float startY;
        float endX;
        float endY;
    }
}

