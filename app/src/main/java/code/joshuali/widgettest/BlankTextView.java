package code.joshuali.widgettest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by joshuali on 16/8/10.
 */
public class BlankTextView extends View{


    public BlankTextView(Context context) {
        super(context);
    }

    public BlankTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BlankTextView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<String> array = new ArrayList<String>();
        array.add("ehfuwh11");
        array.add("wefuhwieuf11");
        array.add("wefwef11");
        array.add("sf11");
        array.add("sfsf11");
        array.add("jtj11");
        array.add("svf11");
        array.add("tht11");
        array.add("22211");
        array.add("sdv11");
        array.add("dfwefwef11");


        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(40);
        textPaint.drawableState = getDrawableState();
        float calWidth = this.getWidth();
        float calTop = 100;
        for(int index = 0; index < array.size(); index ++){
            String content = array.get(index);
            StaticLayout staticLayout = new StaticLayout(content, textPaint, (int)calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            int lineCount = staticLayout.getLineCount();
            if(lineCount == 1){
                canvas.drawText(content, (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if(calWidth <= 0){
                    calWidth = this.getWidth();
                    calTop += staticLayout.getHeight();
                }
            }else if(lineCount > 1){
                float singleLineHeight = staticLayout.getHeight() / lineCount;
                canvas.drawText(content.substring(staticLayout.getLineStart(0), staticLayout.getLineEnd(0)), (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calTop += singleLineHeight;
                calWidth = this.getWidth();
                content = content.substring(staticLayout.getLineEnd(0));
                staticLayout = new StaticLayout(content, textPaint, (int)calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                lineCount = staticLayout.getLineCount();
                for(int line = 0; line < lineCount - 1; line ++){
                    canvas.drawText(content.substring(staticLayout.getLineStart(line), staticLayout.getLineEnd(line)), 0,
                            staticLayout.getLineTop(line) + calTop, textPaint);
                    calTop += singleLineHeight;
                }
                content = content.substring(staticLayout.getLineEnd(lineCount - 2));
                staticLayout = new StaticLayout(content, textPaint, (int)calWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                canvas.drawText(content, (this.getWidth() - calWidth),
                        staticLayout.getLineTop(0) + calTop, textPaint);
                calWidth -= StaticLayout.getDesiredWidth(content, textPaint);
                if(calWidth <= 0){
                    calWidth = this.getWidth();
                    calTop += singleLineHeight;
                }
            }
        }
    }

}
