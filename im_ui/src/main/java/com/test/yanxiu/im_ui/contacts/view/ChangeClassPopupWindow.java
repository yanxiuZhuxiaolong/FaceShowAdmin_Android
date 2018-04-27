package com.test.yanxiu.im_ui.contacts.view;

import com.test.yanxiu.im_ui.contacts.bean.ClassBean;

/**
 * Created by frc on 2018/3/13.
 */

public class ChangeClassPopupWindow {
    private OnClassChangedListener onClassChangedListener;


    public void addClassChangListener(OnClassChangedListener onClassChangedListener) {
        this.onClassChangedListener = onClassChangedListener;
    }

    interface OnClassChangedListener {
        void changed(ClassBean classData);
    }
}
