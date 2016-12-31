package com.zhang.screenlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Z on 2016/12/31 0031.
 */

public class GestureLock extends View {

    private Point[][] points = new Point[3][3];
    private boolean inited = false;
    private boolean isDraw = false;

    private ArrayList<Point> pressedPoints = new ArrayList<>();
    private ArrayList<Integer> pointsNum = new ArrayList<>();

    private Bitmap bmpPointN;
    private Bitmap bmpPointP;
    private Bitmap bmpPointE;

    private float bmpR;
    private float mouseX;
    private float mouseY;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint pressedPaint = new Paint();
    private Paint errorPaint = new Paint();

    private OnDrawFinishedListener listener;

    public GestureLock(Context context) {
        super(context);
    }

    public GestureLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        int[] ij;
        int i;
        int j;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoints();

                ij = getSelectedPoint();
                if (ij != null) {
                    isDraw = true;
                    i = ij[0];
                    j = ij[1];
                    points[i][j].state = Point.STATE_PRESSED;
                    pressedPoints.add(points[i][j]);
                    pointsNum.add(i * 3 + j);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDraw) {
                    ij = getSelectedPoint();
                    if (ij != null) {
                        i = ij[0];
                        j = ij[1];
                        if (!pressedPoints.contains(points[i][j])) {
                            points[i][j].state = Point.STATE_PRESSED;
                            pressedPoints.add(points[i][j]);
                            pointsNum.add(i * 3 + j);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean valid = false;
                if (listener != null && isDraw) {
                    valid = listener.onDrawFinished(pointsNum);
                }
                if (!valid) {
                    for (Point point : pressedPoints) {
                        point.state = Point.STATE_ERROR;
                    }
                }
                isDraw = false;
                break;
        }
        this.postInvalidate();
        return true;
    }

    public void resetPoints() {
        pressedPoints.clear();
        pointsNum.clear();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        this.postInvalidate();
    }

    private int[] getSelectedPoint() {
        Point mouseP = new Point(mouseX, mouseY);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].distance(mouseP) < bmpR) {
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!inited) {
            init();
        }

        drawPoints(canvas);

        if (pressedPoints.size() > 0) {
            Point a = pressedPoints.get(0);
            for (int i = 1; i < pressedPoints.size(); i++) {
                Point b = pressedPoints.get(i);
                drawLine(canvas, a, b);
                a = b;
            }
            if (isDraw) {
                drawLine(canvas, a, new Point(mouseX, mouseY));
            }
        }
    }

    private void drawLine(Canvas canvas, Point a, Point b) {
        if (a.state == Point.STATE_PRESSED) {
            canvas.drawLine(a.x, a.y, b.x, b.y, pressedPaint);
        } else if (a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x, a.y, b.x, b.y, errorPaint);
        }
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].state == Point.STATE_NORMAL) {
                    canvas.drawBitmap(bmpPointN, points[i][j].x - bmpR, points[i][j].y - bmpR, paint);
                } else if (points[i][j].state == Point.STATE_PRESSED) {
                    canvas.drawBitmap(bmpPointP, points[i][j].x - bmpR, points[i][j].y - bmpR, paint);
                } else if (points[i][j].state == Point.STATE_ERROR) {
                    canvas.drawBitmap(bmpPointE, points[i][j].x - bmpR, points[i][j].y - bmpR, paint);
                }
            }
        }
    }

    private void init() {

        bmpPointN = BitmapFactory.decodeResource(getResources(), R.drawable.normal);
        bmpPointP = BitmapFactory.decodeResource(getResources(), R.drawable.press);
        bmpPointE = BitmapFactory.decodeResource(getResources(), R.drawable.error);

        bmpR = bmpPointN.getHeight() / 2;

        pressedPaint.setColor(Color.YELLOW);
        pressedPaint.setStrokeWidth(5);
        errorPaint.setColor(Color.RED);
        errorPaint.setStrokeWidth(5);

        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width - height) / 2;
        int offsetX, offsetY;
        int space;
        if (width > height) {
            space = height / 4;
            offsetX = offset;
            offsetY = 0;
        } else {
            space = width / 4;
            offsetX = 0;
            offsetY = offset;
        }

        points[0][0] = new Point(offsetX + space, offsetY + space);
        points[0][1] = new Point(offsetX + space * 2, offsetY + space);
        points[0][2] = new Point(offsetX + space * 3, offsetY + space);

        points[1][0] = new Point(offsetX + space, offsetY + space * 2);
        points[1][1] = new Point(offsetX + space * 2, offsetY + space * 2);
        points[1][2] = new Point(offsetX + space * 3, offsetY + space * 2);

        points[2][0] = new Point(offsetX + space, offsetY + space * 3);
        points[2][1] = new Point(offsetX + space * 2, offsetY + space * 3);
        points[2][2] = new Point(offsetX + space * 3, offsetY + space * 3);

        inited = true;
    }

    public interface OnDrawFinishedListener {
        boolean onDrawFinished(List<Integer> pointsNum);
    }

    public void setOnDrawFinishedListener(OnDrawFinishedListener listener) {
        this.listener = listener;
    }
}
