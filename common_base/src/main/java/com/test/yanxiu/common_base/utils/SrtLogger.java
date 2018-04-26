package com.test.yanxiu.common_base.utils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by cailei on 13/03/2018.
 */

public class SrtLogger {
    public static void log(String tag, String message, Object... args) {
        if (tag == null) {
            tag = "srt";
        }

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag(tag)
                .build();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Logger.d(message, args);
    }
}
