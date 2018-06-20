package hawk.com.zjjapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hawk.com.zjjapplication.BaseActivity;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.adapter.BacklogListRvAdapter;
import hawk.com.zjjapplication.jsonbean.BacklogMsgBean;
import hawk.com.zjjapplication.net.RequestListener;
import hawk.com.zjjapplication.request.GenBacklogListRequest;
import hawk.com.zjjapplication.utils.UIUtil;
import hawk.com.zjjapplication.view.ButtomBtn;
import hawk.com.zjjapplication.view.HorizontalDividerItemDecoration;
import hawk.com.zjjapplication.view.UIUtils;

/**
 * 待办任务列表
 */
public class TaskListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2, View.OnClickListener, BacklogListRvAdapter.OnItemClickListener  {

    @BindView(R.id.bomBtnMsg)
    ButtomBtn bomBtnMsg;
    @BindView(R.id.bomBtnBack)
    ButtomBtn bomBtnBack;

    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @BindView(R.id.ll_title)
    TextView titlename;

    @BindView(R.id.msgcenter_rv)
    RecyclerView recyclerView;//界面列表
    @BindView(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView mPullRefreshScrollView;

    private BacklogListRvAdapter rvAdapter;//列表适配器
    private LinearLayoutManager linearLayoutManager;//行布局

    private int page = -1;

    private String mLastQuery = "";


    /**
     * 结果集
     */
    private List<BacklogMsgBean> normalMsgLis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_fsc);
        ButterKnife.bind(this);

        initTopBtn();
        setupFloatingSearch();
        initView();
        initData();
    }
    private void initTopBtn() {

        bomBtnBack.setIvAndTv(R.drawable.arrow_back01, "返回");
        titlename.setText("消息中心");
    }

    public void initData() {

        rvAdapter = new BacklogListRvAdapter(new ArrayList<BacklogMsgBean>());

        if (page == -1) {
            getData();
            //  结束刷新
            mPullRefreshScrollView.onRefreshComplete();
        }

        rvAdapter.setmOnItemClickLitener(this);//添加行点击监听
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 数据查询
     */
    private void getData() {

        UIUtil.showProgressDialog(this);
        final int nextPage = page + 1;
        RequestListener listener = new RequestListener() {
            @Override
            public void successBack(Object object) {
                List<BacklogMsgBean> lis = (List<BacklogMsgBean>) object;
                if ((null != lis && lis.size() == 0) || lis == null) {
                    normalMsgLis = new ArrayList<>();
                    UIUtils.showToast("未查到当前用户对应的数据");
                } else {
                    page = nextPage;
                    normalMsgLis.addAll(lis);
                    rvAdapter.addMoreItem(lis);
                }
                //结束刷新
                mPullRefreshScrollView.onRefreshComplete();
                UIUtil.dismissProgressDialog();
            }

            @Override
            public void failBack(Object object) {
                //结束刷新
                mPullRefreshScrollView.onRefreshComplete();
                UIUtil.dismissProgressDialog();
            }
        };
        //查询后台得到对应单据类型下的任务列表
        GenBacklogListRequest recordRequest = new GenBacklogListRequest(this, listener);

        String billtype = getIntent().getStringExtra("billtype");
        boolean ischecked = getIntent().getBooleanExtra("ischecked",false);

        recordRequest.setPageIndex(nextPage);
        recordRequest.setBilltype(billtype);;
        recordRequest.setIschecked(ischecked);
        recordRequest.setmLastQuery(mLastQuery);
        //开始查询
        recordRequest.startRequest();
    }

    private void initView() {

        mPullRefreshScrollView.setOnRefreshListener(this);

        //添加下拉刷新监听
        //初始化recyclerview控件
        linearLayoutManager = new LinearLayoutManager(this);
        //垂直方向
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());

    }

    @OnClick(R.id.bomBtnBack)
    public void onClickBack(View v) {
        finish();
    }

    @Override
    public void onItemClick(View v, int postion) {

        int viewType = rvAdapter.getItemViewType(postion);
        BacklogMsgBean msg = (BacklogMsgBean) rvAdapter.getItemObj(postion);
        Log.d("BlankFragment", "onItemClick()");

        boolean ischecked = getIntent().getBooleanExtra("ischecked",false);
        //新建一个Intent(当前Activity, SecondActivity)=====显示Intent
        Intent intent = new Intent(TaskListActivity.this, TaskInfoActivity.class);
        intent.putExtra("extra_ischecked", ischecked);
        intent.putExtra("extra_billtype", msg.getTask_billtype());
        intent.putExtra("extra_billid", msg.getTask_billid());

        //启动Intent
        startActivity(intent);

//        if (viewType == rvAdapter.TYPE_FOOTER) {
//            getData();
//        }
    }

    @Override
    public void onItemLongClick(View v, int postion) {

    }

    public void onRefresh() {

        //当前屏幕所看到的子项个数
        int visibleItemCount = linearLayoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = linearLayoutManager.getItemCount();

        if (visibleItemCount < totalItemCount) {
            //刷新操作从第一页开始刷新
            clearData();
            getData();
        } else {
            getData();
        }

    }

    /**
     * 清除界面数据
     */
    private void clearData(){
        page = -1;
        rvAdapter.removeAllItem();
        normalMsgLis.clear();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        onRefresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getData();
    }


    /**
     * 查询搜索框
     */
    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
        //实时输入监听
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                }
                mSearchView.hideProgress();
                Log.d("BlankFragment", "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String querypart) {
                mLastQuery = querypart;

                //按条件搜索查询
                onSearchQryData(querypart);
                Log.d("BlankFragment", "onSearchAction()");
            }
        });

    }

    /**
     * 按搜索条件进行查询
     */
    private void onSearchQryData(String querypart){

        clearData();

        UIUtil.showProgressDialog(this);
        final int nextPage = page + 1;
        RequestListener listener = new RequestListener() {
            @Override
            public void successBack(Object object) {
                List<BacklogMsgBean> lis = (List<BacklogMsgBean>) object;
                if ((null != lis && lis.size() == 0) || lis == null) {
                    normalMsgLis = new ArrayList<>();
                    UIUtils.showToast("未查到当前用户对应的数据");
                } else {
                    page = nextPage;
                    normalMsgLis.addAll(lis);
                    rvAdapter.addMoreItem(lis);
                }
                //结束刷新
                mPullRefreshScrollView.onRefreshComplete();
                UIUtil.dismissProgressDialog();
            }

            @Override
            public void failBack(Object object) {
                //结束刷新
                mPullRefreshScrollView.onRefreshComplete();
                UIUtil.dismissProgressDialog();
            }
        };
        //查询后台得到对应单据类型下的任务列表
        GenBacklogListRequest recordRequest = new GenBacklogListRequest(this, listener);

        String billtype = getIntent().getStringExtra("billtype");
        boolean ischecked = getIntent().getBooleanExtra("ischecked",false);

        recordRequest.setPageIndex(nextPage);
        recordRequest.setBilltype(billtype);;
        recordRequest.setIschecked(ischecked);
        recordRequest.setmLastQuery(querypart);
        //开始查询
        recordRequest.startRequest();

    }

}
