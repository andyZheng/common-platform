package com.datanucleus.samples.entity;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(embeddedOnly = "true")
public class Remarks {

    String name;

    String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Remarks: { name : " + name + ", content : " + content + "}";
    }
}
