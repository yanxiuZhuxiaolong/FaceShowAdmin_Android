package com.yanxiu.gphone.faceshowadmin_android.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/19 10:53.
 * Function :解决键盘与scrollview滚动冲突问题
 */
public class ClassCircleTimeUtils {

    public interface onTimeUplistener{
        void onTimeUp();
    }

    private static ClassCircleTimeUtils mTimeUtils;
    private Timer mTimer=new Timer();
    private TimerTask mTask;

    public static ClassCircleTimeUtils creat(){
        if (mTimeUtils==null){
            mTimeUtils=new ClassCircleTimeUtils();
        }
        return mTimeUtils;
    }

    private ClassCircleTimeUtils(){

    }

    public void start(final onTimeUplistener uplistener){
        if (mTask==null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    uplistener.onTimeUp();
                    mTask.cancel();
                    mTask=null;
                }
            };
            mTimer.schedule(mTask, 100);
        }else {
            mTask.cancel();
            mTimer.purge();
            mTask=null;
            start(uplistener);
        }
    }
}
