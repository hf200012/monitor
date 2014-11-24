package cc.julong.monitor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cc.julong.monitor.bean.FaceQuery;
import cc.julong.monitor.bean.FaceRecord;
import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;
import cc.julong.monitor.util.PropertiesHelper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;


/**
 * 对外统一提供的数据查询接口
 * @author zhangfeng
 *
 */
public class HBaseQuery implements Query{
	
	private static final Logger LOG = Logger.getLogger(HBaseQuery.class);
	//zookeeper 地址
	private String quorum ;
	//zk port
	private int port;
	
	//hbase在zk上注册的根节点名称
	private String znodeParent ;
	//hbase的表前缀
	private String tablePrefix ;

    private String featureBin ;
	
	//hbase配置信息
	private static Configuration conf = null;

    private String imageUrl ;

    private String faceImgUrl ;

	public HBaseQuery(){
		this.quorum = PropertiesHelper.getInstance().getValue("hbase.zookeeper.quorum");
		this.port = Integer.parseInt(PropertiesHelper.getInstance().getValue("hbase.zookeeper.property.clientPort"));
		this.znodeParent = PropertiesHelper.getInstance().getValue("zookeeper.znode.parent");
		this.tablePrefix = PropertiesHelper.getInstance().getValue("hbase.table.prefix");
        this.featureBin =  PropertiesHelper.getInstance().getValue("feature_bin");
		this.imageUrl = PropertiesHelper.getInstance().getValue("feature.image.url");
        this.faceImgUrl = PropertiesHelper.getInstance().getValue("face.image.url");
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", this.quorum);
		conf.set("hbase.zookeeper.property.clientPort", this.port+"");
		conf.set("zookeeper.znode.parent", "/" +this.znodeParent);
	}


    private List<Float> getFeatureBin(String feature){
        List<Float> datas = new ArrayList<Float>();
        String[] features =feature.split(",");
        for(String str : features){
            Float bin = Float.parseFloat(str);
            datas.add(bin);
        }
        return datas;
    }
	/**
	 * 精确匹配查询
	 * @return
	 * @throws Exception 
	 */
	private  List<Record> exactMatch(QueryBean query) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HBaseAdmin hbaseadmin = null;
		try{
    		hbaseadmin = new HBaseAdmin(conf);
			//如果表不存在
			if(!hbaseadmin.tableExists("FSN_VEDIO")){
				LOG.info( "FSN_VIDEO Not Found");
			} else {
                Date start = format.parse(query.getStartTime());
                Date end = format.parse(query.getEndTime());
                String startKey = query.getChannelId() + "_" + start.getTime();
                String stopKey = query.getChannelId() + "_" + end.getTime();
                List<Record> records = selectByRowkeyRange("FSN_VEDIO",startKey,stopKey,query);
                return records;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(hbaseadmin != null ){
				hbaseadmin.close();
			}
		}
        return null;
	}



