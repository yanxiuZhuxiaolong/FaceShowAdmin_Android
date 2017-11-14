package com.yanxiu.gphone.faceshowadmin_android.resource.bean;

import com.yanxiu.gphone.faceshowadmin_android.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/11/8.
 * 资源
 */

public class ResourceDataBean extends BaseBean {

    private Resources resources;

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public class Resources{

        private String callbackParam;
        private String callbackValue;
        private List<Callbacks> callbacks;

        public class Callbacks{
            private String callbackParam;
            private String callbackValue;

            public String getCallbackParam() {
                return callbackParam;
            }

            public void setCallbackParam(String callbackParam) {
                this.callbackParam = callbackParam;
            }

            public String getCallbackValue() {
                return callbackValue;
            }

            public void setCallbackValue(String callbackValue) {
                this.callbackValue = callbackValue;
            }
        }

        public String getCallbackParam() {
            return callbackParam;
        }

        public void setCallbackParam(String callbackParam) {
            this.callbackParam = callbackParam;
        }

        public String getCallbackValue() {
            return callbackValue;
        }

        public void setCallbackValue(String callbackValue) {
            this.callbackValue = callbackValue;
        }

        public List<Callbacks> getCallbacks() {
            return callbacks;
        }

        public void setCallbacks(List<Callbacks> callbacks) {
            this.callbacks = callbacks;
        }

        private ArrayList<ResourceBean> elements = new ArrayList();
        private int totalElements;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public ArrayList<ResourceBean> getElements() {
            return elements;
        }

        public void setElements(ArrayList<ResourceBean> elements) {
            this.elements = elements;
        }
    }

}
