
package hawk.com.zjjapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import hawk.com.zjjapplication.NetApplication;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title ToolsPreferences.java
 * @Description: 全局共用的对配置文件操作的类
 * @date 2016-3-17 下午2:09:49
 */
public class ToolsPreferences {

    /**
     * sessionId的键
     */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String KEY_APP_USERKEY = "app_userkey";
    public static final String APP_TOKEN = "app_token";
    public static final String APP_USERNAME = "app_username";
    public static final String IS_HAND_PSD = "isEnableHandPwd";

    //缴费计划对象key
    public static final String KEY_OBJ_PAYMENTPLANAGGBEAN = "key_paymentplan";


    /**
     * 软件设置的配置文件名
     */
    private static final String tdSettingPreferencesName = "APP_FLAG";

    /**
     * 获取软件设置
     *
     * @return
     */
    private static SharedPreferences getTDSettingPreferences() {
        return NetApplication.application.getSharedPreferences(tdSettingPreferencesName, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
    }

    /**
     * 编辑储存变量
     */
    public static void setPreferences(String key, String values) {
        Editor editor = getTDSettingPreferences().edit();
        editor.putString(key, values);
        editor.commit();
    }

    /**
     * 根据键删除数据
     *
     * @param key
     */
    public static void removeByKey(String key) {
        Editor editor = getTDSettingPreferences().edit();
        editor.remove(key);
        editor.commit();
    }


    /**
     * 清除所有缓存
     *
     */
    public static void removeAll() {
        Editor editor = getTDSettingPreferences().edit();
        editor.clear();
        editor.commit();
    }


    /**
     * 获取存储变量
     */
    public static String getPreferences(String key, String defaultValues) {
        return getTDSettingPreferences().getString(key, defaultValues);
    }

    /**
     * 获取存储变量
     */
    public static String getPreferences(String key) {
        return getTDSettingPreferences().getString(key, "");
    }

    /**
     * 编辑储存变量
     */
    public static void setPreferences(String key, boolean values) {
        Editor editor = getTDSettingPreferences().edit();
        editor.putBoolean(key, values);
        editor.commit();
    }

    /**
     * 获取存储变量
     */
    public static boolean getPreferences(String key, boolean defaultValues) {
        return getTDSettingPreferences().getBoolean(key, defaultValues);
    }

    /**
     * 编辑储存变量
     */
    public static void setPreferences(String key, int values) {
        Editor editor = getTDSettingPreferences().edit();
        editor.putInt(key, values);
        editor.commit();
    }

    /**
     * 获取存储变量
     */
    public static int getPreferences(String key, int defaultValues) {
        return getTDSettingPreferences().getInt(key, defaultValues);
    }

    /**
     * 编辑储存变量
     */
    public static void setPreferences(String key, float values) {
        Editor editor = getTDSettingPreferences().edit();
        editor.putFloat(key, values);
        editor.commit();
    }

    /**
     * 获取存储变量
     */
    public static float getPreferences(String key, float defaultValues) {
        return getTDSettingPreferences().getFloat(key, defaultValues);
    }

    /**
     * 编辑储存变量
     */
    public static void setPreferences(String key, long values) {
        Editor editor = getTDSettingPreferences().edit();
        editor.putLong(key, values);
        editor.commit();
    }

    /**
     * 获取存储变量
     */
    public static Long getPreferences(String key, long defaultValues) {
        return getTDSettingPreferences().getLong(key, defaultValues);
    }


//    /**
//     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
//     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
//     *
//     * @param object 待加密的转换为String的对象
//     * @return String   加密后的String
//     */
//    private static String Object2String(Object object) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = null;
//        try {
//            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            objectOutputStream.writeObject(object);
//            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray()));
//            objectOutputStream.close();
//            return string;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 使用Base64解密String，返回Object对象
//     *
//     * @param objectString 待解密的String
//     * @return object      解密后的object
//     */
//    private static Object String2Object(String objectString) {
//        byte[] mobileBytes = Base64.decode(objectString.getBytes());
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
//        ObjectInputStream objectInputStream = null;
//        try {
//            objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            Object object = objectInputStream.readObject();
//            objectInputStream.close();
//            return object;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//    /**
//     * 使用SharedPreference保存对象
//     *
//     * @param key        储存对象的key
//     * @param saveObject 储存的对象
//     */
//    public static void setPreferencesForObject(String key, Object saveObject) {
//        String strvalues = Object2String(saveObject);
//        setPreferences(key, strvalues);
//    }
//
//    /**
//     * 获取SharedPreference保存的对象
//     *
//     * @param key 储存对象的key
//     * @return object 返回根据key得到的对象
//     */
//    public static Object getPreferencesForObject(String key) {
//        String string = getPreferences(key);
//        if (string != null && !"".equals(string)) {
//            Object object = String2Object(string);
//            return object;
//        } else {
//            return null;
//        }
//    }


}
