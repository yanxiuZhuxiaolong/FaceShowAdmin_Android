package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 14:13.
 * Function :
 */
public class MaxLineTextView extends android.support.v7.widget.AppCompatTextView {

    public interface onLinesChangedListener{
        void onLinesChanged(int lines);
    }

    private String mText;
    private int mLines;
    private onLinesChangedListener mLinesChangedListener;

    public MaxLineTextView(Context context) {
        super(context);
    }

    public MaxLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        checkShouldLineNum(getPaint());
        super.onDraw(canvas);
    }

    public void setOnLinesChangedListener(onLinesChangedListener linesChangedListener){
        this.mLinesChangedListener=linesChangedListener;
    }

    private void checkShouldLineNum(Paint paint){
        String txt=mText;
        int lines = 0;
        while (!TextUtils.isEmpty(txt)) {
            int count = paint.breakText(txt, true, getWidth(), null);
            if (txt.length()>count) {
                lines++;
                txt=txt.substring(count,txt.length());
            }else {
                txt=null;
            }
        }
        if (mLinesChangedListener!=null&&mLines!=lines){
            mLinesChangedListener.onLinesChanged(lines);
        }
        mLines=lines;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        this.mText=text.toString();
        super.setText(text, type);
    }
}
