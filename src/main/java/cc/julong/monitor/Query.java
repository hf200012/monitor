/**
 * 
 */
package cc.julong.monitor;

import cc.julong.monitor.bean.FaceQuery;
import cc.julong.monitor.bean.FaceRecord;
import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;

import java.util.List;

/**
 * HBase查询客户端接口
 * 
 * @author zhangfeng
 *
 */
public interface Query {
	
	/**
	 * 根据条件查询数据
	 * @param query 查询对象
	 * @return
	 */
	public List<Record> query(QueryBean query);

    /**
     * 根据已知图片的摄像头通道编号及精确的时间点，查询该记录前后几秒的相关数据信息
     * @param query
     * @return
     */
    public List<Record> relationQuery(QueryBean query);

    public byte[] queryImg(String rowkey) ;

    /**
     * 查询人脸数据
     * @param query
     * @return
     */
    public List<FaceRecord> queryFace(FaceQuery query);

    /**
     * 查询人脸图片信息
     * @param rowkey
     * @return
     */
    public byte[] queryFaceImg(String rowkey) ;

    /**
     * 根据rowkey获取相关的记录
     * @param rowkey
     * @param type 1:人像，2：人脸
     * @return
     */
    public Record getRecord(String rowkey,String type);
	
}
