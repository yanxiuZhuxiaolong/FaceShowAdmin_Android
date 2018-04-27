package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

public class ImResponseBase {
    public int code;
    public String message;

    public Object currentUser;
    public Error error;

    public class Error{
        public int code;
        public String title;
        public String message;
    }
}
