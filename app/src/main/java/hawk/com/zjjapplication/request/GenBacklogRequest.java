package hawk.com.zjjapplication.request;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.jsonbean.BacklogBoardBean;
import hawk.com.zjjapplication.jsonbean.BacklogMsgBean;
import hawk.com.zjjapplication.net.RequestBase;
import hawk.com.zjjapplication.net.RequestCallback;
import hawk.com.zjjapplication.net.RequestListener;
import hawk.com.zjjapplication.net.RequestParam;
import hawk.com.zjjapplication.utils.JsonUitl;
import hawk.com.zjjapplication.utils.Tools;

/**
 * 获取待办消息看板记录
 * Created by fushichao on 2017/3/3 0003.
 */

public class GenBacklogRequest extends RequestBase implements RequestCallback, RequestParam {

    private RequestListener requestListener;
    private Context _context;
    private int pageIndex;
    private int pageSize = 10;//默认每页显示个数
    private boolean ischecked = false;//是否已执行

    public GenBacklogRequest(Context context, RequestListener listener) {
        _context = context;
        requestListener = listener;
    }

    /**
     * 开始调用远程接口
     */
    @Override
    public void startRequest() {
        // TODO Auto-generated method stub
//        String url = Tools.getApiAddress() + AppServiceConstant.API_PORT_MSGQRY;
//        Map<String, String> param = new HashMap<String, String>();
//
//        param.put("platform", "android");
//        param.put("pageIndex", pageIndex + "");
//        param.put("pageSize", pageSize + "");
//
//        Tools.log(this.getClass().getSimpleName() + " : " + url + param.toString());
//        this.onStartTaskPost_Cookie(_context, this, url, param);

        //调用返回测试数据
        getTestData(pageIndex);
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

            String str = jsonObject.optString("jsondata");
            if (str != null && str.length() > 0) {
                List<BacklogMsgBean> infolist = JsonUitl.stringToList(str, BacklogMsgBean.class);
                if (infolist != null && infolist.size() > 0) {
                    return infolist;
                }
            }

        }
        return null;
    }


    /**
     * 设置待办/已办的查询标识
     * false:待办；true:已办
     * @param ischecked
     */
    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
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

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 编写测试用静态数据
     */
    public void getTestData(int pageIndex) {

        List<BacklogBoardBean> normalMsgLis = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            BacklogBoardBean msg = new BacklogBoardBean();

            msg.setBillname("其他非共用经费"+String.valueOf(i)+ischecked);
            msg.setBilltype("billtype");
            msg.setItemimg(R.drawable.navigation_paydues);
            msg.setBadgenum(String.valueOf(i));

            normalMsgLis.add(msg);
        }

        String str = JsonUitl.objectToString(normalMsgLis);

        requestSuccess(normalMsgLis);

    }
}
