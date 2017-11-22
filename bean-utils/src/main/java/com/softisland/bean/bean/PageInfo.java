package com.softisland.bean.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by liwx on 2015/5/6.
 */
public class PageInfo implements Serializable{

    public PageInfo(List<Map<String,Object>> list, int total){
        this.list = list;
        this.total = total;
    }

    private List<Map<String,Object>> list;
    private int total;

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
