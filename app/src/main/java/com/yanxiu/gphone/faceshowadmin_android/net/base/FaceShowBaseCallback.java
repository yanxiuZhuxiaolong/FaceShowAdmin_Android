package com.yanxiu.gphone.faceshowadmin_android.net.base;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;


/**
 * 请求回调基类
 * 目前不清楚是否像易学易练一样，需要登录token失效统一处理。
 */

public abstract class FaceShowBaseCallback<T extends FaceShowBaseResponse> implements HttpCallback<T> {
    @Override
    public void onSuccess(RequestBase request, T ret) {
//        //code =99 表示token失效
//        if(ret != null && ret.getStatus().getCode() == Constants.NOT_LOGGED_IN){
//            PushManager.getInstance().unBindAlias(YanxiuApplication.getContext(), String.valueOf(LoginInfo.getUID()), true);
//            LoginInfo.LogOut();
//            DataSupport.deleteAll(AnswerBean.class);
//            ActivityManger.LogOut("2");
//            Context context = YanxiuApplication.getContext();
//            Intent intent=new Intent(context,LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }else {
        onResponse(request, ret);
//        }
    }

    protected abstract void onResponse(RequestBase request, T response);
}
