package gps.trackerid.location.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import gps.trackerid.location.R;

public final class SpeedyView extends View {

    public static final /* synthetic */ int f3588B = 0;

    public final ValueAnimator f3589A;

    public int f3590e = 60;

    public float f3591f = 36.0f;

    public float f3592g = 50.0f;

    public int f3593h = Color.parseColor("#402c47");

    public int f3594i = Color.parseColor("#d83a78");

    public int f3595j = Color.parseColor("#f5f5f5");

    public String f3596k = "km/h";

    public String f3597l = "00:00";

    public final RectF f3598m = new RectF();

    public final RectF f3599n = new RectF();

    public final Rect f3600o = new Rect();

    public float f3601p = 220.0f;

    public int f3602q;

    public final Paint f3603r;

    public final Paint f3604s;

    public final Paint f3605t;

    public final Paint f3606u;

    public final Paint f3607v;

    public final Paint f3608w;

    public final Paint f3609x;

    public final Paint f3610y;

    public final Paint f3611z;

    public SpeedyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        Typeface createFromAsset = Typeface.createFromAsset(getContext().getAssets(), "fonts/digital.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getBorderColor());
        paint.setStrokeWidth(getBorderSize());
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.f3603r = paint;
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(getFillColor());
        paint2.setStrokeWidth(getBorderSize());
        paint2.setStrokeCap(Paint.Cap.ROUND);
        this.f3604s = paint2;
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setColor(getBorderColor());
        paint3.setStrokeWidth(4.0f);
        paint3.setStrokeCap(Paint.Cap.ROUND);
        this.f3605t = paint3;
        Paint paint4 = new Paint();
        paint4.setAntiAlias(true);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setColor(getBorderColor());
        paint4.setStrokeWidth(4.0f);
        paint4.setStrokeCap(Paint.Cap.BUTT);
        this.f3606u = paint4;
        Paint paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint5.setStyle(Paint.Style.STROKE);
        paint5.setColor(getBorderColor());
        paint5.setStrokeWidth(2.0f);
        paint5.setStrokeCap(Paint.Cap.BUTT);
        this.f3607v = paint5;
        Paint paint6 = new Paint();
        paint6.setAntiAlias(true);
        paint6.setStyle(Paint.Style.FILL);
        paint6.setColor(getTextColor());
        paint6.setTextSize(40.0f);
        this.f3608w = paint6;
        TextView textView = new TextView(getContext());
        textView.setTextAppearance(2132017611);
        TextView textView2 = new TextView(getContext());
        textView2.setTextAppearance(2132017610);
        TextView textView3 = new TextView(getContext());
        textView3.setTextAppearance(2132017630);
        Paint paint7 = new Paint();
        paint7.setAntiAlias(true);
        paint7.setStyle(Paint.Style.FILL);
        paint7.setColor(getTextColor());
        paint7.setTextSize(textView.getTextSize() * ((float) 2));
        paint7.setTypeface(createFromAsset);
        this.f3609x = paint7;
        Paint paint8 = new Paint();
        paint8.setAntiAlias(true);
        paint8.setStyle(Paint.Style.FILL);
        paint8.setColor(getTextColor());
        paint8.setTextSize(textView2.getTextSize());
        paint8.setTypeface(createFromAsset);
        this.f3610y = paint8;
        Paint paint9 = new Paint();
        paint9.setAntiAlias(true);
        paint9.setStyle(Paint.Style.FILL);
        paint9.setColor(getTextColor());
        paint9.setTextSize(textView3.getTextSize());
        paint9.setTypeface(createFromAsset);
        this.f3611z = paint9;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[0]);
        ofFloat.setInterpolator(new AccelerateDecelerateInterpolator());
        this.f3589A = ofFloat;
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SpeedyView, 0, 0);
        try {
            setMaxSpeed(ta.getInt(R.styleable.SpeedyView_sv_maxSpeed, getMaxSpeed()));
            setBorderSize(ta.getDimension(R.styleable.SpeedyView_sv_borderSize, getBorderSize()));
            setTextGap(ta.getDimension(R.styleable.SpeedyView_sv_textGap, getTextGap()));

            String metric = ta.getString(R.styleable.SpeedyView_sv_metricText);
            setMetricText(metric == null ? getMetricText() : metric);

            String timer = ta.getString(R.styleable.SpeedyView_sv_metricText); // if separate attr not defined
            setTimerText(timer == null ? getTimerText() : timer);

            setBorderColor(ta.getColor(R.styleable.SpeedyView_sv_borderColor, getBorderColor()));
            setFillColor(ta.getColor(R.styleable.SpeedyView_sv_fillColor, getBorderColor()));
            setTextColor(ta.getColor(R.styleable.SpeedyView_sv_textColor, getBorderColor()));
        } catch (Exception unused) {
        } catch (Throwable th) {
            ta.recycle();
            throw th;
        }
        ta.recycle();
    }

    private final float getCenterX() {
        return ((float) getWidth()) / 2.0f;
    }

    private final float getCenterY() {
        return ((float) getHeight()) / 2.0f;
    }

    private final int getMajorSteps() {
        if (getMaxSpeed() < 140) {
            return 10;
        }
        if (getMaxSpeed() < 280) {
            return 20;
        }
        return getMaxSpeed() < 440 ? 40 : 50;
    }

    private final int getMinorSteps() {
        if (getMaxSpeed() < 140) {
            return 2;
        }
        if (getMaxSpeed() < 280) {
            return 4;
        }
        return getMaxSpeed() < 440 ? 8 : 10;
    }

    /* renamed from: a */
    public final void mo3685a(Canvas canvas, String str, float f, float f2, Paint paint) {
        paint.getTextBounds(str, 0, str.length(), this.f3600o);
        canvas.drawText(str, f - this.f3600o.exactCenterX(), f2 - this.f3600o.exactCenterY(), paint);
    }

    /* renamed from: b */
    public final float mo3686b(int i) {
        return ((-260.0f / ((float) (getMaxSpeed() + 0))) * ((float) (i + 0))) + 220.0f;
    }

    public final int getBorderColor() {
        return this.f3593h;
    }

    public final float getBorderSize() {
        return this.f3591f;
    }

    public final int getFillColor() {
        return this.f3594i;
    }

    public final int getMaxSpeed() {
        return this.f3590e;
    }

    public final String getMetricText() {
        return this.f3596k;
    }

    public final int getTextColor() {
        return this.f3595j;
    }

    public final float getTextGap() {
        return this.f3592g;
    }

    public final String getTimerText() {
        return this.f3597l;
    }
    public void setSpeed(int speed) {
        this.f3602q = speed;
        this.f3601p = mo3686b(speed);
        invalidate();
    }

