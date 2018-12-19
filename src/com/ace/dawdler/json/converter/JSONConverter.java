package com.ace.dawdler.json.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.ace.dawdler.json.annotation.Alias;
import com.ace.dawdler.json.utils.LogUtils;
import com.ace.dawdler.json.utils.TextUtils;
import com.ace.dawdler.json.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * class help to achieve the conversion between the json and java bean.
 *
 * @author aceding
 */
public class JSONConverter {

    /**
     * convert the java bean object to json object.
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JSONObject convert2JSONObject(T t) {
        JSONObject jsonObj = new JSONObject();
        if (null == t) {
            return jsonObj;
        }

        Field[] fields = t.getClass().getDeclaredFields();
        if (null == fields || fields.length == 0) {
            return jsonObj;
        }

        for (Field field : fields) {
            Alias fieldAlias = field.getAnnotation(Alias.class);
            if (null == fieldAlias) {
                continue;
            }
            String fieldName = fieldAlias.value();
            if (TextUtils.isEmpty(fieldName)) {
                continue;
            }

            try {
                Object fieldObj = field.get(t);
                if (null == fieldObj) {
                    continue;
                }
                if (Utils.isPrimitiveType(fieldObj)) {
                    jsonObj.put(fieldName, fieldObj);
                } else if (fieldObj instanceof List<?>) {
                    jsonObj.put(fieldName, convert2JSONArray((List<?>) fieldObj));
                } else {
                    jsonObj.put(fieldName, convert2JSONObject(fieldObj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return jsonObj;
    }

    /**
     * convert the java bean object list to json array object.
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> JSONArray convert2JSONArray(List<T> list) {
        JSONArray jsonArray = new JSONArray();
        if (null == list || list.isEmpty()) {
            return jsonArray;
        }
        for (T t : list) {
            if (null == t) {
                continue;
            }
            if (Utils.isPrimitiveType(t)) {
                jsonArray.put(t);
            } else if (t instanceof List<?>) {
                jsonArray.put(convert2JSONArray((List<?>) t));
            } else {
                jsonArray.put(convert2JSONObject(t));
            }
        }

        return jsonArray;
    }

    /**
     * convert the json string to java bean object.
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T convertFromJSONObject(String jsonStr, Class<T> cls) {
        if (TextUtils.isEmpty(jsonStr) || null == cls) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            return convertFromJSONObject(jsonObj, cls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * convert the json string to java bean object list.
     *
     * @param jsonStr
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> convertFromJSONArray(String jsonStr, Type type) {
        if (TextUtils.isEmpty(jsonStr) || null == type) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            return convertFromJSONArray(jsonArray, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * convert the json array object to java bean object list.
     *
     * @param jsonArray
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> convertFromJSONArray(JSONArray jsonArray, Type type) {
        if (null == jsonArray || null == type) {
            LogUtils.printLog("null == jsonArray or null == type, return.");
            return null;
        }

        List<T> resultList = new ArrayList<>();
        for (int i = 0, n = jsonArray.length(); i < n; i++) {
            Object element = jsonArray.opt(i);
            if (null == element) {
                continue;
            }

            if (element instanceof JSONObject) {
                Class<T> cls = (Class<T>) Utils.getClass(type.getTypeName());
                T obj = convertFromJSONObject((JSONObject) element, cls);
                resultList.add(obj);
            } else if (element instanceof JSONArray) {
                List objList = convertFromJSONArray((JSONArray) element, ((ParameterizedType) type).getActualTypeArguments()[0]);
                resultList.add((T) objList);
            } else {
                resultList.add((T) element);
            }
        }
        return resultList;
    }

    /**
     * convert the json object to java bean object.
     *
     * @param jsonObj
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T convertFromJSONObject(JSONObject jsonObj, Class<T> cls) {
        if (null == jsonObj || null == cls) {
            LogUtils.printLog("convertFrom jsonObj is null or cls is null.");
            return null;
        }
        LogUtils.printLog("convertFrom jsonObj is : " + jsonObj.toString() + " cls is: " + cls.getName());

        T instance = null;
        try {
            instance = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (null == instance) {
            LogUtils.printLog("instance is null, return.");
            return null;
        }

        Field[] fields = cls.getDeclaredFields();
        if (null == fields || fields.length == 0) {
            LogUtils.printLog("fields is empty, return.");
            return null;
        }
        int fieldLength = fields.length;
        for (int i = 0; i < fieldLength; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                continue;
            }
            Alias fieldAlias = field.getAnnotation(Alias.class);
            if (null == fieldAlias) {
                continue;
            }
            String fieldName = fieldAlias.value();
            if (TextUtils.isEmpty(fieldName)) {
                continue;
            }
            LogUtils.printLog("fieldName is: " + fieldName);
            if (!jsonObj.has(fieldName)) {
                LogUtils.printLog("jsonObj has no fieldName, return.");
                continue;
            }

            try {
                String fieldTypeName = field.getType().getSimpleName();
                LogUtils.printLog("fieldTypeName is: " + fieldTypeName);
                field.setAccessible(true);
                if ("int".equals(fieldTypeName)) {
                    int value = jsonObj.optInt(fieldName);
                    field.set(instance, value);
                } else if ("boolean".equals(fieldTypeName)) {
                    boolean value = jsonObj.optBoolean(fieldName);
                    field.set(instance, value);
                } else if ("long".equals(fieldTypeName)) {
                    long value = jsonObj.optLong(fieldName);
                    field.set(instance, value);
                } else if ("double".equals(fieldTypeName) || "float".equals(fieldTypeName)) {
                    double value = jsonObj.optDouble(fieldName);
                    field.set(instance, value);
                } else if ("String".equals(fieldTypeName)) {
                    String value = jsonObj.optString(fieldName);
                    field.set(instance, value);
                } else if ("Object".equals(fieldTypeName)) {
                    field.set(instance, new Object());
                } else {
                    Object tmpObj = jsonObj.opt(fieldName);
                    Object value = null;
                    if (tmpObj instanceof JSONObject) {
                        value = convertFromJSONObject((JSONObject) tmpObj, field.getType());
                    } else if (tmpObj instanceof JSONArray) {
                        ParameterizedType type = (ParameterizedType) field.getGenericType();
                        value = convertFromJSONArray((JSONArray) tmpObj, type.getActualTypeArguments()[0]);
                    }
                    field.set(instance, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        return instance;
    }

}
