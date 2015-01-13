package cc.julong.monitor.bean;

public class Record {

	private String recordText;
	
	private String recordImgUrl;

    private String rowkey;

    private Float featureBin;

    private String featureList;

    private String blackListNo;

    public String getBlackListNo() {
        return blackListNo;
    }

    public void setBlackListNo(String blackListNo) {
        this.blackListNo = blackListNo;
    }

    public String getFeatureList() {
        return featureList;
    }

    public void setFeatureList(String featureList) {
        this.featureList = featureList;
    }

    public Float getFeatureBin() {
        return featureBin;
    }

    public void setFeatureBin(Float featureBin) {
        this.featureBin = featureBin;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getRecordText() {
		return recordText;
	}

	public void setRecordText(String recordText) {
		this.recordText = recordText;
	}

	public String getRecordImgUrl() {
		return recordImgUrl;
	}

	public void setRecordImgUrl(String recordImgUrl) {
		this.recordImgUrl = recordImgUrl;
	}
	
}
