package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshowadmin_android.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 14:11.
 * Function :
 */
public class MaxLineTextLayout extends LinearLayout {

    public interface onLinesChangedListener{
        void onLinesChanged(boolean isShowAll);
    }

    private static final int LINES=4;
    private static final int CONTENT_SPACE_LINE_HEIGHT=9;
    private static final int CONTENT_TEXT_SIZE=42;
    private static final int MORE_TEXT_SIZE=28;
    private static final int CONTENT_TEXT_COLOR= Color.parseColor("#333333");
    private static final int MORE_TEXT_COLOR= Color.parseColor("#0068bd");
    private static final int MORE_MARGIN_TOP=30;

    private MaxLineTextView mContentView;
    private TextView mMoreView;

    private int mMaxLines=LINES;
    private int mContentSpaceLinesHeight=CONTENT_SPACE_LINE_HEIGHT;
    private int mMoreMarginTop=MORE_MARGIN_TOP;
    private int mMoreTextColor=MORE_TEXT_COLOR;
    private int mContentTextColor=CONTENT_TEXT_COLOR;
    private int mMoreTextSize=MORE_TEXT_SIZE;
    private int mContentTextSize=CONTENT_TEXT_SIZE;

    private onLinesChangedListener mLinesChangedListener;
    private boolean isShowAll=false;

    public MaxLineTextLayout(Context context) {
        this(context,null);
    }

    public MaxLineTextLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaxLineTextLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_max_line,this);

        mContentView= (MaxLineTextView) findViewById(R.id.max_line_tv_content);
        mMoreView= (TextView) findViewById(R.id.max_line_tv_more);
        if (attrs!=null){
            TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.MaxLineTextLayout);
            mContentTextSize=array.getDimensionPixelSize(R.styleable.MaxLineTextLayout_contentTextSize,CONTENT_TEXT_SIZE);
            mMoreTextSize=array.getDimensionPixelSize(R.styleable.MaxLineTextLayout_moreTextSize,MORE_TEXT_SIZE);
            mContentTextColor=array.getColor(R.styleable.MaxLineTextLayout_contentTextColor,CONTENT_TEXT_COLOR);
            mMoreTextColor=array.getColor(R.styleable.MaxLineTextLayout_moreTextColor,MORE_TEXT_COLOR);
            mMoreMarginTop=array.getDimensionPixelSize(R.styleable.MaxLineTextLayout_moreTextMaginTop,MORE_MARGIN_TOP);
            mContentSpaceLinesHeight=array.getDimensionPixelSize(R.styleable.MaxLineTextLayout_contentSpaceLineHeight,CONTENT_SPACE_LINE_HEIGHT);
            mMaxLines=array.getInteger(R.styleable.MaxLineTextLayout_maxLines,LINES);
            array.recycle();
        }

        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContentTextSize);
        mContentView.setTextColor(mContentTextColor);
        mContentView.setLineSpacing(mContentSpaceLinesHeight,1f);
        mContentView.setMaxLines(mMaxLines);
        mMoreView.setTextColor(mMoreTextColor);
        mMoreView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mMoreTextSize);
        int left=mMoreView.getPaddingLeft();
        int bottom=mMoreView.getPaddingBottom();
        mMoreView.setPadding(left,mMoreMarginTop,0,bottom);
        mMoreView.setVisibility(GONE);

        mContentView.setOnLinesChangedListener(new MaxLineTextView.onLinesChangedListener() {
            @Override
            public void onLinesChanged(int lines) {
                if (lines>4){
                    mMoreView.setVisibility(VISIBLE);
                }else {
                    mMoreView.setVisibility(GONE);
                }
            }
        });

        mMoreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowAll){
                    isShowAll=false;
                    mContentView.setMaxLines(mMaxLines);
                    mMoreView.setText(R.string.text_all);
                }else {
                    isShowAll=true;
                    mContentView.setMaxLines(Integer.MAX_VALUE);
                    mMoreView.setText(R.string.text_line);
                }
                if (mLinesChangedListener!=null){
                    mLinesChangedListener.onLinesChanged(isShowAll);
                }
            }
        });
    }

    public void setData(String text){
        setData(text,false);
    }

    public void setData(String text, boolean isShowAll){
        setData(text,isShowAll,null);
    }

    public void setData(String text, boolean isShowAll, onLinesChangedListener linesChangedListener){
        this.isShowAll=isShowAll;
        this.mLinesChangedListener=linesChangedListener;
        mContentView.setText(text);
        setting();
    }

    private void setting(){
        if (isShowAll){
            mContentView.setMaxLines(Integer.MAX_VALUE);
            mMoreView.setText(R.string.text_line);
        }else {
            mContentView.setMaxLines(LINES);
            mMoreView.setText(R.string.text_all);
        }
    }
}
