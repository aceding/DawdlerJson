package com.ace.dawdler.json.gen;

import com.ace.dawdler.json.converter.JSONConverter;
import com.ace.dawdler.json.converter.Alias;
import java.lang.reflect.Type;
import org.json.JSONObject;

import java.util.List;
 
/**
 *	
 *	@date: 2018-12-25 17:38:59 
 */
public class JavaBean {

    @Alias("res_type")
    public String res_type;

    @Alias("res_url")
    public String res_url;

    @Alias("jump_url")
    public String jump_url;

    @Alias("time_begin")
    public String time_begin;

    @Alias("time_end")
    public String time_end;

    @Alias("jump_type")
    public String jump_type;

    @Alias("res_md5")
    public String res_md5;
    
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