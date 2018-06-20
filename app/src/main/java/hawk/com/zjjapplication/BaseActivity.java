
package hawk.com.zjjapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

import hawk.com.zjjapplication.utils.BitmapUtils;
import hawk.com.zjjapplication.utils.Tools;

/**
 * @author jack_sj @company yjlc.com
 * @version v
 * @Title BaseActivity.java
 * @Description: activity基类
 * @date 2016-3-16 下午4:49:18
 */
public abstract class BaseActivity extends FragmentActivity {


    /**
     * 打开浏览器的action
     */
    public final String ACTION_OPEN_BROWSER = "com.huimei.android.OPEN_BROWSER";
    /**
     * 要打开的页面地址
     */
    public final String BROWSER_TARGET_URL = "url";
    /**
     * 没有跳转动画
     */
    public final int ANIM_NONE = 0;
    /**
     * 从左到右滑动的动画
     */
    public final int ANIM_LEFT_TO_RIGHT = 1;
    /**
     * 从右到左的滑动动画
     */
    public final int ANIM_RIGHT_TO_LEFT = 2;
    /**
     * 序列化实体类
     */
    public final String SERIALIZA_MODEL = "serializaModel";
    /**
     * 按返回键退出程序的按键时间间隔
     */
    private final int onBackDelayTime = 2000;
    /**
     * 布局转换器
     */
    public LayoutInflater mInflater;
    /**
     * 当前activity是否有焦点
     */
    public boolean isHaveFocus = false;
    /**
     * 返回键按下时间
     */
    private long onBackPressedTime;

    /**
     * 屏幕的宽度、高度、密度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;


    /**
     * 根据名字设置图片
     *
     * @param context
     * @param imageView
     * @param name
     */
    @SuppressWarnings("deprecation")
    public static void setImageByName(Context context, ImageView imageView, String name) {
        try {
            Bitmap bitmap = BitmapUtils.createDefaultUserBitmap(name);
            if (null != bitmap) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏颜色
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.black));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        NetApplication.application.addActivityToMap(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isHaveFocus = true;
        NetApplication.application.setCurrentActivityClassName(this.getComponentName().getClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isHaveFocus = false;
    }

    /**
     * activity跳转
     *
     * @param intent   intent对象
     * @param isFinish 是否结束当前activity
     * @param animType 跳转时要执行的动画效果 ANIM_NO_ANIM：没有动画. ANIM_LEFT_TO_RIGHT：从左到右滑动的动画
     *                 ANIM_RIGHT_TO_LEFT：从右到左的滑动动画
     */
    public void startActivity(Intent intent, boolean isFinish, int animType) {
        startActivityByIntent(intent, isFinish, animType);
    }

    /**
     * activity跳转
     *
     * @param targetClass 目标activity的class
     * @param isFinish    是否结束当前activity
     * @param animType    跳转时要执行的动画效果 ANIM_NO_ANIM：没有动画. ANIM_LEFT_TO_RIGHT：从左到右滑动的动画
     *                    ANIM_RIGHT_TO_LEFT：从右到左的滑动动画
     */
    public void startActivity(Class<?> targetClass, boolean isFinish, int animType) {
        Intent intent = new Intent();
        intent.setClass(this, targetClass);
        startActivityByIntent(intent, isFinish, animType);
    }

    /**
     * activity跳转并传递对象
     *
     * @param targetClass    目标activity的class
     * @param serializaModel 要传递的对象必须实现序列化
     * @param isFinish       是否结束当前activity.
     * @param animType       跳转时要执行的动画效果 ANIM_NO_ANIM：没有动画.
     */
    public void startActivity(Class<?> targetClass, Serializable serializaModel, boolean isFinish, int animType) {
        Intent intent = new Intent();
        intent.putExtra(SERIALIZA_MODEL, serializaModel);
        intent.setClass(this, targetClass);
        startActivityByIntent(intent, isFinish, animType);
    }

    /**
     * 获取上级activity传递的对象
     *
     * @return
     */
    public Serializable getSerializaModel() {
        Serializable serializaModel = null;
        try {
            serializaModel = (Serializable) getIntent().getSerializableExtra(SERIALIZA_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serializaModel;
    }

    /**
     * 通过intent启动一个activity
     *
     * @param intent
     */
    public void startActivityByIntent(Intent intent, boolean isFinish, int animType) {
        startActivity(intent);
        if (isFinish) {
            finish();
        }
        switch (animType) {
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;
        }
    }

    /**
     * 连续两次点击返回键退出程序
     */
    public void pressAgainToExit() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - onBackPressedTime >= onBackDelayTime) {
            Tools.Toast("再按一次，就退出喽！(⊙︿⊙)", false);
            onBackPressedTime = currentTimeMillis;
        } else {
            NetApplication.application.exit();
        }
    }

    @Override
    protected void onDestroy() {
        NetApplication.application.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public Resources getResources() {
        //修改系统字体大小后  程序中文字不随系统字体大小改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBackPressed(boolean isFinish, int animType) {
        if (isFinish) {
            finish();
        }
        switch (animType) {
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;
        }
        // super.onBackPressed();
    }

    public void onBackPressed(int animType) {
        super.onBackPressed();
        switch (animType) {
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;
        }
    }

    /*
     * 将activity重新排列 跳转到的activity之前的finish掉后面的替换掉前面的
     */
    public void gototActivity(Class<?> cls) {
        Intent intent;
        intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void ShowToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
//		setContentView(R.layout.layout_null);
        super.finish();
        System.gc();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}
