package com.ace.dawdler.json.generator;

public class Attr {
    public String field;
    public String type;

    public Attr(String field, String type) {
        this.field = field;
        this.type = type;
    }

    public String getField() {
        return this.field;
    }

    public String getType() {
        return this.type;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setType(String type) {
        this.type = type;
    }
}