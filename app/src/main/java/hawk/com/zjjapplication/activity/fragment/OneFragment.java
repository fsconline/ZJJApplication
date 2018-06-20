package hawk.com.zjjapplication.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.activity.TaskListActivity;
import hawk.com.zjjapplication.adapter.BackLogAdapter;
import hawk.com.zjjapplication.jsonbean.BacklogBoardBean;
import hawk.com.zjjapplication.net.RequestListener;
import hawk.com.zjjapplication.request.GenBacklogRequest;

/**
 * 待办窗口
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneFragment extends Fragment implements PullToRefreshBase.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<BacklogBoardBean> infoList;//加载列表数据
    private BackLogAdapter backLogAdapter;//列表适配器

    @BindView(R.id.gridview)
    PullToRefreshGridView gridview;
//    @BindView(R.id.refreshLayout)
//    RefreshLayout refreshLayout;
//
//    @BindView(R.id.pull_refresh_scrollview)
//    PullToRefreshGridView mPullRefreshGridView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.setTitle(param1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_backlog_fsc, container, false);
        ButterKnife.bind(this, view);


        //初始化适配器
        infoList = new ArrayList<>();
        backLogAdapter = new BackLogAdapter(this.getContext());
        //添加并且显示
        gridview.setAdapter(backLogAdapter);
        //添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());

        gridview.setOnRefreshListener(this);

        //初始化数据
        initData();
//
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//            }
//        });


        //生成动态数组，并且转入数据
//        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
//        for(int i=0;i<10;i++)
//        {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemImage", R.drawable.navigation_paydues);//添加图像资源的ID
//            map.put("ItemText", "NO."+String.valueOf(i));//按序号做ItemText
//            map.put("main_tab_badge",String.valueOf(i));
//            lstImageItem.add(map);
//        }
//
//        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
//        SimpleAdapter saImageItems = new SimpleAdapter(this.getContext(), //没什么解释
//                lstImageItem,//数据来源
//                R.layout.element_navigation_btn,//night_item的XML实现
//                //动态数组与ImageItem对应的子项
//                new String[] {"ItemImage","ItemText","main_tab_badge"},
//                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[] {R.id.ItemImage,R.id.ItemText,R.id.main_tab_badge});
//        //添加并且显示
//        gridview.setAdapter(saImageItems);
//        //添加消息处理
//        gridview.setOnItemClickListener(new ItemClickListener());

        return view;
    }

    /**
     * 初始化数据
     */
    public void initData() {
        getData();
        //  结束刷新
        gridview.onRefreshComplete();
    }

    public void getData() {

        RequestListener listener = new RequestListener() {
            @Override
            public void successBack(Object object) {

                if (null != object) {

                    infoList.addAll((List<BacklogBoardBean>) object);
//                    设置数据
                    backLogAdapter.setList(infoList);
                }
//                结束刷新
                gridview.onRefreshComplete();
            }

            @Override
            public void failBack(Object object) {
                gridview.onRefreshComplete();
            }
        };
        GenBacklogRequest request = new GenBacklogRequest(this.getContext(), listener);
        request.startRequest();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String title;

    public void setTitle(String t) {
        title = t;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("setUserVisibleHint", "onResume=" + title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getData();
    }


    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3

            BackLogAdapter adapter = (BackLogAdapter) arg0.getAdapter();

            BacklogBoardBean item = (BacklogBoardBean) adapter.getItem(arg2); //arg0.getItemAtPosition(arg2);
            //显示所选Item的ItemText
            Log.e("ItemClick", "ItemText=");

            //跳转进入列表界面
            Intent mintent = new Intent(getActivity(), TaskListActivity.class)
                    .putExtra("billtype", item.getBilltype())
                    .putExtra("ischecked", false);//在Fragment中实现开启新的Activity,传参
            startActivity(mintent);
        }
    }
}
