package hawk.com.zjjapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.jsonbean.BacklogMsgBean;
import hawk.com.zjjapplication.view.UIUtils;

/**
 * 待办消息列表Rv适配器
 * Created by yusheng on 2016/11/28.
 */
public class BacklogListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    /**
     * 缴费消息集合（normalHolder）,调用者传入
     */
    private List<BacklogMsgBean> normalMsgLis;

    private int NORMAL = 100;

    /**
     * 自定义条目点击监听
     */
    private OnItemClickListener mOnItemClickLitener;

    public BacklogListRvAdapter(List<BacklogMsgBean> normalMsgLis) {
        this.normalMsgLis = normalMsgLis;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new NormalHolder(LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.listitem_backlog_fsc,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

//        Log.d("alan", "holder位置---》" + holder.getLayoutPosition());
        NormalHolder normalHolder = (NormalHolder) holder;
        final int realPostion = position;//获取真正的位置

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

        if (normalMsgLis.size() >0) {
            //设置消息列表数据
            BacklogMsgBean bean = normalMsgLis.get(position );
            String date = bean.getTask_billdate();
            String msg = bean.getTask_billname();
            String state = bean.getTask_billstate();
            String operator = bean.getTask_operator();

            normalHolder.task_billdate.setText(date);
            normalHolder.task_billname.setText(msg);
            normalHolder.task_billstate.setText(state);
            normalHolder.task_operator.setText(operator);

        }
    }

    @Override
    public int getItemViewType(int position) {
            return NORMAL;
    }

    /**
     * 获取item信息
     * @param position
     * @return
     */
   public Object getItemObj(int position){
        if(normalMsgLis!=null){
            return normalMsgLis.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return normalMsgLis.size() ;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (v.getId()) {
            case R.id.foot_view_ly_tv:
                break;
        }
    }

    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int postion);

        void onItemLongClick(View v, int postion);
    }

    //普通的Holder
    class NormalHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_billname)
        TextView task_billname;
        @BindView(R.id.task_billdate)
        TextView task_billdate;
        @BindView(R.id.task_billstate)
        TextView task_billstate;
        @BindView(R.id.task_operator)
        TextView task_operator;

        public NormalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addMoreItem(List<BacklogMsgBean> newDatas) {
        normalMsgLis.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void removeAllItem() {
        normalMsgLis.clear();
        notifyDataSetChanged();
    }

}
