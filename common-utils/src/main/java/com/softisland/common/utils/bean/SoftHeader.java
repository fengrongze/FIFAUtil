package com.softisland.common.utils.bean;

/**
 * Created by liwx on 16/3/18.
 */
public class SoftHeader {

    public SoftHeader(){}

    public SoftHeader(String name,String value){
        this.name = name;
        this.value = value;
    }

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
