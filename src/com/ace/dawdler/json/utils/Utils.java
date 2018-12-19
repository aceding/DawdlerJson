package com.ace.dawdler.json.utils;

import java.io.File;

/**
 * util class for always use.
 *
 * @author aceding
 */
public class Utils {

    /**
     * for example: convert com.ace.dawdler to com/ace/dawdler.
     *
     * @param packageName
     * @return
     */
    public static String packageName2Path(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        return packageName.replace(".", File.separator);
    }

    /**
     * convert the String class name to Class object.
     *
     * @param className
     * @return
     */
    public static Class<?> getClass(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return cls;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * check the object is primitive type or not.
     *
     * @param o
     * @return
     */
    public static boolean isPrimitiveType(Object o) {
        return o instanceof Integer
                || o instanceof String
                || o instanceof Boolean
                || o instanceof Double
                || o instanceof Float
                || o instanceof Long
                || o instanceof Byte
                || o instanceof Character
                || o instanceof Short;
    }
}
