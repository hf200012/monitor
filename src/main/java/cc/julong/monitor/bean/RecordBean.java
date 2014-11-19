package cc.julong.monitor.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class RecordBean {

	private int total ;
	
	private List<Record> records;
	
	private int code;
	
	private String msg;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}


	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
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
	
	public static void main(String args[]){
		RecordBean bean = new RecordBean();
		
		List<Record> records = new ArrayList<Record>();
		for(int i = 0;i < 6; i ++){
			Record record = new Record();
			record.setRecordText("172.16.5.50|2858|23|44|24 6 26 86|0.7224575,0.3895....,0.1963");
			record.setRecordImgUrl("http://localhost:8080/monitor/rest/getImg/2858_20141020132345");
			records.add(record);
		}
		
		bean.setCode(200);
		bean.setTotal(6);
		bean.setMsg("ok");
		bean.setRecords(records);
		JSONObject obj = JSONObject.fromObject(bean);
		System.out.println(obj.toString());
	}
	
	
}
