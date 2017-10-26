package com.yanxiu.gphone.faceshowadmin_android.net;

import com.test.yanxiu.network.RequestBase;

/**
 *
 * Created by frc on 17-10-26.
 */

public class FSABaseRequest extends RequestBase {
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return null;
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
