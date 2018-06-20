package hawk.com.zjjapplication.jsonbean;

import java.io.Serializable;

/**
 * 待办任务列表
 * Created by fushichao on 2018/06/06.
 */

public class BacklogMsgBean implements Serializable {

    private String task_billtype;//单据类型
    private String task_billname;//单据名称
    private String task_billdate;//单据日期
    private String task_billstate;//单据状态
    private String task_billid;//单据ID
    private String task_operator;//制单

    public String getTask_operator() {
        return task_operator;
    }

    public void setTask_operator(String tasek_operator) {
        this.task_operator = tasek_operator;
    }

    public String getTask_billtype() {
        return task_billtype;
    }

    public void setTask_billtype(String task_billtype) {
        this.task_billtype = task_billtype;
    }

    public String getTask_billname() {
        return task_billname;
    }

    public void setTask_billname(String task_billname) {
        this.task_billname = task_billname;
    }

    public String getTask_billdate() {
        return task_billdate;
    }

    public void setTask_billdate(String task_billdate) {
        this.task_billdate = task_billdate;
    }

    public String getTask_billstate() {
        return task_billstate;
    }

    public void setTask_billstate(String task_billstate) {
        this.task_billstate = task_billstate;
    }

    public String getTask_billid() {
        return task_billid;
    }

    public void setTask_billid(String task_billid) {
        this.task_billid = task_billid;
    }
}
