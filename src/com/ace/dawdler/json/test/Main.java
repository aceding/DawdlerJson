package com.ace.dawdler.json.test;

import com.ace.dawdler.json.gen.JavaBean;
import com.ace.dawdler.json.generator.JSONParser;
import com.ace.dawdler.json.utils.FileUtils;
import com.ace.dawdler.json.utils.TextUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        // 1. read the json string from file.
        String filePath = System.getProperty("user.dir") + "\\res\\json\\tmp.json";
        String jsonStr = null;
        try {
            jsonStr = FileUtils.readFileToString(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }

        // 2. parse json string，auto make the.java file.
        String packageName = "com.ace.dawdler.json.gen";
        String className = "JavaBean";
        JSONParser.parseJSONStr(packageName, className, jsonStr);

        //3. deserialize json string to JavaBean.
        JavaBean bean = JavaBean.convertFromJSONObject(jsonStr);

        //4. serialize JavaBean to json object.
        JSONObject jsonObj = bean.convert2JSONObject();

        //5. print the serialized json object.
        System.out.println(jsonObj);
    }

}