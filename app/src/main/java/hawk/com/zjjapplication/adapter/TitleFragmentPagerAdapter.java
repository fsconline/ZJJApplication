package hawk.com.zjjapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里使用多个fragment进行切换，好处是每个fragment 都有自己独立的代码
 *
 * Created by fushichao on 2018/5/29.
 */

public class TitleFragmentPagerAdapter  extends FragmentPagerAdapter implements View.OnClickListener{

    /**
     * The m fragment list.
     */
    private List<Fragment> mFragmentList = null;

    private String[] titles; //tab名的列表

    /**
     * Tab标签切换监听
     */
    public interface OnTabChangeListener {
        void onTabChange(int position, String name, View view);
    }

    private OnTabChangeListener listener;

    public void setOnTabChangeListener(OnTabChangeListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }


    /**
     * Instantiates a new ab fragment pager adapter.
     *
     * @param mFragmentManager the m fragment manager
     * @param fragmentList     the fragment list
     */
    public TitleFragmentPagerAdapter(FragmentManager mFragmentManager,
                                     ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }

    /**
     * titles是给TabLayout设置title用的
     *
     * @param mFragmentManager
     * @param fragmentList
     * @param titles
     */
    public TitleFragmentPagerAdapter(FragmentManager mFragmentManager,
                                     List<Fragment> fragmentList, String[] titles) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
        this.titles = titles;
    }

    /**
     * 描述：获取数量.
     *
     * @return the count
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * 描述：获取索引位置的Fragment.
     *
     * @param position the position
     * @return the item
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0)
            return titles[position];
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.iv_goodsLeft:
//                int postion= (int) v.getTag();
//
//                UIUtils.showToast("shoes left"+"\n"+"postion "+postion);
//                 break;
//            case R.id.iv_goodsRight:
//                int postion2= (int) v.getTag();
//
//                UIUtils.showToast("shoes right"+"\n"+"postion "+postion2);
//                break;
        }
    }
}
