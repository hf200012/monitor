package cc.julong.monitor.bean;

/**
 * Created by zhangfeng on 2014/11/19.
 */
public class FaceRecord {

    private String ip;

    private String port;

    private String time;

    private String personId;

    private String recordImgUrl;

    public String getRecordImgUrl() {
        return recordImgUrl;
    }

    public void setRecordImgUrl(String faceImgUrl) {
        this.recordImgUrl = faceImgUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
