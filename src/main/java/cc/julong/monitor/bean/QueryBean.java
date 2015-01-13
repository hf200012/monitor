package cc.julong.monitor.bean;

/**
 * Created by zhangfeng on 2014/10/22.
 */
public class QueryBean  {

    private String channelId;

    private String startTime;

    private String endTime;

    private String featureBin;

    private String time;

    private int interval;
    //黑名单编号
    private String blackListNo;
    //目标类型
    private String type;
    //报警阀值
    private String warnValue;
    //黑名单及其对应的告警阀值，可以使多个，之间用逗号隔开，格式是:黑名单1:告警阀值,黑名单2:告警阀值,黑名单3:告警阀值...
    private String blackListAndWarnValue;

    public String getBlackListAndWarnValue() {
        return blackListAndWarnValue;
    }

    public void setBlackListAndWarnValue(String blackListAndWarnValue) {
        this.blackListAndWarnValue = blackListAndWarnValue;
    }

    public String getBlackListNo() {
        return blackListNo;
    }

    public void setBlackListNo(String blackListNo) {
        this.blackListNo = blackListNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarnValue() {
        return warnValue;
    }

    public void setWarnValue(String warnValue) {
        this.warnValue = warnValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFeatureBin() {
        return featureBin;
    }

    public void setFeatureBin(String featureBin) {
        this.featureBin = featureBin;
    }
}
