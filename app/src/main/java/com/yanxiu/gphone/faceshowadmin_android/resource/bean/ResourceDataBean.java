package com.yanxiu.gphone.faceshowadmin_android.resource.bean;

import com.yanxiu.gphone.faceshowadmin_android.model.*;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/11/8.
 * 资源
 */

public class ResourceDataBean extends BaseBean {

    private ArrayList<ResourceBean> elements = new ArrayList();
    private int pageSize;
    private int pageNum;
    private int offset;
    private int totalElements;
    private int lastPageNumber;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public ArrayList<ResourceBean> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ResourceBean> elements) {
        this.elements = elements;
    }
}
