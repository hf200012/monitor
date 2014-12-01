package cc.julong.monitor;

import cc.julong.monitor.bean.*;
import net.sf.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

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
    @Compress
    @Path("/queryBlackList")
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RecordBean queryBlackList(QueryBean queryBean, @Context HttpServletResponse response){
        RecordBean bean = new RecordBean();
        Query query = new HBaseQuery();
        List<Record> records = query.queryBlackList(queryBean);
        bean.setCode(200);
        if(records != null){
            bean.setTotal(records.size());
            bean.setRecords(records);
        } else {
            bean.setTotal(0);
            bean.setRecords(records);
        }
        bean.setMsg("ok");
        String json = JSONObject.fromObject(bean).toString();
        String compressJson = null;
        try {
            compressJson = uncompressToString(json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.print(compressJson);
       // response.setHeader("Content-Encoding","gzip");
        return bean;
    }

    /**
     * 字节数组解压缩后返回字符串
     * @param b
     * @param encoding
     * @return
     */
    public static String uncompressToString(byte[] b, String encoding) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组解压缩后返回字符串
     * @param b
     * @return
     */
    public static String uncompressToString(byte[] b) {
        if (b == null || b.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(b);

        try {
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
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



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Compress
    public String testCompress() {
        return "testtttttttttttttttttttttttttttttttttttttttttttttttttt";
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
