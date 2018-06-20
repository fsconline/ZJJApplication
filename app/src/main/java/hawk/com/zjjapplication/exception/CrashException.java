
package hawk.com.zjjapplication.exception;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import hawk.com.zjjapplication.NetApplication;
import hawk.com.zjjapplication.utils.AppConstant;
import hawk.com.zjjapplication.utils.CacheDataMgr;
import hawk.com.zjjapplication.utils.DateUtil;
import hawk.com.zjjapplication.utils.Tools;
import hawk.com.zjjapplication.utils.ToolsFile;


/**
 * @author jack_sj @company yjlc.com
 * @version v
 * @Title CrashException.java
 * @Description: 自定义全局异常类
 * @date 2016-3-16 下午4:58:34
 */
public class CrashException implements UncaughtExceptionHandler {

    /**
     * 文件最大值
     */
    private final int fileSizeMax = 100 * 1024;
    private UncaughtExceptionHandler defaultExceptionHandler;

    public CrashException() {
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        if (defaultExceptionHandler != null) {
            //将异常信息输出
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            saveErrorLog(DateUtil.getTodayDateStr() + sw.toString() + "\n");
            // 将异常抛出，则应用会弹出异常对话框
            defaultExceptionHandler.uncaughtException(thread, e);
        }
    }

    /**
     * 保存错误日志
     *
     * @param content
     */
    private void saveErrorLog(String content) {
        try {

            CacheDataMgr.cleanDatabases(NetApplication.applicationContext);

            File file = new File(Tools.getRootPath() + AppConstant.root
                    + AppConstant.log);
            if (file.exists() && file.length() >= fileSizeMax) {
                file.delete();
            }
            ToolsFile.writeDataToSdcard(content.getBytes(), AppConstant.root, AppConstant.log, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @category 获取try-catch中的异常内容
     * @param e Exception
     * @return  异常内容
     */
    public static String getException(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception ex) {

        }
        return ret;
    }

}
