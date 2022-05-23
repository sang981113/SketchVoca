package kr.ac.jbnu.se.mobile.sketchvoca;


import android.graphics.Paint;


public class PaintPoint {

    private float x, y;
    private boolean isDraw;
    private Paint paint;

    PaintPoint(float x, float y, boolean isDraw, Paint paint) {
        this.x = x;
        this.y = y;
        this.isDraw = isDraw;
        this.paint = paint;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}


