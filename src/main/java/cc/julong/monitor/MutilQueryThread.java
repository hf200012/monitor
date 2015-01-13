package cc.julong.monitor;

import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 精确查询线程
 * @author zhangfeng
 *
 */
public class MutilQueryThread implements Runnable {

	private static final Logger LOG = Logger.getLogger(MutilQueryThread.class);

	private Configuration conf = null;
	//查询状态管理器
	private QueryStatusManager manager;
	//表名
	private String tableName;

	//查询对象
	private QueryBean query;

	private String imgUrl;

	private String faceImgUrl;

	private String channelId;

	public MutilQueryThread(QueryStatusManager manager, String channelId,
							QueryBean query, Configuration conf, String imgUrl, String faceImgUrl){
		this.manager = manager;
		if(query.getType().equals("1")) {
			this.tableName = "FSN_VEDIO";
		} else {
			this.tableName = "FSN_FACE";
		}
		this.query = query;
		this.conf = conf;
		this.channelId = channelId;
		this.imgUrl = imgUrl;
		this.faceImgUrl = faceImgUrl;
	}
	
	public void run() {
		try {
			LOG.info( "Thread Id ["+Thread.currentThread().getId()+"] === getChannelId : " + channelId );
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = new Date();
			LOG.info("QueryThread.MutilQueryThread thread ID :[" + Thread.currentThread().getId() + " ] ========= start time : " + format.format(start));
			long starttime = format.parse(query.getStartTime()).getTime() ;
			long endtime = format.parse(query.getEndTime()).getTime();
			//生成开始和结束rowkey
			String startRowkey = channelId + "_" + starttime;
			String endRowkey = channelId + "_" + endtime;
			//执行查询
			selectByRowkeyRange(this.tableName, startRowkey, endRowkey);
			LOG.info("Thread Id ["+Thread.currentThread().getId()+"] ====== "+ channelId +" =  size : " + manager.getResults().size());
			LOG.info("Thread Id ["+Thread.currentThread().getId()+"] === "+channelId +" = query completed : ");
			Date end = new Date();
			LOG.info("QueryThread.MutilQueryThread thread ID :[" + Thread.currentThread().getId() + " ] ========= start time : " + format.format(end));
			LOG.info("QueryThread.MutilQueryThread thread ID :[" + Thread.currentThread().getId() + " ]  ["+channelId + "] ========= cost time : " +(end.getTime() - start.getTime()) / 1000 +" s");

			//设置这个线程的查询状态为完成
			//manager.setStatus(query.getChannelId(), true);
			manager.getStatus().put(channelId,true);
			for(String key : manager.getStatus().keySet()){
				LOG.info( key + ": " + manager.getStatus().get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 根据表名，开始key，结束key查询数据
	 * @param tableName
	 * @param startRowkey
	 * @param endRowkey
	 * @return
	 * @throws java.io.IOException
	 */
	public void selectByRowkeyRange(String tableName, String startRowkey,
			String endRowkey) throws IOException {
		HTableInterface table = null;
		ResultScanner rs = null;
		try{
			table = new HTable(conf,tableName);
			Scan scan = new Scan();
			scan.setStartRow(startRowkey.getBytes());
			//设置scan的扫描范围由startRowkey开始
			Filter filter =new InclusiveStopFilter(endRowkey.getBytes());
			scan.setFilter(filter);
			scan.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("c1"));
			//设置scan扫描到endRowkey停止，因为setStopRow是开区间，InclusiveStopFilter设置的是闭区间
			rs = table.getScanner(scan);
			int count = 0;
			//迭代查询的黑名单
			String[] blackNos = query.getBlackListAndWarnValue().split(",");
			for(String blackName : blackNos) {
				String[] blacklist =  blackName.split(":");
				String blackListName = blacklist[0];
				float warn = Float.parseFloat(blacklist[1]);
				for (Result r : rs) {
					//符合条件的相似度值
					String similarityValue = "";
					boolean blackListRight = false;
					boolean warnRight = false;
					String value = new String(r.getValue("cf".getBytes(), "c1".getBytes()));
					String rowkey = new String(r.getRow());
					String record = value.substring(0, value.lastIndexOf("|"));
					String[] datas = record.split("\\|");
					if (datas.length >= 6) {
						if (datas[5] != null && !datas[5].equals("")) {
							String[] blackList = datas[5].split(",");
							for (String black : blackList) {
								String[] list = black.split(":");
								float warnValue = Float.parseFloat(list[1]);
								//如果和查询条件的黑名单一致
								if (!isEmpty(blackListName) && blackListName.equals(list[0])) {
									blackListRight = true;
								}
								//如果和查询条件的黑名单一致
								if (!isEmpty(blacklist[1])) {
									if (warnValue >= warn) {
										warnRight = true;
									}
								}
								//如果符合条件，跳出循环
								if (blackListRight && warnRight) {
									similarityValue = list[1];
									break;
								} else {
									blackListRight = false;
									warnRight = false;
								}
							}
						}
					}

					if (blackListRight && warnRight) {
						String featureBin = "";
						Record record1 = new Record();
						if (query.getType().equals("1")) {
							featureBin = value.substring(value.lastIndexOf("|") + 1);
							record1.setRecordImgUrl(imgUrl + rowkey);
						} else {
							record1.setRecordImgUrl(this.faceImgUrl + rowkey);
						}
						record = record.substring(0, record.lastIndexOf("|")) + "|" + similarityValue;
						record1.setRecordText(record);
						record1.setBlackListNo(blackListName);
						//record1.setFeatureList(featureBin);
						record1.setRowkey(rowkey);
						manager.getResults().add(record1);
						count++;
					}
				}
			}
			LOG.info("================table ["+ tableName +"] query record count :" + count);
		} catch (Exception e) {
			LOG.info("===================Error，Not Found，Exception: " + e.getMessage());
			e.printStackTrace();
		}	finally{
			if(table != null){
				table.close();
			}
			if(rs != null){
				rs.close();
			}
		}
	}
	
	private boolean isEmpty(String value){
		if(value != null && !value.equals("")){
			return false;
		}
		return true;
	}
}
