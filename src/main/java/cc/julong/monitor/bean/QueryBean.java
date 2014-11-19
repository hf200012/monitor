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
