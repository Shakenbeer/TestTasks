package com.shakenbeer.wolttest.model;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Hour {

    public static final String OPEN = "open";
    public static final String CLOSE = "close";

    @Type
    private String type;

    private long value;

    public boolean isOpen() {
        return type.equals(OPEN);
    }

    public boolean isClose() {
        return type.equals(CLOSE);
    }

    public boolean isType(@Type String type) {
        return this.type.equals(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @StringDef({OPEN, CLOSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }
}
