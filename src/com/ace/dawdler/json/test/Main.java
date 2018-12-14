package com.ace.dawdler.json.test;

import com.ace.dawdler.json.generator.JSONParser;
import com.ace.dawdler.json.utils.FileUtils;


public class Main {

    public static void main(String[] args) {
        // 1. 读取json文件里面的内容。
        String filePath = System.getProperty("user.dir") + "\\res\\json\\tmp.json";
        String jsonStr = FileUtils.filePath2String(filePath);
        if (null == jsonStr || jsonStr.length() <= 0) {
            return;
        }

        // 2. 解析json文件，生成对应的.java文件。
        JSONParser.parseJSONStr(jsonStr);

//        List<JavaBean> jsonBeanList = JavaBean.convertFromJSONArray(jsonStr);

        System.out.println("");
    }

}