package hawk.com.zjjapplication.request;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hawk.com.zjjapplication.net.RequestBase;
import hawk.com.zjjapplication.net.RequestCallback;
import hawk.com.zjjapplication.net.RequestListener;
import hawk.com.zjjapplication.net.RequestParam;
import hawk.com.zjjapplication.utils.AppServiceConstant;
import hawk.com.zjjapplication.utils.Tools;
import hawk.com.zjjapplication.utils.ToolsPreferences;


/**
 * 登陆接口
 * Created by fushichao on 2017/3/3 0003.
 */
public class FormLoginRequest extends RequestBase implements RequestCallback, RequestParam {

    private RequestListener requestListener;
    private Context _context;
    private String login_name;
    private String password;
    private String deviceNum;

    public FormLoginRequest(Context context, RequestListener listener) {
        _context = context;
        requestListener = listener;
        isShowProgressdialog = false;
    }

    /**
     * 开始调用远程接口
     */
    @Override
    public void startRequest() {
        // TODO Auto-generated method stub
        String url = Tools.getApiAddress() + AppServiceConstant.API_PORT_LOGIN;

        Map<String, String> param = new HashMap<String, String>();
        param.put("platform", "android");
        param.put("devicenum", Tools.getIMEI());
        param.put("login_name", login_name);
        param.put("password", password);

        Tools.log(this.getClass().getSimpleName() + " : " + url + param.toString());
        this.onStartTaskPost(_context, this, url, param);

//        getTestData();
    }

    /**
     * 处理返回报文
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    @Override
    public Object parserData(JSONObject jsonObject) throws JSONException {
        // TODO Auto-generated method stub
        Tools.log(this.getClass().getSimpleName() + "返回参数：" + jsonObject.toString());
        if (responseCode == RequestCode_SUCCESS_OPERATION) {

            if (jsonObject != null) {

                ToolsPreferences.setPreferences(ToolsPreferences.APP_TOKEN, jsonObject.optString("app_token"));
                ToolsPreferences.setPreferences(ToolsPreferences.APP_USERNAME, jsonObject.optString("app_username"));
                ToolsPreferences.setPreferences(ToolsPreferences.KEY_APP_USERKEY, jsonObject.optString("app_userkey"));
//                ToolsPreferences.setPreferences(ToolsPreferences.IS_HAND_PSD,jsonObject.optString("isEnableHandPwd"));

                return null;
            }

        }
        return null;
    }

    @Override
    public void requestParserFinishedOnAysncTask(Object obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean handleCode(int responseCode, String responseMessage) {
        // TODO Auto-generated method stub
        if (responseCode != RequestCode_SUCCESS_OPERATION) {
            requestFailed(responseMessage);
            return true;
        }
        return false;
    }

    @Override
    public void requestCancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean requestFailed(String errorMsg) {
        // TODO Auto-generated method stub
        if (null != requestListener)
            requestListener.failBack(errorMsg);
        return false;
    }

    @Override
    public void requestSuccess(Object object) {
        // TODO Auto-generated method stub
        if (null != requestListener)
            requestListener.successBack(object);
    }

    @Override
    public void uploadProgress(long totalSize, long currentSize) {
        // TODO Auto-generated method stub

    }


    @Override
    public String getParamStr() {
        return null;
    }


    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * 编写测试用静态数据
     */
    public void getTestData() {

        ToolsPreferences.setPreferences(ToolsPreferences.APP_TOKEN, "app_token");
        ToolsPreferences.setPreferences(ToolsPreferences.APP_USERNAME, "APP_USERNAME");
        ToolsPreferences.setPreferences(ToolsPreferences.KEY_APP_USERKEY, "app_userkey");

        requestSuccess("");


    }


}
