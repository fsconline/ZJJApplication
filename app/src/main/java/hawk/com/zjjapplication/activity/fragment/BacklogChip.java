package hawk.com.zjjapplication.activity.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.adapter.IndexRvAdapter;
import hawk.com.zjjapplication.adapter.TitleFragmentPagerAdapter;

/**
 * 待办事项
 * <p>
 * A simple {@link Fragment} subclass.
 */
public class BacklogChip extends Fragment implements View.OnClickListener, IndexRvAdapter.OnItemClickListener {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    public BacklogChip() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_index_fsc, container, false);
        ButterKnife.bind(this, view);

        initData();

//        tabLayout.setTabTextColors(Color.WHITE, Color.GRAY);//设置文本在选中和为选中时候的颜色
//        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);//设置选中时的指示器的颜色
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//可滑动，默认是FIXED

        String[] titles = new String[]{"待办事项", "已办事项"};
        String[] tags = new String[]{"uncheck", "ischecked"};


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(OneFragment.newInstance(titles[0], tags[0]));
        fragments.add(TwoFragment.newInstance(titles[1], tags[1]));

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getFragmentManager(), fragments, titles);

        viewpager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewpager);
        tabLayout.getTabAt(0).setTag(tags[0]);
        tabLayout.getTabAt(1).setTag(tags[1]);
        this.reflex(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.i("TGA", "位置：" + tab.getPosition() + "      选项卡：" + tab.getTag());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    /**
     * 画线
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }


                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            /**
             * dp转px
             *
             * @return
             */
            private int dip2px(Context context, int dpValue) {
                final float scale = context.getResources().getDisplayMetrics().density;
                return (int) (dpValue * scale + 0.5f);
            }

        });

    }

    /**
     * 初始化消息数据
     */
    private void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }


    @Override
    public void onItemClick(View v, int postion) {

    }

    @Override
    public void onItemLongClick(View v, int postion) {

    }

}
