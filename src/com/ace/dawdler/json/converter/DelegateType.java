package com.ace.dawdler.json.converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DelegateType<T> {

    private Type mType;

    public DelegateType() {
        mType = getParameterizedType();
    }

    private Type getParameterizedType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return parameterized.getActualTypeArguments()[0];
        } else {
            return null;
        }
    }

    public Type getType() {
        System.out.println(mType.getTypeName());
        return mType;
    }
}
