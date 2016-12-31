package com.zhang.screenlock;

/**
 * Created by Mr.Z on 2016/12/31 0031.
 */

public class Point {
    public static int STATE_NORMAL = 0;
    public static int STATE_PRESSED = 1;
    public static int STATE_ERROR = 2;

    float x;
    float y;
    int state = STATE_NORMAL;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float distance(Point p) {
        return (float) Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }
}
