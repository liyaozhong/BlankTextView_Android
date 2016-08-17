package code.joshuali.widgettest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by joshuali on 16/8/10.
 */
public class BlankTextView extends View {


    private ArrayList<String> array = new ArrayList<String>();
    private ArrayList<BlankItem> blankRects = new ArrayList<BlankItem>();

    public BlankTextView(Context context) {
        super(context);
        this.setup();
    }

    public BlankTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setup();
    }

    public BlankTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.setup();
    }

    private void setup(){
        array.add("ehfuwh11");
        array.add("wefuhwieuf11");
        array.add("wefwef11");
        array.add("sfddd11");
        array.add("sfsf11");
        array.add("jtj11");
        array.add("svf11");
        array.add("tht11");
        array.add("22211");
        array.add("sdv11");
        array.add("dfwefwef11");
        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = new BlankItem();
            blank.content = null;
            blankRects.add(blank);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.drawableState = getDrawableState();
        float calWidth = this.getWidth();
        float topMargin = 100;
        float blankWidth = 150;
        float calTop = topMargin;
        StaticLayout singleLineLayout = new StaticLayout("A", textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        float singleLineHeight = singleLineLayout.getHeight();
        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = blankRects.get(index);
            RectF rectF = new RectF();
            if(blank.content == null){
                if (calWidth < blankWidth) {
                    if(calTop > topMargin || calWidth < this.getWidth()){
                        calTop += singleLineHeight;
                    }
                    calWidth = this.getWidth();
                }
                rectF = new RectF(this.getWidth() - calWidth, calTop - singleLineHeight/3*2, this.getWidth() - calWidth + blankWidth, calTop);
                calWidth -= blankWidth;
            }else{
                float width = StaticLayout.getDesiredWidth(blank.content, textPaint);
                if(calWidth < width){
                    if(calTop > topMargin || calWidth < this.getWidth()){
                        calTop += singleLineHeight;
                    }
                    calWidth = this.getWidth();
                    if(width > this.getWidth()){
                        StaticLayout staticLayout = new StaticLayout(blank.content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                        rectF = new RectF(0, calTop - singleLineHeight / 3 * 2, this.getWidth(), calTop - singleLineHeight + staticLayout.getHeight());
                        for (int line = 0; line < staticLayout.getLineCount(); line++) {
                            canvas.drawText(blank.content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                                    staticLayout.getLineTop(line) + calTop, textPaint);
                        }
                        calTop += staticLayout.getHeight();
                    }else {
                        rectF = new RectF(this.getWidth() - calWidth, calTop - singleLineHeight / 3 * 2, this.getWidth() - calWidth + width, calTop);
                        calWidth -= width;
                        canvas.drawText(blank.content, rectF.left, rectF.bottom, textPaint);
                    }
                }else{
                    rectF = new RectF(this.getWidth() - calWidth, calTop - singleLineHeight/3*2, this.getWidth() - calWidth + width, calTop);
                    calWidth -= width;
                    canvas.drawText(blank.content, rectF.left, rectF.bottom, textPaint);
                }
            }
            blank.rectf = rectF;
            canvas.drawRoundRect(rectF, 5.0f, 5.0f, linePaint);

            String content = array.get(index);
            StaticLayout staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            int lineCount = staticLayout.getLineCount();
            if (lineCount == 1) {
                canvas.drawText(content, (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = this.getWidth();
                    calTop += staticLayout.getHeight();
                }
            } else if (lineCount > 1) {
                canvas.drawText(content.substring(staticLayout.getLineStart(0), staticLayout.getLineEnd(0)), (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calTop += singleLineHeight;
                calWidth = this.getWidth();
                content = content.substring(staticLayout.getLineEnd(0));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                lineCount = staticLayout.getLineCount();
                for (int line = 0; line < lineCount - 1; line++) {
                    canvas.drawText(content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                            staticLayout.getLineTop(line) + calTop, textPaint);
                    calTop += singleLineHeight;
                }
                content = content.substring(staticLayout.getLineEnd(lineCount - 2));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                canvas.drawText(content, (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = this.getWidth();
                    calTop += singleLineHeight;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();
        for(int i = 0 ; i < blankRects.size(); i ++){
            BlankItem blank = this.blankRects.get(i);
            if(blank.rectf.contains(x, y)){
                if(blank.content == null){
                    blank.content = new Random().nextInt()%3 == 0 ? x+","+y+""+x : x+"";
                }else{
                    blank.content = null;
                }
                this.invalidate();
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    public class BlankItem{
        RectF rectf;
        String content;
    }

}
