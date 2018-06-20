
package hawk.com.zjjapplication.net;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 联网基类，其他联网类都继承此类
 *
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title RequestCallback.java
 * @Description: 请求监听者
 * @date 2016-3-17 下午2:05:24
 */
public interface RequestCallback {

    /**
     * 解析数据
     */
    public Object parserData(JSONObject jsonObject) throws JSONException;

    /**
     * 联网解析结束，但还在异步线程中，这里可以放一些费时操作，如操作数据库，读写文件，不可以放UI相关代码
     */
    public void requestParserFinishedOnAysncTask(Object obj);

    /**
     * 处理状态码的方法,true处理，false不处理
     */
    public boolean handleCode(int responseCode, String responseMessage);

    /**
     * 取消联网
     */
    public void requestCancel();

    /**
     * 联网错误
     */
    public boolean requestFailed(String errorMsg);

    /**
     * 联网解析完成，处理一些UI事件，严禁放入耗时操作，如联网，操作数据库，操作本地文件
     */
    public void requestSuccess(Object object);

    /**
     * 文件上传或下载百分比
     */
    public void uploadProgress(long totalSize, long currentSize);

}
