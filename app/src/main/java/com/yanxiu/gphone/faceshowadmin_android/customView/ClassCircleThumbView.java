package com.yanxiu.gphone.faceshowadmin_android.customView;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.classCircle.response.ClassCircleResponse;

import java.util.ArrayList;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleThumbView extends android.support.v7.widget.AppCompatTextView {

    private Context mContext;

    public ClassCircleThumbView(Context context) {
        this(context,null);
    }

    public ClassCircleThumbView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassCircleThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext=context;
    }

    public void setData(ArrayList<ClassCircleResponse.Data.Moments.Likes> list){
        if (list == null || list.size() <= 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setImageSpan());
        for (int i = 0; i < list.size(); i++) {
            ClassCircleResponse.Data.Moments.Likes item = list.get(i);
            if (item.publisher != null&& !TextUtils.isEmpty(item.publisher.realName)) {
                builder.append(setClickableSpan(item.publisher.realName, item));
                if (i != list.size() - 1) {
                    builder.append(" , ");
                } else {
                    builder.append(" ");
                }
            }
        }
        setText(builder, BufferType.SPANNABLE);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public SpannableString setClickableSpan(final String item, final ClassCircleResponse.Data.Moments.Likes bean) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(mContext, bean.publisher.realName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#333333"));
                ds.setUnderlineText(false);
            }
        };

        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new ImageSpan(getContext(), R.drawable.classcircle_like, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

}
