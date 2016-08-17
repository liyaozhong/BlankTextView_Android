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

    //可编辑属性
    float lineSpacing;
    float leftPadding, rightPadding;
    float topMargin = 0;
    float blankWidth = 150;
    float textSize = 30;
    float blankCornerRadius = 5;
    int blankColor = Color.GRAY;
    int textColor = Color.BLACK;

    //内部属性
    private ArrayList<String> array = new ArrayList<String>();
    private ArrayList<BlankItem> blankRects = new ArrayList<BlankItem>();

    private float transX, transY;
    private ArrayList<TextHolder> textHolders = new ArrayList<TextHolder>();
    private ArrayList<RectF> blankHolders = new ArrayList<RectF>();

    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint.FontMetrics fm;

    public BlankTextView(Context context) {
        super(context);
    }

    public BlankTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BlankTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTextSize (float textSize){
        this.textSize = textSize;
        this.requestLayout();
        this.postInvalidate();
    }

    public void setup(){
        array.add("");
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
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.drawableState = getDrawableState();
        fm = textPaint.getFontMetrics();

        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = new BlankItem();
            blank.content = null;
            blankRects.add(blank);
        }
        this.requestLayout();
        this.postInvalidate();
    }

    private float getActualWidth(){
        return this.getWidth() - leftPadding - rightPadding;
    }

    private int getPreferHeight(float widthSize){
        widthSize -= leftPadding + rightPadding;
        float calWidth = widthSize;
        float calTop = 0;

        textHolders.clear();
        blankHolders.clear();

        float singleLineHeight = fm.descent - fm.ascent;
        for (int index = 0; index < array.size(); index++) {
            BlankItem blank = blankRects.get(index);
            RectF rectF = new RectF();
            if(blank.content == null){
                if (calWidth < blankWidth) {
                    if(calTop > topMargin || calWidth < widthSize){
                        calTop += singleLineHeight + lineSpacing;
                    }
                    calWidth = widthSize;
                }
                rectF = new RectF(widthSize - calWidth, calTop + fm.ascent,
                        widthSize - calWidth + blankWidth, calTop + fm.descent);
                calWidth -= blankWidth;
            }else{
                float width = StaticLayout.getDesiredWidth(blank.content, textPaint);
                if(calWidth < width){
                    if(calTop > topMargin || calWidth < widthSize){
                        calTop += singleLineHeight + lineSpacing;
                    }
                    calWidth = widthSize;
                    if(width > widthSize){
                        StaticLayout staticLayout = new StaticLayout(blank.content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                        float lastLine = 0;
                        for (int line = 0; line < staticLayout.getLineCount(); line++) {
                            textHolders.add(new TextHolder(blank.content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                                    staticLayout.getLineTop(line) + calTop));
                            lastLine = staticLayout.getLineTop(line);
                        }
                        rectF = new RectF(0, calTop + fm.ascent,
                                widthSize, calTop + fm.descent + lastLine);
                        calTop += staticLayout.getHeight();
                    }else {
                        rectF = new RectF(widthSize - calWidth, calTop + fm.ascent,
                                widthSize - calWidth + width, calTop + fm.descent);
                        calWidth -= width;
                        textHolders.add(new TextHolder(blank.content, rectF.left, calTop));
                    }
                }else{
                    rectF = new RectF(widthSize - calWidth, calTop + fm.ascent,
                            widthSize - calWidth + width, calTop + fm.descent);
                    calWidth -= width;
                    textHolders.add(new TextHolder(blank.content, rectF.left, calTop));
                }
            }
            blank.rectf = rectF;
            blankHolders.add(rectF);

            String content = array.get(index);
            StaticLayout staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
            int lineCount = staticLayout.getLineCount();
            if (lineCount == 1) {
                textHolders.add(new TextHolder(content, (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop));
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = widthSize;
                    calTop += staticLayout.getHeight();
                }
            } else if (lineCount > 1) {
                textHolders.add(new TextHolder(content.substring(staticLayout.getLineStart(0), staticLayout.getLineEnd(0)), (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop));
                calTop += singleLineHeight + lineSpacing;
                calWidth = widthSize;
                content = content.substring(staticLayout.getLineEnd(0));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                lineCount = staticLayout.getLineCount();
                for (int line = 0; line < lineCount - 1; line++) {
                    textHolders.add(new TextHolder(content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                            staticLayout.getLineTop(line) + calTop));
                    calTop += singleLineHeight + lineSpacing;
                }
                content = content.substring(staticLayout.getLineEnd(lineCount - 2));
                staticLayout = new StaticLayout(content, textPaint, (int) calWidth, Layout.Alignment.ALIGN_NORMAL, 1, lineSpacing, false);
                textHolders.add(new TextHolder(content, (this.getActualWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop));
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if (calWidth <= 0) {
                    calWidth = widthSize;
                    calTop += singleLineHeight + lineSpacing;
                }
            }
        }
        return (int)(calTop + singleLineHeight + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int desiredHeight = getPreferHeight(widthSize);

        int width = widthSize, height;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        linePaint.setColor(blankColor);
        textPaint.setColor(textColor);

        super.onDraw(canvas);
        transX = leftPadding;
        transY = topMargin - fm.ascent;
        canvas.translate(transX, transY);

        for(int i = 0 ; i < blankHolders.size(); i ++){
            canvas.drawRoundRect(blankHolders.get(i), blankCornerRadius, blankCornerRadius, linePaint);
        }
        for(int i = 0 ; i < textHolders.size(); i ++){
            TextHolder holder = textHolders.get(i);
            canvas.drawText(holder.text, holder.x, holder.y, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        final float x = event.getX() - transX;
        final float y = event.getY() - transY;
        for(int i = 0 ; i < blankRects.size(); i ++){
            BlankItem blank = this.blankRects.get(i);
            if(blank.rectf.contains(x, y)){
                if(blank.content == null){
                    blank.content = new Random().nextInt()%3 == 0 ? x+","+y+""+x : x+"";
                }else{
                    blank.content = null;
                }
                this.requestLayout();
                this.postInvalidate();
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
