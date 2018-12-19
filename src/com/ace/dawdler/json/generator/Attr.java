package com.ace.dawdler.json.generator;

/**
 * class help record he field name and type.
 *
 * @author aceding
 */
public class Attr {

    public String field;//field name.

    public String type;//field type.

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