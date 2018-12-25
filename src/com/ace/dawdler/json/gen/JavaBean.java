package com.ace.dawdler.json.gen;

import com.ace.dawdler.json.converter.JSONConverter;
import com.ace.dawdler.json.converter.Alias;
import java.lang.reflect.Type;
import org.json.JSONObject;

import java.util.List;
 
/**
 *	@author auto create
 *	@date: 2018-12-25 21:09:11 
 */
public class JavaBean {

    @Alias("res_type")
    public String resType;

    @Alias("res_url")
    public String resUrl;

    @Alias("jump_url")
    public String jumpUrl;

    @Alias("time_begin")
    public String timeBegin;

    @Alias("time_end")
    public String timeEnd;

    @Alias("jump_type")
    public String jumpType;

    @Alias("res_md5")
    public String resMd5;
    
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


}