    /**
     * 根据表名，开始key，结束key查询数据
     * @param tableName
     * @param startRowkey
     * @param endRowkey
     * @return
     * @throws IOException
     */
    public List<Record> selectByRowkeyRange(String tableName, String startRowkey,
                                    String endRowkey,QueryBean query) throws IOException {
        HTableInterface table = null;
        try{
            table = new HTable(conf,tableName);
            Scan scan = new Scan();
            scan.setStartRow(startRowkey.getBytes());
            //设置scan的扫描范围由startRowkey开始
            Filter filter =new InclusiveStopFilter(endRowkey.getBytes());
            scan.setFilter(filter);
            scan.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("c1"));
            //设置scan扫描到endRowkey停止，因为setStopRow是开区间，InclusiveStopFilter设置的是闭区间
            ResultScanner rs = table.getScanner(scan);
            List<Record> records = new ArrayList<Record>();
            int count = 0;
            for (Result r : rs) {
                String value = new String(r.getValue("cf".getBytes(), "c1".getBytes()));
                if(value.contains("|")) {
                    String rowkey = new String(r.getRow());
                    String record = value.substring(0, value.lastIndexOf("|"));
                    String featureBin = value.substring(value.lastIndexOf("|") + 1);
                    //计算特征码
                    Float bin = this.getFeatureBinByCondition(featureBin, query);
                    Record record1 = new Record();
                    record1.setRecordImgUrl(imageUrl + rowkey);
                    record1.setRecordText(record);
                    record1.setFeatureBin(bin);
                    record1.setRowkey(rowkey);
                    records.add(record1);
                }
            }
            LOG.info("table ["+ tableName +"] query record count :" + count);
            return records;
        } catch (Exception e) {
            LOG.info("table not found");
            e.printStackTrace();
        }	finally{
            if(table != null){
                table.close();
            }
        }
        return null;
    }

    /**
     * 根据Rowkey获取该rowkey对应的图片信息
     * @param rowkey
     * @return
     */
    public byte[] getFeatureImgByRowkey(String rowkey,String tableName,String colName){
        HTableInterface table = null;
        try {
            table = new HTable(conf, tableName);
            Get get = new Get(Bytes.toBytes(rowkey));
            get.addColumn(Bytes.toBytes("cf"), Bytes.toBytes(colName));
            Result res = table.get(get);
            byte[] val = res.getValue(Bytes.toBytes("cf"),Bytes.toBytes(colName));
            return val;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算特征码的值
     * @param featureBin
     * @param query
     * @return
     */
    private float getFeatureBinByCondition(String featureBin,QueryBean query){
        List<Float> aList = this.getFeatureBin(featureBin);
        List<Float> bList = this.getFeatureBin(query.getFeatureBin());
        List<Float> rankList = this.getFeatureBin(this.featureBin);
        float sum = 0;
        for (int i = 0; i < aList.size(); i++) {
            float aValue = aList.get(i);
            float bValue = bList.get(i);
            float rankValue = rankList.get(i);
            float poor = aValue - bValue;
            float value = Math.abs(poor) * rankValue;
            sum += -value;
        }
        return sum;
    }

	/**
	 * 根据条件查询数据
	 * @param query 查询对象
	 * @return
	 */
	public  List<Record> query(QueryBean query) {
		//String gzh = query.getGzh();
		try{
		    return exactMatch(query);
		}catch(Exception e){
			e.printStackTrace();
		}
        return null;
	}



    /**
     * 根据已知图片的摄像头通道编号及精确的时间点，查询该记录前后几秒的相关数据信息
     * @param query
     * @return
     */
    public List<Record> relationQuery(QueryBean query){
        try{
            return relactionMatch(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    private  List<Record> relactionMatch(QueryBean query) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HBaseAdmin hbaseadmin = null;
        try{
            hbaseadmin = new HBaseAdmin(conf);
            //如果表不存在
            if(!hbaseadmin.tableExists("FSN_VEDIO")){
                LOG.info( "FSN_VIDEO Not Found");
            } else {
                Date time = format.parse(query.getTime());
                long start = time.getTime() - query.getInterval() * 1000;
                long end = time.getTime() + query.getInterval() * 1000;
                String startKey = query.getChannelId() + "_" + start;
                String stopKey = query.getChannelId() + "_" + end + "1";
                List<Record> records = selectByRowkeyRange("FSN_VEDIO",startKey,stopKey,query);
                return records;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(hbaseadmin != null ){
                hbaseadmin.close();
            }
        }
        return null;
    }


    public byte[] queryImg(String rowkey) {
        try{
            return this.getFeatureImgByRowkey(rowkey,"FSN_VEDIO","c2");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 查询人脸数据
     * @param query
     * @return
     */
    public List<FaceRecord> queryFace(FaceQuery query){
        try{
            return exactFaceMatch(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 人脸精确匹配查询
     * @return
     * @throws Exception
     */
    private  List<FaceRecord> exactFaceMatch(FaceQuery query) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HBaseAdmin hbaseadmin = null;
        try{
            hbaseadmin = new HBaseAdmin(conf);
            //如果表不存在
            if(!hbaseadmin.tableExists("FSN_FACE")){
                LOG.info( "FSN_FACE Not Found");
            } else {
                Date start = format.parse(query.getStartTime());
                Date end = format.parse(query.getEndTime());
                String startKey = query.getIp() + "_" + query.getPort() + "_" + start.getTime();
                String stopKey =  query.getIp() + "_" + query.getPort() +"_" + end.getTime();
                List<FaceRecord> records = queryByRowkeyRange("FSN_FACE", startKey, stopKey);
                return records;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(hbaseadmin != null ){
                hbaseadmin.close();
            }
        }
        return null;
    }


    /**
     * 根据表名，开始key，结束key查询数据
     * @param tableName
     * @param startRowkey
     * @param endRowkey
     * @return
     * @throws IOException
     */
    public List<FaceRecord> queryByRowkeyRange(String tableName, String startRowkey,
                                            String endRowkey) throws IOException {
        HTableInterface table = null;
        try{
            table = new HTable(conf,tableName);
            Scan scan = new Scan();
            scan.setStartRow(startRowkey.getBytes());
            //设置scan的扫描范围由startRowkey开始
            Filter filter =new InclusiveStopFilter(endRowkey.getBytes());
            scan.setFilter(filter);
            scan.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("c1"));
            //设置scan扫描到endRowkey停止，因为setStopRow是开区间，InclusiveStopFilter设置的是闭区间
            ResultScanner rs = table.getScanner(scan);
            List<FaceRecord> records = new ArrayList<FaceRecord>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count = 0;
            for (Result r : rs) {
                String rowkey = new String(r.getRow());
                if(rowkey.contains("_")) {
                    String[] datas = rowkey.split("_");
                    FaceRecord record = new FaceRecord();
                    record.setFaceImgUrl(this.faceImgUrl + rowkey);
                    record.setIp(datas[0]);
                    record.setPort(datas[1]);
                    long time = Long.parseLong(datas[2]);
                    Date date = new Date();
                    date.setTime(time);
                    record.setTime(format.format(date));
                    record.setPersonId(datas[3]);
                    records.add(record);
                }
            }
            LOG.info("table ["+ tableName +"] query record count :" + count);
            return records;
        } catch (Exception e) {
            LOG.info("table not found");
            e.printStackTrace();
        }	finally{
            if(table != null){
                table.close();
            }
        }
        return null;
    }


    /**
     * 查询人脸图片信息
     * @param rowkey
     * @return
     */
    public byte[] queryFaceImg(String rowkey) {
        try{
            return this.getFeatureImgByRowkey(rowkey,"FSN_FACE","c1");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 根据rowkey获取相关的记录
     * @param rowkey
     * @return
     */
    public Record getRecord(String rowkey,String type){
        try{
            byte[] record = null;
            if(type.equals("1")) {
                record = this.getFeatureImgByRowkey(rowkey, "FSN_VEDIO", "c1");
            } else {
                record = this.getFeatureImgByRowkey(rowkey, "FSN_FACE", "c2");
            }
            String value = new String(record);
            Record record1 = new Record();
            if(value.contains("|")) {
                value = value.substring(0, value.lastIndexOf("|"));
                record1.setRecordImgUrl(imageUrl + rowkey);
                record1.setRecordText(value);
                record1.setRowkey(rowkey);
             }
            return record1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
