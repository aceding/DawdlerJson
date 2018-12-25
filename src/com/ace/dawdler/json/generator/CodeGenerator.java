package com.ace.dawdler.json.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

import com.ace.dawdler.json.utils.FileUtils;
import com.ace.dawdler.json.utils.LogUtils;
import com.ace.dawdler.json.utils.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * use the FreeMaker library to generator .java file.
 *
 * @author aceding
 */
public class CodeGenerator {

    public static String ROOT_PATH = System.getProperty("user.dir");

    public static String TEMPLATE_PATH = ROOT_PATH + File.separator + "res" + File.separator + "template";

    /**
     * method help to generator the outer class.
     *
     * @param packageName
     * @param className
     * @param fileName
     * @param attr_list
     */
    private static void genClass(String packageName, String className, String fileName, List<Attr> attr_list) {
        Template temp = null;

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = cfg.getTemplate("class.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null == temp) {
            LogUtils.printLog("CodeGenerator gen fail.");
            return;
        }

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("packageName", packageName);
        root.put("className", className);
        root.put("author", "DawdlerJson auto create.");
        root.put("date", new Date());
        root.put("attrs", attr_list);

        File dir = new File(ROOT_PATH + "\\src\\" + Utils.packageName2Path(packageName));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File genFile = new File(dir, fileName + ".java");
        if (genFile.exists()) {
            genFile.delete();
        }
        LogUtils.printLog("genFile path is: " + genFile.getAbsolutePath());

        String[] filesInDir = new File(TEMPLATE_PATH).list();
        List<String> innerClassNames = new ArrayList<>();
        for (int i = 0, n = filesInDir.length; i < n; i++) {
            if (!filesInDir[i].equals(fileName) && filesInDir[i].startsWith(fileName)) {
                innerClassNames.add(filesInDir[i]);
            }
            LogUtils.printLog(innerClassNames.toString());
        }
        if (innerClassNames.size() > 0) {
            root.put("hasInnerClass", "true");
            root.put("innerClasses", innerClassNames);
        } else {
            root.put("hasInnerClass", "false");
        }

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(genFile);
            Writer out = new OutputStreamWriter(fos);
            temp.process(root, out);
            fos.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (String innerClassName : innerClassNames) {
            FileUtils.deleteFile(TEMPLATE_PATH + File.separator + innerClassName);
        }

        LogUtils.printLog("JsonParser gen code success!");
    }

    /**
     * method help to generator the inner class.
     *
     * @param className
     * @param fileName
     * @param attr_list
     */
    private static void genInnerClass(String className, String fileName, List<Attr> attr_list) {
        Template temp = null;

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            temp = cfg.getTemplate("inner_class.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null == temp) {
            LogUtils.printLog("CodeGenerator gen fail.");
            return;
        }

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("attrs", attr_list);
        root.put("className", className);

        File dir = new File(TEMPLATE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File genFile = new File(dir, fileName + ".java");
        if (genFile.exists()) {
            genFile.delete();
        }
        LogUtils.printLog("genFile path is: " + genFile.getAbsolutePath());

        String[] filesInDir = new File(TEMPLATE_PATH).list();
        List<String> innerClassNames = new ArrayList<>();
        for (int i = 0, n = filesInDir.length; i < n; i++) {
            if (!filesInDir[i].equals(fileName) && filesInDir[i].startsWith(fileName)) {
                innerClassNames.add(filesInDir[i]);
            }
        }
        if (innerClassNames.size() > 0) {
            root.put("hasInnerClass", "true");
            root.put("innerClasses", innerClassNames);
        } else {
            root.put("hasInnerClass", "false");
        }

        OutputStream fos = null;
        try {
            fos = new FileOutputStream(genFile);
            Writer out = new OutputStreamWriter(fos);
            temp.process(root, out);
            fos.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (String innerClassName : innerClassNames) {
            FileUtils.deleteFile(TEMPLATE_PATH + File.separator + innerClassName);
        }

        LogUtils.printLog("JsonParser gen code success!");
    }

    /**
     * input the package name and attrs tree, to generator the java bean file.
     *
     * @param classMap
     */
    public static void genJavaBeans(String packageName, LinkedHashMap<String, Map<String, Attr>> classMap) {
        if (null == classMap || classMap.isEmpty()) {
            return;
        }
        ListIterator<Map.Entry> i = new ArrayList<Map.Entry>(classMap.entrySet()).listIterator(classMap.size());
        while (i.hasPrevious()) {
            Map.Entry entry = i.previous();
            String fileName = (String) entry.getKey();
            Map<String, Attr> attrMap = (Map<String, Attr>) entry.getValue();
            if (fileName.contains("#")) {
                String className = fileName.substring(fileName.lastIndexOf("#") + 1);
                genInnerClass(className, fileName, new ArrayList<>(attrMap.values()));
            } else {
                genClass(packageName, fileName, fileName, new ArrayList<>(attrMap.values()));
            }
        }

    }
}