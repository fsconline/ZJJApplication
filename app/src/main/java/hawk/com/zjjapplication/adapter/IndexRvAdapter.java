package hawk.com.zjjapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 首页Rv适配器，包含多个头布局
 * Created by yusheng on 2016/11/28.
 */
public class IndexRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    /**
     * 缴费消息集合（normalHolder）,调用者传入
     */
    private List<?> normalMsgLis;
    /**
     * 头布局总数(头部显示的总数)
     */
    private int HEADER_CONUNT = 2;
    public int TOTAL_CONUNT = 0;

    private int HEADER0 = 0;
    private int HEADER1 = 1;
    private int HEADER2 = 2;
    private int NORMAL = 100;
    private View headView0;
    private View headView1;
    private View headView2;

    private HeadlinesHolder topLineHolder;//置顶消息holder

    public HeadlinesHolder getTopLineHolder() {
        return topLineHolder;
    }

    public void setTopLineHolder(HeadlinesHolder topLineHolder) {
        this.topLineHolder = topLineHolder;
    }

    /**
     * 自定义条目点击监听
     */
    private OnItemClickListener mOnItemClickLitener;


    public IndexRvAdapter() {
        super();
    }

    public void setNormalMsgLis(List<?> normalMsgLis) {
        this.normalMsgLis = normalMsgLis;
        TOTAL_CONUNT = HEADER_CONUNT + normalMsgLis.size();//列表显示数量需要将头布局数量加上明细数据数量
    }

    public IndexRvAdapter(List<?> normalMsgLis) {
        this.normalMsgLis = normalMsgLis;
        TOTAL_CONUNT = HEADER_CONUNT + normalMsgLis.size();//列表显示数量需要将头布局数量加上明细数据数量
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER0) {
            return new BannerHolder(headView0);
        } else if (viewType == HEADER1) {
            return new GridMenuHolder(headView1);
        } else if (viewType == HEADER2) {
            return new HeadlinesHolder(headView2);
        } else {
//            return new NormalHolder(View.inflate(UIUtils.getContext(), R.layout.msg_rv_item_normal, null));
            return null;

        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Log.d("alan", "holder位置---》" + holder.getLayoutPosition());
        if (viewType == HEADER0) {
            return;
        } else if (viewType == HEADER1) {
            return;
        } else if (viewType == HEADER2) {
//            return;
            HeadlinesHolder lineHolder = (HeadlinesHolder) holder;

            if (normalMsgLis.size() > 0) {
//                DuesMsgBean topmsg = normalMsgLis.get(0);
//                //顶置消息
//                lineHolder.topmsg_senddate.setText(topmsg.getSenddate());
//                lineHolder.topmsg_vmsginfo.setText(topmsg.getVmsginfo());
            }

        } else if (viewType == NORMAL) {
            NormalHolder normalHolder = (NormalHolder) holder;
            final int realPostion = position - TOTAL_CONUNT;//获取真正的位置

            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = realPostion;
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = realPostion;
                        mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                        return true;
                    }
                });
            }

//            Log.d("alan","位置-->"+position);
//            normalHolder.tv_title.setText(normalGoodsTitls.get(realPostion));
//            normalHolder.iv_goodsLeft.setOnClickListener(this);
//            normalHolder.iv_goodsLeft.setTag(realPostion);
//            normalHolder.iv_goodsRight.setOnClickListener(this);
//            normalHolder.iv_goodsRight.setTag(realPostion);

            if (normalMsgLis.size() > 1) {
                //设置消息列表数据
//                DuesMsgBean bean = normalMsgLis.get(position - HEADER_CONUNT);
//                String date = bean.getSenddate();
//                String msg = bean.getVmsginfo();
//
//                normalHolder.item_senddate.setText(date);
//                normalHolder.item_vmsginfo.setText(msg);
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headView0 != null) {
            return HEADER0;
        } else if (position == 1 && headView1 != null) {
            return HEADER1;
        } else if (position == 2 && headView2 != null) {
            return HEADER2;
        } else {
            return NORMAL;
        }
    }

    public List<?> getNormalMsgLis() {
        if(normalMsgLis == null){
            normalMsgLis = new ArrayList<>();
        }
        return normalMsgLis;
    }

    //有7条普通数据，但是要加上Header的总数
    @Override
    public int getItemCount() {
        return getNormalMsgLis().size() + HEADER_CONUNT;
    }

    /**
     * 添加顶部轮播图banner
     */
    public void addHeadView0(View view) {
        this.headView0 = view;
    }

    /**
     * 添加中部导航栏
     */
    public void addHeaderView1(View v) {
        this.headView1 = v;
    }

    /**
     * 添加顶置消息栏
     */
    public void addHeaderView2(View v) {
        this.headView2 = v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int postion);

        void onItemLongClick(View v, int postion);
    }

    //顶部图片轮播图banner
    class BannerHolder extends RecyclerView.ViewHolder {

        public BannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //中部导航
    class GridMenuHolder extends RecyclerView.ViewHolder {
        public GridMenuHolder(View itemView) {
            super(itemView);
        }
    }

    //普通的Holder
    class NormalHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.item_senddate)
//        TextView item_senddate;
//        @BindView(R.id.item_vmsginfo)
//        TextView item_vmsginfo;

        public NormalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 顶置消息
     */
    class HeadlinesHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.topmsg_senddate)
//        TextView topmsg_senddate;
//        @BindView(R.id.topmsg_vmsginfo)
//        TextView topmsg_vmsginfo;

        public HeadlinesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeAllItem() {
        normalMsgLis.clear();
        TOTAL_CONUNT = 0;//列表显示数量需要将头布局数量加上明细数据数量
        notifyDataSetChanged();
    }

}
