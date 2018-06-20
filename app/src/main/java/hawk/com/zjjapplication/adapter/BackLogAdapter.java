package hawk.com.zjjapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hawk.com.zjjapplication.R;
import hawk.com.zjjapplication.jsonbean.BacklogBoardBean;

/**
 * 待办事项看板页面数据适配器
 * Created by fushichao on 2018/6/14.
 */

public class BackLogAdapter extends BaseAdapter{

    /**
     * 待办/已办消息集合（viewHolder）,调用者传入
     */
    private ArrayList<BacklogBoardBean> infoList;
    private ViewHolder viewHolder;
    private Context context;

    public BackLogAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    /**
     * 设置数据列表
     *
     * @param
     */
    public void setList(ArrayList<BacklogBoardBean> infoList) {
        this.infoList = infoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (infoList != null) {
            return infoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (infoList != null) {
           return infoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup praent) {
        // TODO Auto-generated method stub
        try {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.element_navigation_fsc, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.updateView(position);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView tab_item_image;//显示图片
        private TextView tab_item_text;//显示名称
        private TextView tab_item_badge;//未处理消息数量


        /**
         * ViewHolder构造函数
         *
         * @param v
         */
        public ViewHolder(View v) {
            initView(v);
        }

        private void initView(View v) {
            // TODO Auto-generated method stub
            tab_item_image = (ImageView) v.findViewById(R.id.tab_item_image);
            tab_item_text = (TextView) v.findViewById(R.id.tab_item_text);
            tab_item_badge =  (TextView) v.findViewById(R.id.tab_item_badge);
        }

        /**
         * 更新view
         *
         * @param position
         */
        public void updateView(int position) {
            BacklogBoardBean ab = infoList.get(position);

            tab_item_image.setImageResource(ab.getItemimg());
            tab_item_text.setText(ab.getBillname());
            tab_item_badge.setText(ab.getBadgenum());

        }
    }

}
