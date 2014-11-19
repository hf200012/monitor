package cc.julong.monitor;

import java.io.Serializable;

/**
 * 查询条件对象
 * @author zhangfeng
 *
 */
public class QueryObject implements Serializable{

	private static final long serialVersionUID = -6787096990820816374L;
	
	//摄像头编号
	private String channelId;
	
	//开始时间
	private String startTime;
	
	//结束时间
	private String endTime;
	
	//480维度的特征码
	private String featureBin;

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
