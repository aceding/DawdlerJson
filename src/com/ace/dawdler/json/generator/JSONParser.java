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
 * parse the json object, and make the attrs tree.
 *
 * @author aceding
 */
public class JSONParser {

    /**
     * parse the json string and build the attrs tree, then generator the .java file.
     *
     * @param packageName
     * @param className
     * @param jsonStr
     */
    public static void parseJSONStr(String packageName, String className, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            parseJSONObject(packageName, className, jsonObject);
        } catch (Exception e) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                parseJSONArray(packageName, className, jsonArray);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * parse the json object and make the attrs tree, then generator the .java file.
     *
     * @param packageName
     * @param className
     * @param jsonObj
     */
    public static void parseJSONObject(String packageName, String className, JSONObject jsonObj) {
        if (null == jsonObj) {
            return;
        }

        LinkedHashMap<String, Map<String, Attr>> classMap = new LinkedHashMap<>();
        parseJSONObject(className, jsonObj, classMap);
        CodeGenerator.genJavaBeans(packageName, classMap);
    }

    /**
     * parse the json array and make the attrs tree, and then generator the .java file.
     *
     * @param packageName
     * @param className
     * @param jsonArray
     */
    public static void parseJSONArray(String packageName, String className, JSONArray jsonArray) {
        if (null == jsonArray) {
            return;
        }

        LinkedHashMap<String, Map<String, Attr>> classMap = new LinkedHashMap<>();
        parseJSONArray(className, className, jsonArray, classMap);
        CodeGenerator.genJavaBeans(packageName, classMap);
    }

    /**
     * parse the json object and get the attrs, then to fill in the attrs tree.
     *
     * @param fileName
     * @param jsonObj
     * @param classMap
     */
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

    /**
     * parse the json array object and get the attrs, then to fill in the attrs tree.
     *
     * @param className
     * @param fileName
     * @param jsonArray
     * @param classMap
     * @return
     */
    public static String parseJSONArray(String className, String fileName, final JSONArray jsonArray, Map<String, Map<String, Attr>> classMap) {
        if (TextUtils.isEmpty(className) || null == jsonArray || jsonArray.length() == 0) {
            return null;
        }
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
            if ((className + "$Bean").equals(tmp)) {
                for (int i = 1, n = jsonArray.length(); i < n; i++) {
                    parseJSONArray(className + "$Bean", fileName + "$Bean", jsonArray.optJSONArray(i), classMap);
                }
            }
            result = "List<" + tmp + ">";
        } else {
            result = valueType.getSimpleName();
        }

        return result;
    }

    /**
     * original class name maybe used, make the new class name and return.
     * @param fileName
     * @param className
     * @return
     */
    public static String getUsableClassName(String fileName, String className) {
        if (TextUtils.isEmpty(fileName)) {
            return firstWord2UpperCase(className);
        }
        String[] names = fileName.split("#");
        if (names.length == 0) {
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
