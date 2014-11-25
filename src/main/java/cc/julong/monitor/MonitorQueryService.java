package cc.julong.monitor;

import cc.julong.monitor.bean.*;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 监控视频数据查询Rest API接口
 *
 * Created by zhangfeng on 2014/10/22.
 */
@Path("/video")
public class MonitorQueryService {

    @Context
    private ServletContext servletContext;

    @POST
    @Path("/query")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RecordBean query(QueryBean queryBean){
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        List<Record> records = query.query(queryBean);
        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        return bean;
    }

    @POST
    @Path("/queryBlackList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RecordBean queryBlackList(QueryBean queryBean){
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        List<Record> records = query.query(queryBean);
        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        return bean;
    }


    @POST
    @Path("/relationQuery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RecordBean relationQuery(QueryBean queryBean){
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        List<Record> records = query.relationQuery(queryBean);
        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        return bean;
    }

    @GET
    @Path("/queryImg/{rowkey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] queryImg(@PathParam("rowkey") String rowkey) {
        Query query = new HBaseQuery();
        byte [] b = query.queryImg(rowkey);
        if(b != null){
            return b;
        }
        return new byte[0];
    }

    @GET
    @Path("/queryImg/{rowkey}")
    @Produces("image/jpg")
    public Response queryImg1(@PathParam("rowkey") String rowkey) {
        Query query = new HBaseQuery();
        byte [] b = query.queryImg(rowkey);
        if(b != null){
            return Response.ok(new ByteArrayInputStream(b)).build();
        }
        return null;
    }

    @POST
    @Path("/queryFace")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FaceResponse queryFace(FaceQuery queryBean){
        FaceResponse bean = new FaceResponse();
        Query query = new HBaseQuery();
        List<FaceRecord> records = query.queryFace(queryBean);

        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        return bean;
    }


    @GET
    @Path("/queryFaceImg/{rowkey}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] queryFaceImg(@PathParam("rowkey") String rowkey) {
        Query query = new HBaseQuery();
        byte [] b = query.queryFaceImg(rowkey);
        if(b != null){
            return b;
        }
        return new byte[0];
    }

    @GET
    @Path("/getRecord/{rowkey}/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public RecordBean getRecord(@PathParam("rowkey") String rowkey,@PathParam("type") String type) {
        Query query = new HBaseQuery();
        RecordBean bean = new RecordBean();
        Record record = query.getRecord(rowkey,type);
        List<Record> records = new ArrayList<Record>();
        if(record != null){
            records.add(record);
        }
        bean.setCode(200);
        if( records.size() > 0){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        return bean;
    }

}
