package <Your Namespace Here>;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;

/**
 * The MIT-Zero License
*
* Copyright (c) 2015 Erik Buttram
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
 */
public class BadgeFactory {

    private static final int BADGE_RADIUS = 30;

    private enum BadgeType {
        Circular, //for single digit badges
        Cylinder //multi digit badges
    }

    private Context context;
    private BadgeType type;
    private int badgeColor;
    /**
     * following four rects are for multi counts only,
     */
    private RectF totalRect;
    private RectF textRect; //used to draw the rectangle containing the text
    private RectF leftRect; //used to draw the rectangle for the left arc for drawArc call
    private RectF rightRect; //ditto for the right arc


    /**
     * Count displayed by the badge, casted to string if it isn't already
     */
    private String count;

    private Paint paint;
    private Rect textBounds;
    /**
     * Instantiates the badge factory, this class is mostly useful for use in collection
     * adapters such as RecyclerView Adapters or another Base Adapters of some sort
     * @param context context of the current application (note: doesn't actually need to be Application Context)
     */
    public BadgeFactory(Context context) {
        this.context = context;
        this.totalRect = new RectF();
        this.textRect = new RectF();
        this.leftRect = new RectF();
        this.rightRect = new RectF();
        this.textBounds = new Rect();
        this.count = "";
        this.badgeColor = -1;
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                context.getResources().getDisplayMetrics());
        this.paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }

    /**
     * Instantiates the badge factory, this class is mostly useful for use in collection
     * adapters such as RecyclerView Adapters or another Base Adapters of some sort
     * @param context context of the current application (note: doesn't actually need to be Application Context)
     * @param color sets a default color using a resource ID
     */
    public BadgeFactory(Context context, int color) {
        this(context);
        this.badgeColor = context.getResources().getColor(color);
    }

    /**
     * Similar to {@link #setCount(int)} except with the string representation being passed in.
     * @param count
     */
    public void setCount(String count) {
        this.count = count;
        setUp();
    }


    /**
     * sets the string and initializes the dimensions needed to render the badge
     * @param count the int representation of the count to be displayed in the badge
     */
    public void setCount(int count) {
        this.count = String.valueOf(count);
        setUp();
    }

    /**
     * Sets the text size to be used for the badge numbers
     * @param textSize int in space pixels
     */
    public void setTextSize(int textSize) {
        float newSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                context.getResources().getDisplayMetrics());
        paint.setTextSize(newSize);
        if (!TextUtils.isEmpty(count)) {
            this.paint.getTextBounds(this.count, 0, count.length(), this.textBounds);
        }
    }

    /**
     * retrieves a color based on Resource ID
     * @param color the resource id of the color to be used
     */
    public void setBadgeColor(int color) {
        this.badgeColor = context.getResources().getColor(color);
    }

    /**
     * Utilizes {@link Color#parseColor(String)} to set the badge color
     * @param color
     * @throws IllegalArgumentException
     */
    public void setBadgeColor(String color) throws IllegalArgumentException {
        this.badgeColor = Color.parseColor(color);
    }

    //sets up the rectangle(s) to be used for drawing
    private void setUp() {
        int len = count.length();
        this.paint.getTextBounds(this.count, 0, count.length(), this.textBounds);
        this.type = len > 1 ? BadgeType.Cylinder : BadgeType.Circular;

        final float totalW = textBounds.width();
        final float totalH = paint.getTextSize() * 1.25f;//Magic number is to add vertical padding

        if (type != BadgeType.Circular) {
            totalRect.set(0, 0, totalW + (BADGE_RADIUS * 2), totalH);
            textRect.set(BADGE_RADIUS / 2, 0, totalRect.right - (BADGE_RADIUS/2), totalRect.bottom);
            leftRect.set(0, 0, BADGE_RADIUS, totalRect.bottom);
            rightRect.set(textRect.right - BADGE_RADIUS/2, 0, totalRect.right, totalRect.bottom);
        } else {
            totalRect.set(0, 0, totalH, totalH);
        }
    }

    public Bitmap build() {

        Bitmap output = Bitmap.createBitmap((int)totalRect.width(), (int)totalRect.bottom, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        paint.setColor(badgeColor);

        if (type != BadgeType.Circular) {
            float textY = textRect.height() * .8f;
            canvas.drawRect(textRect, paint);
            canvas.drawArc(leftRect, 90, 180, true, paint);
            canvas.drawArc(rightRect, 270, 180, true, paint);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText(count, textRect.centerX(), textY, paint);
        } else {
            final float square = Math.min(textRect.width(), textRect.height());
            final float centerX = totalRect.width() / 2 + 0.7f;
            final float centerY = totalRect.height() / 2 + 0.7f;
            canvas.drawCircle(centerX, centerY, square / 2 + 0.1f, paint);
            canvas.drawText(count, centerX, centerY, paint);
        }

        return output;
    }

    public Bitmap build(String count) {
        setCount(count);
        return build();
    }

    public Bitmap build(int count) {
        setCount(count);
        return build();
    }
}
