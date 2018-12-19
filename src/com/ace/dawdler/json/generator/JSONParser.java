package com.ace.dawdler.json.generator;

import java.util.*;

import com.ace.dawdler.json.utils.LogUtils;
import com.ace.dawdler.json.utils.TextUtils;
import com.ace.dawdler.json.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ace.dawdler.json.utils.TextUtils.firstWord2UpperCase;

/**
 * Json文本解析类，解析Json文本为JavaBean。
 *
 * @author aceding
 */
public class JSONParser {

    public static final String PACKAGE_NAME = "com.ace.dawdler.json.gen";

    public static final String CLASS_NAME = "JavaBean";

    public static void parseJSONStr(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            parseJSONObject(jsonObject);
        } catch (Exception e) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                parseJSONArray(jsonArray);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void parseJSONObject(JSONObject jsonObj) {
        if (null == jsonObj) {
            return;
        }

        LinkedHashMap<String, Map<String, Attr>> classMap = new LinkedHashMap<>();
        parseJSONObject(CLASS_NAME, jsonObj, classMap);
        CodeGenerator.genJavaBeans(classMap);
    }

    public static void parseJSONArray(JSONArray jsonArray) {
        if (null == jsonArray) {
            return;
        }

        LinkedHashMap<String, Map<String, Attr>> classMap = new LinkedHashMap<>();
        parseJSONArray(CLASS_NAME, CLASS_NAME, jsonArray, classMap);
        CodeGenerator.genJavaBeans(classMap);
    }

    public static void parseJSONObject(String fileName, JSONObject jsonObj, Map<String, Map<String, Attr>> classMap) {
        if (TextUtils.isEmpty(fileName) || null == jsonObj) {
            return;
        }

        fileName = firstWord2UpperCase(fileName);
        Map<String, Attr> attrMap = null;
        if (classMap.containsKey(fileName)) {
            attrMap = classMap.get(fileName);
        } else {
            attrMap = new HashMap<String, Attr>();
            classMap.put(fileName, attrMap);
        }

        Iterator<String> keys = jsonObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (attrMap.containsKey(key)) {
                continue;
            }
            Object obj = jsonObj.opt(key);
            if (null == obj) {
                continue;
            }

            System.out.println("key is: " + key);
            Attr attr = null;
            if (obj instanceof Integer) {
                attr = new Attr(key, "int");
            } else if (obj instanceof Long) {
                attr = new Attr(key, "long");
            } else if (obj instanceof Boolean) {
                attr = new Attr(key, "boolean");
            } else if (obj instanceof Double || obj instanceof Float) {
                attr = new Attr(key, "double");
            } else if (obj instanceof String) {
                attr = new Attr(key, "String");
            } else if (obj instanceof JSONObject) {
                String usableClsName = getUsableClassName(fileName, key);
                parseJSONObject(fileName + "#" + usableClsName, (JSONObject) obj, classMap);
                attr = new Attr(key, usableClsName);
            } else if (obj instanceof JSONArray) {
                String usableClsName = getUsableClassName(fileName, key);
                String type = parseJSONArray(usableClsName, fileName + "#" + usableClsName, (JSONArray) obj, classMap);
                attr = new Attr(key, "List<" + type + ">");
            } else {
                attr = new Attr(key, "Object");
            }

            attrMap.put(key, attr);
        }
    }

    public static String parseJSONArray(String className, String fileName, final JSONArray jsonArray, Map<String, Map<String, Attr>> classMap) {
        //异常逻辑处理。
        if (TextUtils.isEmpty(className) || null == jsonArray || jsonArray.length() == 0) {
            return null;
        }
        //首先清除jsonArray中为空的value
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            if (null == jsonArray.opt(i)) {
                jsonArray.remove(i);
            }
        }

        className = firstWord2UpperCase(className);
        String result = null;

        boolean allValueIsOneType = true;
        for (int i = 1, n = jsonArray.length(); i < n; i++) {
            if (jsonArray.opt(i).getClass() != jsonArray.opt(i - 1).getClass()) {
                allValueIsOneType = false;
                break;
            }
        }

        if (!allValueIsOneType) {
            //如果JSONArray的内容不是同一种类型，认为是不合理的设计，不进行处理。
            LogUtils.printLog("JsonArray's value not one type, not good design, return.");
            return result;

        }

        Class valueType = jsonArray.opt(0).getClass();
        if (valueType == JSONObject.class) {
            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                parseJSONObject(fileName, (JSONObject) jsonArray.opt(i), classMap);
            }
            result = className;
        } else if (valueType == JSONArray.class) {
            String tmp = parseJSONArray(className + "$Bean", fileName + "$Bean", jsonArray.optJSONArray(0), classMap);
            if (tmp.equals(className + "$Bean")) {
                for (int i = 1, n = jsonArray.length(); i < n; i++) {
                    parseJSONArray(className + "$Bean", fileName + "$Bean", jsonArray.optJSONArray(i), classMap);
                }
            }
            result = "List<" + tmp + ">";
        } else {
            //如果JSONArrayList的内容全部是基础类型,则不需要创建新的类。
            result = valueType.getSimpleName();
        }

        return result;
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
