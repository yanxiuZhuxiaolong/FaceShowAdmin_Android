package com.test.yanxiu.im_ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.test.yanxiu.common_base.utils.anim.TranslationYAnimUtil;
import com.test.yanxiu.im_ui.R;


/**
 * Created by Canghaixiao.
 * Time : 2017/8/3 16:44.
 * Function :
 */
public class ChoosePicsDialog extends Dialog implements View.OnClickListener {

    private View mAnimView;
    private Context mContext;
    private View mAnimBgview;

    public interface OnViewClickListener{
        void onAlbumClick();
        void onCameraClick();
    }

    private OnViewClickListener mOnViewClickListener;

    public ChoosePicsDialog(@NonNull Context context) {
        super(context, R.style.ChoosePicDialog);
        setOwnerActivity((Activity) context);
        init(context);
    }

    private void init(Context context){
        mContext=context;
        View rootView= LayoutInflater.from(context).inflate(R.layout.im_ui_dialog_choose_pics,null);
        setContentView(rootView);
        rootView.findViewById(R.id.im_ui_ll_album).setOnClickListener(this);
        rootView.findViewById(R.id.im_ui_ll_camera).setOnClickListener(this);
        rootView.findViewById(R.id.im_ui_tv_cancle).setOnClickListener(this);
        rootView.findViewById(R.id.im_ui_gradation).setOnClickListener(this);

        mAnimBgview=rootView.findViewById(R.id.im_ui_gradation);
        mAnimView=rootView.findViewById(R.id.ll_anim);
    }

    @Override
    public void show() {
        super.show();
        TranslationYAnimUtil.getInstence().setAnimViewHeight(mContext,R.dimen.dialog_choose_pics).setBgGradation(mAnimBgview,0f,0.6f).setStartAnim(mAnimView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        setDismiss();
    }

    private void setDismiss(){
        TranslationYAnimUtil.getInstence().setAnimViewHeight(mContext,R.dimen.dialog_choose_pics).setBgGradation(mAnimBgview,0.6f,0f).setCloseAnim(mAnimView, new TranslationYAnimUtil.onCloseFinishedListener() {
            @Override
            public void onFinished() {
                dismiss();
            }
        });
    }

    public void setClickListener(OnViewClickListener onViewClickListener){
        this.mOnViewClickListener=onViewClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.im_ui_ll_album) {
            if (mOnViewClickListener != null) {
                mOnViewClickListener.onAlbumClick();
            }
            setDismiss();

        } else if (i == R.id.im_ui_ll_camera) {
            if (mOnViewClickListener != null) {
                mOnViewClickListener.onCameraClick();
            }
            setDismiss();

        } else if (i == R.id.im_ui_gradation || i == R.id.im_ui_tv_cancle) {
            setDismiss();

        }
    }
}
