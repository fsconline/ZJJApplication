package hawk.com.zjjapplication.jsonbean;

import hawk.com.zjjapplication.R;

/**
 * 待办消息面板
 * Created by fushichao on 2018/6/14.
 */

public class BacklogBoardBean {

    private String billtype;//单据类型
    private String billname;//单据类型名称
    private String badgenum;//未处理消息条数
    private int itemimg = R.drawable.navigation_paydues;//显示图标

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBillname() {
        return billname;
    }

    public void setBillname(String billname) {
        this.billname = billname;
    }

    public String getBadgenum() {
        return badgenum;
    }

    public void setBadgenum(String badgenum) {
        this.badgenum = badgenum;
    }

    public int getItemimg() {
        return itemimg;
    }

    public void setItemimg(int itemimg) {
        this.itemimg = itemimg;
    }
}
