package com.ace.dawdler.json.generator;

/**
 * class help record he field name and type.
 *
 * @author aceding
 */
public class Attr {
    public String alias;//field alias.

    public String field;//field name.

    public String type;//field type.

    public Attr(String alias, String type, String field) {
        this.field = field;
        this.alias = alias;
        this.type = type;
    }

    public String getField() {
        return this.field;
    }

    public String getType() {
        return this.type;
    }

    public String getAlias() { return this.alias; }

    public void setField(String field) {
        this.field = field;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAlias(String alias) { this.alias = alias; }

}