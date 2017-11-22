package com.softisland.common.utils.bean;

/**
 * Created by liwx on 16/3/16.
 */
public class SoftCookie {

    public SoftCookie(){}

    public SoftCookie(String name,String value,String domain){
        this.domain = domain;
        this.name = name;
        this.value = value;
    }

    private String name;
    private String value;
    private String domain;
    private String path;

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
