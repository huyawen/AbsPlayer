package com.meiaomei.absadplayerrotation.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by meiaomei on 2017/4/18.
 */
public class XmlUtils {

    Context context;

    public XmlUtils(Context context) {
        this.context = context;
    }

    public  boolean writeXmlToMemory(String str, String fileName) {
        System.out.println("run==>writeToXml");
        OutputStream out = null;
        try {
            String dirName = "xml";
            File workDir = context.getDir(dirName, Context.MODE_PRIVATE); //存在找到路径，不存在创建xml目录
            if (!workDir.exists())
                workDir.mkdir();
            File f = new File(workDir + "/" + fileName+".xml");
            out = new FileOutputStream(f);

        } catch (Exception e) {
            Log.e("ERROR ====","Error" + e.getMessage());
            return false;
        }
        OutputStreamWriter outw = new OutputStreamWriter(out);
        try {
            outw.write(str);
            outw.close();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public  String xmlFileToString(File file){
        String xmlString="";
        try {
            InputStream in = new FileInputStream(file);
             xmlString=inputStream2String(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            return  xmlString;
    }

    public static  String inputStream2String(InputStream is){
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    public void inputstreamtofile(InputStream ins,File file){
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**
     * 解析list集合的XML转换成对象
     *
     * @param is 输入流
     * @param clazz 对象Class
     * @param fields 字段集合一一对应节点集合
     * @param elements    //d（这两行标红，是因为这两个是java中字段和xml文件中的字段对应） 节点集合一一对应字段集合
     * @param itemElement 每一项的节点标签  例 systemSetting
     * @return
     */
    //静态方法中加入泛型，需要申明<T>
    public static <T> List<T> parse(InputStream is, Class<T> clazz, List<String> fields, List<String> elements, String itemElement) {
        Log.v("rss", "开始解析XML.");
        List<T> list = new ArrayList<T>();
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            int event = xmlPullParser.getEventType();//得到元素个数
            T obj = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            obj = clazz.newInstance();
                        }
                        if (obj != null && elements.contains(xmlPullParser.getName())) {
                            setFieldValue(obj, fields.get(elements.indexOf(xmlPullParser.getName())), xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (itemElement.equals(xmlPullParser.getName())) {
                            list.add(obj);
                            obj = null;
                        }
                        break;
                }
                event = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.e("rss", "解析XML异常：" + e.getMessage());
            throw new RuntimeException("解析XML异常：" + e.getMessage());
        }
        return list;
    }

    /**
     * 设置字段值
     *
     * @param propertyName 字段名
     * @param obj 实例对象
     * @param value 新的字段值
     * @return
     */
    public static void setFieldValue(Object obj, String propertyName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(propertyName);//根据反射得到属性
            field.setAccessible(true);
            if (field.getGenericType().toString().contains("int")){//int类型的属性
                field.set(obj, Integer.parseInt((String) value));
            }
            if (field.getType().toString().contains("float")){//float
                field.setFloat(obj, Float.parseFloat((String) value));
            }
            if (field.getType().toString().contains("String")){//string  类型的属性
                field.set(obj, value);
            }
            if (field.getType().toString().contains("boolean")){//Boolean  类型的属性
                field.setBoolean(obj, Boolean.parseBoolean((String) value));
            }
            if (field.getType().toString().contains("long")){//Boolean  类型的属性
                field.setLong(obj, Long.valueOf((String) value));
            }
            if (field.getType().toString().contains("Date")){//Date  类型的属性
                if (value.equals("0") || value.equals("")) {
                    field.set(obj, null);
                } else {
                    field.set(obj, DateUtils.string2Date((String) value));
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

}
