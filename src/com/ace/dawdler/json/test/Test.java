package com.ace.dawdler.json.test;

public class Test {

    private Object obj;

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getObjHashCode() {
        return obj.hashCode();
    }

    public String getObjDesc() {
        if(null != obj) {
            return obj.toString();
        } else {
            return "";
        }
    }

    public void handleObj(Object obj) {
        System.out.println(obj.hashCode());
        if(null != obj) {
            System.out.println(obj.toString());
        }
    }
}
