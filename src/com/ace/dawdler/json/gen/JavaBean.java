package com.ace.dawdler.json.gen;

import com.ace.dawdler.json.converter.JSONConverter;
import com.ace.dawdler.json.annotation.Alias;
import java.lang.reflect.Type;
import org.json.JSONObject;

import java.util.List;
 
/**
 *	@author aceding 
 *	@date: 2018-12-20 00:26:02 
 */
public class JavaBean {

    @Alias("parent")
    public Parent parent;

    @Alias("name")
    public String name;

    @Alias("age")
    public int age;

    @Alias("hobby")
    public List<List<Hobby$Bean>> hobby;
    
    public static JavaBean convertFromJSONObject(String content){
    	JavaBean result = JSONConverter.convertFromJSONObject(content, JavaBean.class);
    	return result;
    }

    public static List<JavaBean> convertFromJSONArray(String content){
        Type type = new JavaBean(){}.getClass().getGenericSuperclass();
        List<JavaBean> result = JSONConverter.convertFromJSONArray(content, type);
        return result;
    }

    public JSONObject convert2JSONObject(){
        JSONObject result = JSONConverter.convert2JSONObject(this);
        return result;
    }


    public static class Hobby$Bean {

        @Alias("dream")
        public String dream;

        @Alias("night")
        public String night;

        @Alias("day")
        public String day;

        public static Hobby$Bean convertFromJSONObject(String content){
            Hobby$Bean result = JSONConverter.convertFromJSONObject(content, Hobby$Bean.class);
            return result;
        }

        public static List<Hobby$Bean> convertFromJSONArray(String content){
            Type type = new Hobby$Bean(){}.getClass().getGenericSuperclass();
            List<Hobby$Bean> result = JSONConverter.convertFromJSONArray(content, type);
            return result;
        }

        public JSONObject convert2JSONObject(){
            JSONObject result = JSONConverter.convert2JSONObject(this);
            return result;
        }

    }

    public static class Parent {

        @Alias("mother")
        public String mother;

        @Alias("father")
        public String father;

        public static Parent convertFromJSONObject(String content){
            Parent result = JSONConverter.convertFromJSONObject(content, Parent.class);
            return result;
        }

        public static List<Parent> convertFromJSONArray(String content){
            Type type = new Parent(){}.getClass().getGenericSuperclass();
            List<Parent> result = JSONConverter.convertFromJSONArray(content, type);
            return result;
        }

        public JSONObject convert2JSONObject(){
            JSONObject result = JSONConverter.convert2JSONObject(this);
            return result;
        }

    }

}