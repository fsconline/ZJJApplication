
package hawk.com.zjjapplication.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.Executors;

import hawk.com.zjjapplication.NetApplication;
import hawk.com.zjjapplication.exception.NetworkConnectException;
import hawk.com.zjjapplication.exception.NetworkForceCloseException;
import hawk.com.zjjapplication.exception.NetworkNotException;
import hawk.com.zjjapplication.exception.NetworkTimeoutException;
import hawk.com.zjjapplication.exception.ParseException;
import hawk.com.zjjapplication.exception.SdcardException;
import hawk.com.zjjapplication.utils.CustomTextViewDialog;
import hawk.com.zjjapplication.utils.Tools;
import hawk.com.zjjapplication.utils.UIUtil;

/**
 * @author Jack_SJ
 * @version v
 *          Copyright Yjlc Co. Ltd.
 * @Title RequestBase.java
 * @Description: 联网基类，其他联网类都继承此类
 * @date 2016-3-17 下午2:05:09
 */
public class RequestBase {

    public static final int RequestCode_DATAULT = -1;
    /**
     * 操作成功的返回码
     */
    public static final int RequestCode_SUCCESS_OPERATION = 1;
    /**
     * 未知错误
     */
    public static final int RequestCode_UNKNOWN_ERROR = 0;
    /**
     * 联网类型
     */
    private final int requestGet = 0;
    private final int requestPost = requestGet + 1;
    private final int requestPostFile = requestPost + 1;
    private final int requestGetFile = requestPostFile + 1;
    private final int requestPost_cookie = requestGetFile + 1;
    /**
     * dialog的显示信息
     */
    protected String dialogMessage = "";
    /**
     * 是否显示联网对话框
     */
    protected boolean isShowProgressdialog = false;
    /**
     * 是否可以取消dialog
     */
    protected boolean isCancelDialog = true;
    /**
     * 是否解析
     */
    protected boolean isParser = true;
    /**
     * 联网返回的状态码
     */
    protected int responseCode = RequestCode_DATAULT;
    /**
     * 联网返回的状态信息
     */
    protected String responseMessage = "";
    /**
     * 联网底层类
     */
    private RequestConnection remoteRequester = new RequestConnection();
    /**
     * 对话框显示的context
     */
    private Context context = null;
    /**
     * 取消标记
     */
    private boolean cancel = false;
    /**
     * 联网类型
     */
    private int requestType = requestPost;
    /**
     * url地址
     */
    private String url = "";
    /**
     * 上传的参数
     */
    private String params;
    /**
     * 上传文件的参数
     */
    private Map<String, File> files;
    /**
     * 下载文件路径
     */
    private String filePath = "";
    /**
     * 下载文件当前长度
     */
    private long currentLength = 0;
    /**
     * 下载文件分段长度
     */
    private long targetLength = 0;
    /**
     * 回调函数
     */
    private RequestCallback requestCallback;

    /**
     * 获取Context
     *
     * @return
     */
    protected Context getContext() {
        return context;
    }