//    public void setSpeed(int speed) {
//
//        if (speed < 0) speed = 0;
//        if (speed > getMaxSpeed()) speed = getMaxSpeed();
//
//        final float targetAngle = mo3686b(speed);
//
//        f3589A.cancel();
//        f3589A.setFloatValues(f3601p, targetAngle);
//        f3589A.setDuration(600); // smooth animation
//        f3589A.addUpdateListener(animation -> {
//            f3601p = (float) animation.getAnimatedValue();
//            invalidate();
//        });
//        f3589A.start();
//
//        f3602q = speed;
//    }


    public static final int m5051a(int i, int i2, int i3) {
        if (i3 > 0) {
            if (i >= i2) {
                return i2;
            }
            int i4 = i2 % i3;
            if (i4 < 0) {
                i4 += i3;
            }
            int i5 = i % i3;
            if (i5 < 0) {
                i5 += i3;
            }
            int i6 = (i4 - i5) % i3;
            if (i6 < 0) {
                i6 += i3;
            }
            return i2 - i6;
        } else if (i3 >= 0) {
            throw new IllegalArgumentException("Step is zero.");
        } else if (i <= i2) {
            return i2;
        } else {
            int i7 = -i3;
            int i8 = i % i7;
            if (i8 < 0) {
                i8 += i7;
            }
            int i9 = i2 % i7;
            if (i9 < 0) {
                i9 += i7;
            }
            int i10 = (i8 - i9) % i7;
            if (i10 < 0) {
                i10 += i7;
            }
            return i2 + i10;
        }
    }

    public final void onDraw(Canvas canvas) {
        int i;
        String str;
        int maxSpeed = getMaxSpeed();
        int majorSteps = getMajorSteps();
        String str2 = "Step must be positive, was: ";
        if (majorSteps > 0) {
            int i2 = 0;
            int a = m5051a(0, maxSpeed, majorSteps);
            if (a >= 0) {
                int i3 = 0;
                while (true) {
                    canvas.drawLine((((getCenterX() - getBorderSize()) - 50.0f) * ((float) Math.cos((double) (mo3686b(i3) * 0.017453292f)))) + getCenterX(), getCenterY() - (((getCenterY() - getBorderSize()) - 50.0f) * ((float) Math.sin((double) (mo3686b(i3) * 0.017453292f)))), (((getCenterX() - getBorderSize()) - 10.0f) * ((float) Math.cos((double) (mo3686b(i3) * 0.017453292f)))) + getCenterX(), getCenterY() - (((getCenterY() - getBorderSize()) - 10.0f) * ((float) Math.sin((double) (mo3686b(i3) * 0.017453292f)))), this.f3606u);
                    String valueOf = String.valueOf(i3);
                    float centerX = (((((getCenterX() - getBorderSize()) - 50.0f) - 10.0f) - 30.0f) * ((float) Math.cos((double) (mo3686b(i3) * 0.017453292f)))) + getCenterX();
                    float centerY = getCenterY() - (((((getCenterY() - getBorderSize()) - 50.0f) - 10.0f) - 30.0f) * ((float) Math.sin((double) (mo3686b(i3) * 0.017453292f))));
                    int i4 = i3;
                    String str3 = valueOf;
                    int i5 = a;
                    float f = centerX;
                    i = i2;
                    float f2 = centerY;
                    str = str2;
                    mo3685a(canvas, str3, f, f2, this.f3608w);
                    if (i4 == i5) {
                        break;
                    }
                    i3 = i4 + majorSteps;
                    a = i5;
                    str2 = str;
                    i2 = i;
                }
            } else {
                i = 0;
                str = str2;
            }
            int majorSteps2 = getMajorSteps();
            int maxSpeed2 = getMaxSpeed();
            int minorSteps = getMinorSteps();
            if (minorSteps > 0) {
                int a2 = m5051a(i, maxSpeed2, minorSteps);
                if (a2 >= 0) {
                    int i6 = i;
                    while (true) {
                        if (i6 % majorSteps2 != 0) {
                            canvas.drawLine((((getCenterX() - getBorderSize()) - 25.0f) * ((float) Math.cos((double) (mo3686b(i6) * 0.017453292f)))) + getCenterX(), getCenterY() - (((getCenterY() - getBorderSize()) - 25.0f) * ((float) Math.sin((double) (mo3686b(i6) * 0.017453292f)))), (((getCenterX() - getBorderSize()) - 10.0f) * ((float) Math.cos((double) (mo3686b(i6) * 0.017453292f)))) + getCenterX(), getCenterY() - (((getCenterY() - getBorderSize()) - 10.0f) * ((float) Math.sin((double) (mo3686b(i6) * 0.017453292f)))), this.f3607v);
                        }
                        if (i6 == a2) {
                            break;
                        }
                        i6 += minorSteps;
                    }
                }
                canvas.drawArc(this.f3598m, 140.0f, 260.0f, false, this.f3603r);
                canvas.drawArc(this.f3598m, 140.0f, 220.0f - this.f3601p, false, this.f3604s);
                canvas.drawArc(this.f3599n, 140.0f, 260.0f, false, this.f3605t);
                mo3685a(canvas, String.valueOf(this.f3602q), ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, this.f3609x);
                float f3 = (float) 2;
                mo3685a(canvas, getMetricText(), ((float) getWidth()) / 2.0f, getTextGap() + (this.f3609x.getTextSize() / f3) + (((float) getHeight()) / 2.0f), this.f3610y);
                mo3685a(canvas, getTimerText(), ((float) getWidth()) / 2.0f, getTextGap() + (this.f3610y.getTextSize() / f3) + getTextGap() + (this.f3609x.getTextSize() / f3) + (((float) getHeight()) / 2.0f), this.f3611z);
                return;
            }
            throw new IllegalArgumentException(str + minorSteps + ".");
        }
        throw new IllegalArgumentException(str2 + majorSteps + ".");
    }

    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(i, i);
    }

    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        float f = (float) 2;
        this.f3598m.set(getBorderSize() / f, getBorderSize() / f, ((float) getWidth()) - (getBorderSize() / f), ((float) getWidth()) - (getBorderSize() / f));
        this.f3599n.set(getBorderSize() + 10.0f, getBorderSize() + 10.0f, (((float) getWidth()) - getBorderSize()) - 10.0f, (((float) getWidth()) - getBorderSize()) - 10.0f);
    }

    public final void setBorderColor(int i) {
        this.f3593h = i;
        this.f3603r.setColor(i);
        this.f3605t.setColor(i);
        this.f3606u.setColor(i);
        this.f3607v.setColor(i);
        invalidate();
    }

    public final void setBorderSize(float f) {
        this.f3591f = f;
        this.f3603r.setStrokeWidth(f);
        this.f3604s.setStrokeWidth(f);
        invalidate();
    }

    public final void setFillColor(int i) {
        this.f3594i = i;
        this.f3604s.setColor(i);
        invalidate();
    }

    public final void setMaxSpeed(int i) {
        this.f3590e = i;
        invalidate();
    }

    public final void setMetricText(String str) {
        this.f3596k = str;
        invalidate();
    }

    public final void setTextColor(int i) {
        this.f3595j = i;
        this.f3608w.setColor(i);
        this.f3609x.setColor(i);
        this.f3610y.setColor(i);
        this.f3611z.setColor(i);
        invalidate();
    }

    public final void setTextGap(float f) {
        this.f3592g = f;
        invalidate();
    }

    public final void setTimerText(String str) {
        this.f3597l = str;
        invalidate();
    }
}
