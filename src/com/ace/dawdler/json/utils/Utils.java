package com.ace.dawdler.json.utils;

import com.ace.dawdler.json.utils.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Utils {

    public static String packageName2Path(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        return packageName.replace(".", File.separator);
    }

    public static String firstWord2UpperCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String firstWord = str.substring(0, 1);
        return firstWord.toUpperCase() + str.substring(1);
    }

    public static String firstWord2LowerCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String firstWord = str.substring(0, 1);
        return firstWord.toLowerCase() + str.substring(1);
    }

    public static String geObjectType(Object obj) {
        String result = null;
        if (null == obj) {
            return result;
        }
        if (obj instanceof Integer) {
            result = "Integer";
        } else if (obj instanceof Double) {
            result = "Double";
        } else if (obj instanceof String) {
            result = "String";
        } else if (obj instanceof Float) {
            result = "Float";
        } else if (obj instanceof Boolean) {
            result = "Boolean";
        } else if (obj instanceof JSONObject) {
            result = "JSONObject";
        } else if (obj instanceof JSONArray) {
            result = "JSONArray";
        }
        return result;
    }

    public static Class<?> getClass(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return cls;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getClassPackage(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return getClassPackage(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getClassPackage(Class<?> cls) {
        if (null == cls) {
            return null;
        }

        String name = cls.getPackage().getName();
        return name;
    }

    public static String getUsableClassName(String fileName, String className) {
        if (TextUtils.isEmpty(fileName)) {
            return firstWord2UpperCase(className);
        }
        String[] names = fileName.split("#");
        if (null == names || names.length == 0) {
            return firstWord2UpperCase(className);
        }

        for (String name : names) {
            if (name.equalsIgnoreCase(className)) {
                return getUsableClassName(fileName, className + "Bean");
            }
        }
        return firstWord2UpperCase(className);
    }
}
