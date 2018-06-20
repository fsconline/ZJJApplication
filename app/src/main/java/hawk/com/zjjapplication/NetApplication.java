package hawk.com.zjjapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hawk.com.zjjapplication.exception.CrashException;

/**
 * Created by yusheng on 2016/11/28.
 */
public class NetApplication extends Application {


    /**
     * 应用fragment指针，　防止二次加载数据造成的数据重复
     */
    public static int index = 0;
    /**
     * 全局上下文
     */
    public static NetApplication application;
    public static Context applicationContext;
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static String codetsid="1";//接收推送

    /**
     * 用于保存当前打开的activity,为了不影响系统回收activity，所以用软引用
     */
    private Map<String, SoftReference<BaseActivity>> activityMaps = new LinkedHashMap<String, SoftReference<BaseActivity>>();
    private Map<String, SoftReference<BaseActivity>> payActivityMaps = new LinkedHashMap<String, SoftReference<BaseActivity>>();
    /**
     * 当前打开的activity的类名
     */
    private String currentClassName = "";


    @Override
    public void onCreate() {
        application=this;
        applicationContext = this;
        new CrashException();
        super.onCreate();

    }
    /**获取上下文*/
    public static Context getInstance() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 将打开的activity添加到map集合
     *
     * @param activity 打开的activity
     */
    public void addPayActivityToMap(BaseActivity activity) {
        String key = activity.getComponentName().getClassName();
        payActivityMaps.put(key, new SoftReference<BaseActivity>(activity));
    }

    /**
     * 将打开的activity添加到map集合
     *
     * @param activity 打开的activity
     */
    public void removePayActivityToMap(BaseActivity activity) {
        String key = activity.getComponentName().getClassName();
        payActivityMaps.remove(key);
    }

    /**
     * 循环遍历并退出列表中打开的activity
     */
    public void exitPay() {
        for (Iterator<Map.Entry<String, SoftReference<BaseActivity>>> iterator = payActivityMaps.entrySet().iterator(); iterator.hasNext(); ) {
            SoftReference<BaseActivity> activityReference = iterator.next().getValue();
            BaseActivity activity = activityReference.get();
            if (activity != null) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
                activity = null;
            }
        }
        payActivityMaps.clear();
    }

    /**
     * 将打开的activity添加到map集合
     *
     * @param activity 打开的activity
     */
    public void addActivityToMap(BaseActivity activity) {
        String key = activity.getComponentName().getClassName();
        activityMaps.put(key, new SoftReference<BaseActivity>(activity));
    }

    /**
     * 设置当前打开的activity的类名
     *
     * @param currentClassName
     */
    public void setCurrentActivityClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    /**
     * 获取当前打开的activity
     *
     * @return
     */
    public BaseActivity getCurrentActivity() {
        BaseActivity hmBaseActivity = null;
        if (!TextUtils.isEmpty(currentClassName) && activityMaps.size() > 0) {
            hmBaseActivity = activityMaps.get(currentClassName).get();
        }
        return hmBaseActivity;
    }

    /**
     * 获取当前打开的activity(该方法是从任务栈中取出位于栈顶的activity，如果activity切换到后台 该方法会返回null)
     *
     * @return
     */
    public BaseActivity getTaskTopActivity() {
        BaseActivity activity = null;
        try {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
            SoftReference<BaseActivity> activityReference = activityMaps.get(componentName.getClassName());
            activity = activityReference.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

    /**
     * 是否已打开程序
     * (有可能已退出程序但后台服务仍然在运行，map中有数据就说明程序被打开了)
     *
     * @return
     */
    public boolean isOpenedApp() {
        if (activityMaps.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * activity关闭时 将其从列表中移除 便于系统及时回收
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityMaps.remove(activity.toString());
    }

    /**
     * 循环遍历并退出列表中打开的activity
     */
    public void exit() {
        for (Iterator<Map.Entry<String, SoftReference<BaseActivity>>> iterator = activityMaps
                .entrySet().iterator(); iterator.hasNext(); ) {
            SoftReference<BaseActivity> activityReference = iterator.next().getValue();
            BaseActivity activity = activityReference.get();
            if (activity != null) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
                activity = null;
            }
        }
        activityMaps.clear();
        //System.exit(0);
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


}
