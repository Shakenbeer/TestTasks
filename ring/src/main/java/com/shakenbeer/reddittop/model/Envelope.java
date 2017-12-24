package com.shakenbeer.reddittop.model;


import java.util.List;

public class Envelope {

    private boolean notify;

    private String message;

    private List<ChildData> children;

    public Envelope(boolean notify, String message, List<ChildData> children) {
        this.notify = notify;
        this.message = message;
        this.children = children;
    }

    public Envelope(boolean notify, List<ChildData> children) {
        this.notify = notify;
        this.children = children;
    }

    public boolean isNotify() {
        return notify;
    }

    public String getMessage() {
        return message;
    }

    public List<ChildData> getChildren() {
        return children;
    }
}
