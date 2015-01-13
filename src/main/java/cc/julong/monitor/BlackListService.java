package cc.julong.monitor;

import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;
import cc.julong.monitor.bean.RecordBean;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangfeng on 2014/12/8.
 */
@Path("/blacklist")
public class BlackListService {
    private static final Logger LOG = Logger.getLogger(BlackListService.class);
    @POST
    @Path("/query")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RecordBean query(QueryBean queryBean, @Context HttpServletResponse response){
        //LOG.info("========================= query condition :" + JSONObject.fromObject(queryBean).toString());
        Date start = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        LOG.info("BlackListService.query ========= start time : " + format.format(start));
        List<Record> records = query.queryMutilBlackList(queryBean);
        Date end = new Date();
        LOG.info("BlackListService.query ========= end time : " + format.format(end));
        LOG.info("BlackListService.query ========= cost time : " + (end.getTime() - start.getTime())/1000 +" s");
        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        Date end1 = new Date();
        LOG.info("BlackListService.query ========= return after time : " + format.format(end1));

//        LOG.info("BlackListService.queryFace ========================= return result :" + JSONObject.fromObject(bean).toString());
        return bean;
    }



    @POST
    @Path("/queryTest")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RecordBean queryTest(QueryBean queryBean, @Context HttpServletResponse response){
        LOG.info("========================= query condition :" + JSONObject.fromObject(queryBean).toString());
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        List<Record> records = new ArrayList<Record>();
        for(int i = 0; i < 1000; i ++){
            Record record1 = new Record();

            record1.setRecordImgUrl("http://localhost:8080/monitor/rest/video/queryImg/192.168.9.14_9087_1416977226355_12");

            record1.setRecordText("192.168.9.14|9087|1416977226355|12|23 34 45 56|111:0.23,222:0.54,333:0.345");
            //record1.setFeatureList(featureBin);
            record1.setRowkey("192.168.9.14_9087_1416977226355_12");
            records.add(record1);
        }
        bean.setRecords(records);
        bean.setCode(200);

         bean.setTotal(1000);
//         bean.setRecords(null);

        bean.setMsg("ok");
        LOG.info("BlackListService.queryTest ========================= return result :" + JSONObject.fromObject(bean).toString());
        return bean;
    }


}
