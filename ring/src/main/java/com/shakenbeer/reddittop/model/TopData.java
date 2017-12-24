package com.shakenbeer.reddittop.model;


import java.util.List;

public class TopData {

    private List<Child> children;

    private String after;

    public List<Child> getChildren() {
        return children;
    }

    public String getAfter() {
        return after;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
