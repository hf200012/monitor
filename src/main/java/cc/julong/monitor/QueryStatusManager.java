package cc.julong.monitor;

import cc.julong.monitor.bean.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多线程查询状态及查询结果管理器
 * 
 * @author zhangfeng
 *
 */
public class QueryStatusManager {
	
	//多线程状态管理容器
	private Map<String,Object> status = Collections.synchronizedMap(new HashMap<String,Object>()) ;
	//多线程查询结果list
	private List<Record> results = Collections.synchronizedList(new ArrayList<Record>());

	/**
	 * 添加记录数据到查询结果集合总
	 * @param record
	 */
	public synchronized void add(Record record){
		if(!this.results.contains(record)){
			this.results.add(record);
		}
	}
	
	/**
	 * 获取查询结果
	 * @return
	 */
	public synchronized List<Record> getResults(){
		return this.results;
	}
	
	/**
	 * 设置查询状态
	 * @param key
	 * @param value
	 */
	public synchronized void setStatus(String key,Object value){
		status.put(key, value);
	}
	
	/**
	 * 判断查询是否完成
	 * @return
	 */
	public synchronized boolean isCompleted(){
		if(status.containsValue(false)){
			return false;
		}
		return true;
	}
	
	/**
	 * 清除查询结果
	 */
	public synchronized void clear(){
		this.status.clear();
		this.results.clear();
	}
}
