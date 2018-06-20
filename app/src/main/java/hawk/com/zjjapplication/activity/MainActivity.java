package hawk.com.zjjapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import hawk.com.zjjapplication.BaseActivity;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.activity.fragment.BacklogChip;
import hawk.com.zjjapplication.bottomtabbar.BottomTabBar;

/**
 * 首页
 * Created by fsc on 2018/5/29 0028.
 */
public class MainActivity extends BaseActivity {


    private BottomTabBar mBottomBar;

    public BottomTabBar getmBottomBar() {
        return mBottomBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main_fsc);

        mBottomBar = (BottomTabBar) findViewById(R.id.bottom_bar);
        mBottomBar.init(getSupportFragmentManager())
                .setImgSize(getResources().getDimension(R.dimen.w_footbar_img), getResources().getDimension(R.dimen.w_footbar_img))
                .setFontSize(getResources().getDimension(R.dimen.size_footbar_fount))
                .setTabPadding(getResources().getDimension(R.dimen.martop_footbar), getResources().getDimension(R.dimen.marmiddle_footbar_fount), getResources().getDimension(R.dimen.marbottom_footbar))
                . setTabBarBackgroundColor(getResources().getColor(R.color.white))
                .setChangeColor(getResources().getColor(R.color.color_3BD2FF), getResources().getColor(R.color.tabClick))
                .addTabItem("任务", R.drawable.bar_icon_index, R.drawable.bar_icon_index_grey, BacklogChip.class)
                .addTabItem("申请", R.drawable.bar_icon_dues, R.drawable.bar_icon_dues_grey, BacklogChip.class)
                .addTabItem("报销", R.drawable.bar_icon_my, R.drawable.bar_icon_my_grey, BacklogChip.class)
                .addTabItem("查询", R.drawable.bar_icon_index, R.drawable.bar_icon_index_grey, BacklogChip.class)
                .addTabItem("我的", R.drawable.bar_icon_my, R.drawable.bar_icon_my_grey, BacklogChip.class)
//                .setTabBarBackgroundResource(R.mipmap.ic_launcher)
                .setTabBarBackgroundColor(getResources().getColor(R.color.f5))
                .isShowDivider(true)
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {
                        Log.i("TGA", "位置：" + position + "      选项卡：" + name);
                        if (position == 2) {
//                            showTree(view);
//                            MyFragment frag2 = (MyFragment) getSupportFragmentManager().findFragmentByTag(name);
//                            if (frag2 != null) {
//                                frag2.initView();
//                            }
                        }
                    }
                });

    }


}
