package org.libsdl.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class DPadView extends View {

    private Paint normalPaint;
    private Paint pressedPaint;
    private Paint linePaint;

    private boolean isUp = false;
    private boolean isDown = false;
    private boolean isLeft = false;
    private boolean isRight = false;

    private int centerX;
    private int centerY;
    private int radius;
    private int deadzoneRadius;

    private DPadListener listener;

    public DPadView(Context context) {
        super(context);
        init();
    }

    public DPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DPadView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        init();
    }

    private void init() {
        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalPaint.setColor(Color.LTGRAY);
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setAlpha(150);

        pressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pressedPaint.setColor(Color.DKGRAY);
        pressedPaint.setStyle(Paint.Style.FILL);
        pressedPaint.setAlpha(200);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));
        setMeasuredDimension(d, d);

        centerX = d / 2;
        centerY = d / 2;
        radius = (int) (d / 2 * 0.9);
        deadzoneRadius = (int) (radius * 0.2);
    }

    private int measure(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw Right
        canvas.drawArc(rectF, -45, 90, true, isRight ? pressedPaint : normalPaint);
        // Draw Bottom
        canvas.drawArc(rectF, 45, 90, true, isDown ? pressedPaint : normalPaint);
        // Draw Left
        canvas.drawArc(rectF, 135, 90, true, isLeft ? pressedPaint : normalPaint);
        // Draw Top
        canvas.drawArc(rectF, -135, 90, true, isUp ? pressedPaint : normalPaint);

        // Draw dividing lines
        canvas.drawLine(centerX - radius, centerY - radius, centerX + radius, centerY + radius, linePaint);
        canvas.drawLine(centerX - radius, centerY + radius, centerX + radius, centerY - radius, linePaint);

        // Draw deadzone
        canvas.drawCircle(centerX, centerY, deadzoneRadius, normalPaint);
        canvas.drawCircle(centerX, centerY, deadzoneRadius, linePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            updateDirections(false, false, false, false);
            invalidate();
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        float dx = x - centerX;
        float dy = y - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < deadzoneRadius) {
            updateDirections(false, false, false, false);
        } else {
            double angle = Math.atan2(dy, dx) * 180 / Math.PI;

            boolean newUp = false;
            boolean newDown = false;
            boolean newLeft = false;
            boolean newRight = false;

            if (angle >= -22.5 && angle < 22.5) {
                newRight = true;
            } else if (angle >= 22.5 && angle < 67.5) {
                newRight = true;
                newDown = true;
            } else if (angle >= 67.5 && angle < 112.5) {
                newDown = true;
            } else if (angle >= 112.5 && angle < 157.5) {
                newDown = true;
                newLeft = true;
            } else if (angle >= 157.5 || angle < -157.5) {
                newLeft = true;
            } else if (angle >= -157.5 && angle < -112.5) {
                newLeft = true;
                newUp = true;
            } else if (angle >= -112.5 && angle < -67.5) {
                newUp = true;
            } else if (angle >= -67.5 && angle < -22.5) {
                newUp = true;
                newRight = true;
            }

            updateDirections(newUp, newDown, newLeft, newRight);
        }

        invalidate();
        return true;
    }

    private void updateDirections(boolean newUp, boolean newDown, boolean newLeft, boolean newRight) {
        if (listener != null) {
            if (newUp != isUp) {
                if (newUp) listener.onDirectionPress(KeyEvent.KEYCODE_DPAD_UP);
                else listener.onDirectionRelease(KeyEvent.KEYCODE_DPAD_UP);
            }
            if (newDown != isDown) {
                if (newDown) listener.onDirectionPress(KeyEvent.KEYCODE_DPAD_DOWN);
                else listener.onDirectionRelease(KeyEvent.KEYCODE_DPAD_DOWN);
            }
            if (newLeft != isLeft) {
                if (newLeft) listener.onDirectionPress(KeyEvent.KEYCODE_DPAD_LEFT);
                else listener.onDirectionRelease(KeyEvent.KEYCODE_DPAD_LEFT);
            }
            if (newRight != isRight) {
                if (newRight) listener.onDirectionPress(KeyEvent.KEYCODE_DPAD_RIGHT);
                else listener.onDirectionRelease(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        }

        isUp = newUp;
        isDown = newDown;
        isLeft = newLeft;
        isRight = newRight;
    }

    public void setDPadListener(DPadListener listener) {
        this.listener = listener;
    }

    public interface DPadListener {
        void onDirectionPress(int keycode);
        void onDirectionRelease(int keycode);
    }
}