    /**
     * 开始联网，Get
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     */
    protected final void onStartTaskGet(Context context, RequestCallback requestCallback, String url, Map<String, String> params) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestGet;
        this.url = url;
        this.params = getParamStr(params);
        startAsyncTask();
    }

    /**
     * 开始联网，Get
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     */
    protected final void onStartTaskGet(Context context, RequestCallback requestCallback, String url, String params) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestGet;
        this.url = url;
        this.params = params;
        startAsyncTask();
    }

    /**
     * 开始联网，post
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     */
    protected final void onStartTaskPost(Context context, RequestCallback requestCallback, String url, Map<String, String> params) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestPost;
        this.url = url;
        this.params = getParamStr(params);
        Tools.log("参数：" + params);
        startAsyncTask();
    }

    /**
     * 开始联网，post
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     */
    protected final void onStartTaskPost_Cookie(Context context, RequestCallback requestCallback, String url, Map<String, String> params) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestPost_cookie;
        this.url = url;
        this.params = getParamStr(params);
        startAsyncTask();
    }

    /**
     * 开始联网，post
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     */
    protected final void onStartTaskPost(Context context, RequestCallback requestCallback, String url, String params) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestPost;
        this.url = url;
        this.params = params;
        startAsyncTask();
    }

    /**
     * 开始联网,上传文件
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param params          联网参数
     * @param files           文件参数
     */
    protected final void onStartTaskPostFile(Context context, RequestCallback requestCallback, String url, Map<String, String> params, Map<String, File> files) {
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestPostFile;
        this.url = url;
        this.params = getParamStr(params);
        this.files = files;
        startAsyncTask();
    }

    /**
     * 异步下载文件
     *
     * @param context         不为空则显示联网对话框
     * @param requestCallback 子类实现的接口
     * @param url             联网地址
     * @param filePath        联网参数
     */
    protected final void onStartAsyncTaskGetFile(Context context, RequestCallback requestCallback, String url, String filePath) {
        this.isParser = false;
        this.context = context;
        this.requestCallback = requestCallback;
        this.requestType = requestGetFile;
        this.url = url;
        this.filePath = filePath;
        startAsyncTask();
    }

    /**
     * 开始联网,下载文件(不开启异步任务)
     *
     * @param url
     * @param filePath
     */
    public final boolean onStartSyncTaskGetFile(String url, String filePath) {
        try {
            remoteRequester.doDownloadFile(url, filePath, currentLength, targetLength, requestCallback);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消联网请求
     */
    public void cancleRequest() {
        cancel = true;
        remoteRequester.cancel();
        onCancelTask();
    }

    /**
     * 开始异步联网
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAsyncTask() {
        //设置联网dialog
        if (getContext() != null && isShowProgressdialog) {
            UIUtil.showProgressDialog(getContext(), dialogMessage, new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    cancleRequest();
                }

            });
        }

        //开启异步联网
        new AsyncTask<String, Integer, Object>() {
            private Exception myException = null;

            @Override
            protected Object doInBackground(String... params) {
                try {
                    Object o = onRequestTask();
                    //假如不是下载，解析返回的数据
                    if (isParser) {
                        o = onParserTask((String) o);
                    }
                    //联网解析完成，提供一个方法供子类使用，做一些数据库，本地存储等耗时操作
                    if (requestCallback != null) {
                        requestCallback.requestParserFinishedOnAysncTask(o);
                    }
                    return o;
                } catch (Exception e) {
                    myException = e;
                }
                return null;
            }

            protected void onPostExecute(Object object) {
                if (!cancel) {
                    try {
                        if (myException == null) {
                            if (!isParser || onHandleCodeTask()) {
                                onFinishTask(object);
                            }
                        } else {
                            onFailedTask(myException);
                        }
                    } catch (Exception e) {
                        onFailedTask(e);
                    } finally {
                        if (isShowProgressdialog)
                            UIUtil.dismissProgressDialog(NetApplication.applicationContext);
                    }
                }
            }

            ;
        }.executeOnExecutor(Executors.newCachedThreadPool());

    }

    /**
     * 联网中
     *
     * @return
     * @throws Exception
     */
    private Object onRequestTask() throws Exception {
        Object o = null;
        //根据不同的类型选择不同的处理方式
        switch (requestType) {
            case requestGet:
                o = remoteRequester.doGet(url, params);
                break;
            case requestPost:
                o = remoteRequester.doPost(url, params, false);
                break;
            case requestPost_cookie:
                o = remoteRequester.doPost(url, params, true);
                break;
            case requestPostFile:
                o = remoteRequester.doPostFile(url, params, files);
                break;
            case requestGetFile:
                o = remoteRequester.doDownloadFile(url, filePath, currentLength, targetLength, requestCallback);
                break;
        }
        return o;
    }

    /**
     * 解析数据，如果解析的数据不止状态码，需要子类重写此方法
     *
     * @param response
     * @return
     * @throws ParseException
     */
    private Object onParserTask(String response) throws ParseException {
        try {
            //解析状态码
            JSONObject jsonObject = new JSONObject(response);

            String code = jsonObject.optString("flag");
            if (null != code && code.length() > 0) {
                responseCode = Integer.parseInt(code);
            }
            responseMessage = jsonObject.optString("msg", "");

            //解析状态码之后的信息，子类实现
            if (this.requestCallback != null) {
                return this.requestCallback.parserData(jsonObject);
            }
            return null;
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    /**
     * 统一处理状态码
     * 返回true 基类不再处理，
     * 返回false，基类继续处理
     *
     * @return
     */
    private boolean onHandleCodeTask() {
        //子类处理
        if (this.requestCallback != null) {
            boolean result = this.requestCallback.handleCode(this.responseCode, this.responseMessage);
            if (result) {
                return false;
            }
        }
        //子类没有处理，父类处理
        switch (responseCode) {
            case RequestCode_SUCCESS_OPERATION:
                return true;
            case RequestCode_UNKNOWN_ERROR:
            case RequestCode_DATAULT:
                showRetryDialog(responseMessage);
                return false;
        }
        return true;
    }

    /**
     * 联网完成，处理一些UI事件，严禁放入耗时操作，如联网，操作数据库，操作本地文件
     */
    private void onFinishTask(Object o) throws Exception {
        if (requestCallback != null) {
            requestCallback.requestSuccess(o);
        }
    }

    /**
     * 取消联网
     */
    private void onCancelTask() {
        if (requestCallback != null) {
            requestCallback.requestCancel();
        }
    }

    /**
     * 子类可重写，统一的异常的处理方式，实现此方法，
     * 返回false，基类处理
     * 返回true，基类不处理
     *
     * @param e
     */
    private void onFailedTask(Exception e) {
        if (e instanceof NetworkForceCloseException) {
            //主动取消联网返回异常，不做任何处理
        } else {
            //获取错误信息
            String errorMsg;
            if (e instanceof NetworkNotException) {
                errorMsg = "无网络，请确认网络正常后重试！";
            } else if (e instanceof NetworkTimeoutException) {
                errorMsg = "网络连接超时";
            } else if (e instanceof NetworkConnectException) {
                errorMsg = "网络连接错误" + e.getMessage();
            } else if (e instanceof ParseException) {
                errorMsg = "数据解析错误";
            } else if (e instanceof SdcardException) {
                SdcardException ex = (SdcardException) e;
                String message;
                if (ex.getErrorCode() == SdcardException.SDCARD_ERROR) {
                    message = "存储卡错误！";
                } else if (ex.getErrorCode() == SdcardException.SDCARD_FULL) {
                    message = "存储卡已满！";
                } else {
                    message = "存储卡错误！";
                }

                errorMsg = message;
            } else {
                //showRetryDialog("网络连接失败！");
                errorMsg = "网络连接失败！";
            }

            //子类处理
            if (requestCallback != null) {
                boolean result = requestCallback.requestFailed(errorMsg);
                if (result) {
                    return;
                }
            }

            //子类没有处理，父类处理
            try {
                showRetryDialog(errorMsg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 重试对话框
     *
     * @param message
     * @param message
     */
    private final void showRetryDialog(final String message) {
        if (context != null) {
            final CustomTextViewDialog dialog = new CustomTextViewDialog(context);
            dialog.setTitle("提示");
            dialog.setMessage(message);
            dialog.setCertainButton("重试", new OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                    startAsyncTask();
                }
            });
            dialog.setCancelButton("取消", new OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                    onCancelTask();
                }
            });
            dialog.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.cancel();
                        onCancelTask();
                        return true;
                    }
                    return false;
                }
            });
            dialog.show();
        }
    }


    public String getParamStr(Map<String, String> params) {
        //拼接参数
        String str = "";
        try {
            if (params != null && params.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(entry.getKey() + "=");
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
                }
                str = sb.toString().substring(0, sb.toString().length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}


