package com.qqdebug.utils;

import com.qqdebug.QQDebugApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @Desc
 * @time 2017/9/13 16:50
 * @Author lixiangxiang
 */

public class PrefUtils {

    //APP配置数据，不会因为用户不同而注销数据
    public static String TB_APP_CONFIG = "TB_APP_CONFIG";
    public static String APP_MOTOR_INFO = "app_motor_info";

    public static final String ACTION_LIST = "ACTIONLIST";
    public static final String URL = "URL";

    /**
     * 保存对象到SharedPreferences，存入的对象必需继承Serializable接口，否则取出来会是null
     *
     * @param key     存入的key
     * @param value   需要存入的对象
     */
    public static void saveObject(String name, String key, Object value) {
        SharedPreferences preferences = QQDebugApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(value);
            // 将字节流编码成base64的字符窜
            String data = Base64.encodeToString(baos
                    .toByteArray(), Base64.DEFAULT);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, data);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  数据存储 默认存在app config 内
     * @param key
     * @param value
     */
    public static  void saveObject(String key, Object value){
        saveObject(TB_APP_CONFIG,key,value);
    }
    /**
     * 从SharedPreferences中取对象
     *
     * @param name    sp名称
     * @param key     存入的key
     * @return key对应的object
     */
    public static Object readObject(String name, String key) {
        SharedPreferences preferences = QQDebugApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
        String data = preferences.getString(key, "");
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        //读取字节
        byte[] base64 = Base64.decode(data.getBytes(), Base64.DEFAULT);

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                return bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  数据读取 默认存在app config 内
     * @param key 传入key
     * @return
     */
    public static Object readObject(String key){
        return readObject(TB_APP_CONFIG,key);
    }
}
