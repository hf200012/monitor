package cc.julong.monitor.bean;

import java.util.List;

/**
 * Created by zhangfeng on 2014/11/19.
 */
public class FaceResponse {

    private int total ;

    private List<FaceRecord> records;

    private int code;

    private String msg;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FaceRecord> getRecords() {
        return records;
    }

    public void setRecords(List<FaceRecord> records) {
        this.records = records;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
