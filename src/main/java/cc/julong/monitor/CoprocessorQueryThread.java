package cc.julong.monitor;

import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;
import cc.julong.monitor.generated.HBaseQueryProcess.QueryRequest;
import cc.julong.monitor.generated.HBaseQueryProcess.QueryResponse;
import cc.julong.monitor.generated.HBaseQueryProcess.ServiceQuery;
import com.google.protobuf.ByteString;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.ServerRpcController;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 基于Coprocessor查询操作的线程
 * @author zhangfeng
 *
 */
public class CoprocessorQueryThread implements Runnable {

    private static final Logger LOG = Logger.getLogger(CoprocessorQueryThread.class);

    //查询状态管理器
    private QueryStatusManager manager;

    //要查询的表名，可能是多个，每个表名之间使用英文的逗号隔开，例如:GSN_20140712,FSN_20140809
    private String tableName;

    //查询对象
    private QueryBean query;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //hbase的配置对象
    private Configuration conf ;

    //coprocessor执行回调函数所在的表
    private String tn ;

    private String channelId ;

    public CoprocessorQueryThread(String tn,Configuration conf,QueryStatusManager manager,
                                  QueryBean query,String channelId) {
        this.manager = manager;
        this.query = query;
        this.conf = conf;
        this.tn = tn;
        this.channelId  = channelId;
    }


    public void run() {
        LOG.info( "Thread Id ["+Thread.currentThread().getId()+"] === getChannelId : " + channelId );
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = new Date();
        LOG.info("QueryThread.CoprocessorQueryThread thread ID :[" + Thread.currentThread().getId() + " ] ========= start time : " + format.format(start));

        HTable table = null;
        try {
            table = new HTable(conf, tn);
            long starttime = format.parse(query.getStartTime()).getTime() / 1000;
            long endtime = format.parse(query.getEndTime()).getTime() / 1000;
            //构造查询条件
            final QueryRequest req = QueryRequest.newBuilder()
                    .setStart(starttime).setEnd(endtime)
                    .setChannelId(channelId)
                    .setBlackListAndWarnValue(query.getBlackListAndWarnValue())
                    .setTableName("FSN_VEDIO")
                    .build();
            //执行coprocessor查询
            Map<byte[], ByteString> res = table.coprocessorService(
                    ServiceQuery.class, null, null,
                    new Batch.Call<ServiceQuery, ByteString>() {
                        public ByteString call(ServiceQuery instance)
                                throws IOException {
                            ServerRpcController controller = new ServerRpcController();
                            BlockingRpcCallback<QueryResponse> rpccall = new BlockingRpcCallback<QueryResponse>();
                            //执行查询
                            instance.query(controller, req, rpccall);
                            //获取查询返回的结果
                            QueryResponse resp = rpccall.get();
                            return resp.getRetWord();
                        }
                    });
            //对返回结果去重
            for (ByteString str : res.values()) {
                String results = str.toStringUtf8();
               // System.out.println("==== : " + results);
                if(results != null && !results.equals("")){
                    //多条结果之间server端是使用#分割的，所以这里使用#对返回的结果进行拆分
                    results = results.substring(0,results.lastIndexOf("#"));
                    String[] datas = results.split("#");
                    if (datas != null) {
                        for(String rec : datas){
                            String[] re = rec.split(",");
                            //将数据添加到结果队列中
                            Record record1 = new Record();
                            if (query.getType().equals("1")) {
                                record1.setRecordImgUrl("http://localhost:8080/monitor/rest/video/queryImg/" + re[2]);
                            } else {
                                record1.setRecordImgUrl("http://localhost:8080/monitor/rest/video/queryFaceImg/" + re[2]);
                            }

                            record1.setRecordText(re[0]);
                            record1.setBlackListNo(re[1]);
                            //record1.setFeatureList(featureBin);
                            record1.setRowkey(re[2]);
                            manager.getResults().add(record1);
                        }
                    }
                }
            }
            LOG.info("Thread Id ["+Thread.currentThread().getId()+"] ====== "+ channelId +" =  size : " + manager.getResults().size());
            LOG.info("Thread Id ["+Thread.currentThread().getId()+"] === "+channelId +" = query completed : ");
            Date end = new Date();
            LOG.info("QueryThread.CoprocessorQueryThread thread ID :[" + Thread.currentThread().getId() + " ] ========= start time : " + format.format(end));
            LOG.info("QueryThread.CoprocessorQueryThread thread ID :[" + Thread.currentThread().getId() + " ]  ["+channelId + "] ========= cost time : " +(end.getTime() - start.getTime()) / 1000 +" s");

            //设置这个线程的查询状态为完成
            //manager.setStatus(query.getChannelId(), true);
            manager.getStatus().put(channelId,true);
            for(String key : manager.getStatus().keySet()){
                LOG.info( key + ": " + manager.getStatus().get(key));
            }
        }catch(Exception e){
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }finally{
            if(table != null){
                try {
                    //关闭table对象，防止出现table一直占用rpc连接不释放的问题
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}