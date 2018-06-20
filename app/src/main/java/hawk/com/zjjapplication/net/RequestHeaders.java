
package hawk.com.zjjapplication.net;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.utils.Tools;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title RequestHeaders.java
 * @Description: header类
 * @date 2016-3-17 下午2:06:03
 */
public class RequestHeaders {

    /**
     * 获取header
     *
     * @return
     */
    public static Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("X-Client", getXClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * webView请求时发送的客户端信息
     *
     * @return xClient信息
     */
    public static Map<String, String> webViewXClient() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("X-Client", getXClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获得上传的X-Client信息
     */
    private static String getXClient() {
        String sdk = "";
        try {
            sdk = URLEncoder.encode(Tools.getSDK(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String type = "";
        try {
            type = URLEncoder.encode(Tools.getPhoneModel(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String channelId = Tools.getString(R.string.app_channel);
        String screenSize = Tools.getScreenWidth() + "*" + Tools.getScreenHeight();
//        String imei = Tools.getIMEI();
        String imsi = Tools.getIMSI();
//        String mac = Tools.getMacAddress();
        String version = Tools.getVersionName() + "." + channelId;
        String rn = Tools.getRandomNumber();

//        String xClient =
//                "sdk=" + sdk + ";" +
//                        "screenSize=" + screenSize + ";" +
//                        "type=" + type + ";" +
//                        "imei=" + imei + ";" +
//                        "imsi=" + imsi + ";" +
//                        //在版本号中加上渠道号
//                        "version=" + version + ";" +
//                        "mac=" + mac + ";" +
//                        "rn=" + rn + ";";
        String xClient =
                "";

        return xClient;
    }

}
