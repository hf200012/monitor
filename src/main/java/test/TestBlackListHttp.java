package test;

import java.io.*;

/**
 * Created by zhangfeng on 2014/10/31.
 */
public class TestBlackListHttp {

    public static void main(String args[]){
        //192.168.9.12_9876_1416366866894_29
        //172.3.6.2_3652_1416453486_0
        //String json = "{\"channelId\": \"192.168.9.14_9087\",\"type\": \"2\",\"blackListNo\": \"222\",\"warnValue\": \"0.26\",\"startTime\": \"2014-11-26 12:30:00\",\"endTime\": \"2014-11-26 12:59:00\"}";
        //String json = "{\"channelId\": \"172.16.5.50_0\",\"type\": \"2\",\"blackListNo\": \"1175581843\",\"warnValue\": \"0.3\",\"startTime\": \"2014-11-27 14:50:00\",\"endTime\": \"2014-11-27 20:29:42\"}";
        //String json = "{\"channelId\": \"172.16.5.50_0\",\"type\": \"2\",\"blackListNo\": \"1175581843\",\"warnValue\": \"0.0\",\"startTime\": \"2014-11-27 15:50:00\",\"endTime\": \"2015-12-08 13:20:00\"}";

//        String json = "{\"channelId\": \"172.16.5.50_9,172.16.5.50_1\",\"type\": \"1\",\"blackListAndWarnValue\": \"123:0.12,234:0.34\",\"startTime\": \"2014-12-10 15:50:00\",\"endTime\": \"2014-12-108 17:20:00\"}";
//        String json = "{\"channelId\": \"192.108.16.142_5,192.168.9.14_2,192.168.9.14_3,192.168.9.14_4\",\"type\":\"1\",\"blackListAndWarnValue\":\"123:0.0\",\"startTime\": \"2015-01-12 08:02:58\",\"endTime\": \"2015-01-12 14:00:03\"}";

        String json = "{\"channelId\": \"192.143.154.153_7,192.143.24.103_3,192.144.194.210_4\",\"type\":\"1\",\"blackListAndWarnValue\":\"123:0.0\",\"startTime\": \"2015-01-12 08:02:58\",\"endTime\": \"2015-01-13 14:00:03\"}";


        String url = "http://localhost:8080/monitor/rest/blacklist/query";

           for(int i = 0; i < 10 ; i ++){
               Thread thread = new Thread(new PostThread(url,json));
               thread.start();
           }

    }

}
