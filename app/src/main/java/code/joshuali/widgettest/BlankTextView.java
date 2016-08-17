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

    class TextHolder{
        public String text;
        public float x, y;
        public TextHolder(String text, float x, float y){
            super();
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }


    private ArrayList<String> array = new ArrayList<String>();
    private ArrayList<BlankItem> blankRects = new ArrayList<BlankItem>();

    private float lineSpacing;
    private float leftPadding, rightPadding;
    float topMargin = 100;
    float blankWidth = 150;

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
        lineSpacing = 20;
        leftPadding = 20;
        rightPadding = 20;
        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = new BlankItem();
            blank.content = null;
            blankRects.add(blank);
        }
    }

    private float getActualWidth(){
        return this.getWidth() - leftPadding - rightPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.drawableState = getDrawableState();
        float calWidth = this.getActualWidth();

        float calTop = topMargin;
        Paint.FontMetrics fm = textPaint.getFontMetrics();

        float singleLineHeight = fm.descent - fm.ascent;
        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = blankRects.get(index);
            RectF rectF = new RectF();
            ArrayList<TextHolder> textHolders = new ArrayList<TextHolder>();
            if(blank.content == null){
                if (calWidth < blankWidth) {
                    if(calTop > topMargin || calWidth < this.getActualWidth()){
                        calTop += singleLineHeight + lineSpacing;
                    }
                    calWidth = this.getActualWidth();
                }
                rectF = new RectF(this.getActualWidth() - calWidth, calTop + fm.ascent,
                        this.getActualWidth() - calWidth + blankWidth, calTop + fm.descent);
                calWidth -= blankWidth;
            }else{
                float width = StaticLayout.getDesiredWidth(blank.content, textPaint);
                if(calWidth < width){
                    if(calTop > topMargin || calWidth < this.getActualWidth()){
                        calTop += singleLineHeight + lineSpacing;
                    }
                    calWidth = this.getActualWidth();
                    if(width > this.getActualWidth()){
                        StaticLayout staticLayout = new StaticLayout(blank.content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                        float lastLine = 0;
                        for (int line = 0; line < staticLayout.getLineCount(); line++) {
                            textHolders.add(new TextHolder(blank.content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                                    staticLayout.getLineTop(line) + calTop));
                            lastLine = staticLayout.getLineTop(line);
                        }
                        rectF = new RectF(0, calTop + fm.ascent,
                                this.getActualWidth(), calTop + fm.descent + lastLine);
                        calTop += staticLayout.getHeight();
                    }else {
                        rectF = new RectF(this.getActualWidth() - calWidth, calTop + fm.ascent,
                                this.getActualWidth() - calWidth + width, calTop + fm.descent);
                        calWidth -= width;
                        textHolders.add(new TextHolder(blank.content, rectF.left, calTop));
                    }
                }else{
                    rectF = new RectF(this.getActualWidth() - calWidth, calTop + fm.ascent,
                            this.getActualWidth() - calWidth + width, calTop + fm.descent);
                    calWidth -= width;
                    textHolders.add(new TextHolder(blank.content, rectF.left, calTop));
                }
            }
            blank.rectf = rectF;
            canvas.drawRoundRect(rectF, 5.0f, 5.0f, linePaint);
            for(int i = 0 ; i < textHolders.size(); i ++){
                TextHolder holder = textHolders.get(i);
                canvas.drawText(holder.text, holder.x, holder.y, textPaint);
            }

            String content = array.get(index);
            StaticLayout staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
            int lineCount = staticLayout.getLineCount();
            if (lineCount == 1) {
                canvas.drawText(content, (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = this.getActualWidth();
                    calTop += staticLayout.getHeight();
                }
            } else if (lineCount > 1) {
                canvas.drawText(content.substring(staticLayout.getLineStart(0), staticLayout.getLineEnd(0)), (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calTop += singleLineHeight + lineSpacing;
                calWidth = this.getActualWidth();
                content = content.substring(staticLayout.getLineEnd(0));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                lineCount = staticLayout.getLineCount();
                for (int line = 0; line < lineCount - 1; line++) {
                    canvas.drawText(content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                            staticLayout.getLineTop(line) + calTop, textPaint);
                    calTop += singleLineHeight + lineSpacing;
                }
                content = content.substring(staticLayout.getLineEnd(lineCount - 2));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                canvas.drawText(content, (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = this.getActualWidth();
                    calTop += singleLineHeight + lineSpacing;
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
