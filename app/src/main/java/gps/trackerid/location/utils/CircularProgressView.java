package gps.trackerid.location.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import gps.trackerid.location.R;

public class CircularProgressView extends View {

    public final Paint paint;

    public final Paint paint1;

    public final Paint paint2;

    public float aFloat;

    public String string;

    public final RectF rectF;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CircularProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.grey));
        Paint.Style style = Paint.Style.STROKE;
        paint.setStyle(style);
        paint.setStrokeWidth(20.0f);
        paint.setAntiAlias(true);
        this.paint = paint;
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.clrAccent));
        paint2.setStyle(style);
        paint2.setStrokeWidth(20.0f);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setAntiAlias(true);
        this.paint1 = paint2;
        Paint paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.black));
        paint3.setTextSize(context.getResources().getDisplayMetrics().density * 24.0f);
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setAntiAlias(true);
        this.paint2 = paint3;
        this.string = "00:00:00";
        this.rectF = new RectF();
//        paint3.setTypeface(m1813a);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = this.rectF;
        rectF.set(20.0f, 20.0f, getWidth() - 20.0f, getHeight() - 20.0f);
        canvas.drawArc(rectF, BitmapDescriptorFactory.HUE_RED, 360.0f, false, this.paint);
        canvas.drawArc(rectF, -90.0f, this.aFloat * 360, false, this.paint1);
        Paint paint = this.paint2;
        canvas.drawText(this.string, getWidth() / 2.0f, (paint.getTextSize() / 4) + (getHeight() / 2.0f), paint);
    }

    public final void setProgress(float f) {
        this.aFloat = f;
        invalidate();
    }

    public final void setTimerText(String text) {
        this.string = text;
        invalidate();
    }
}

