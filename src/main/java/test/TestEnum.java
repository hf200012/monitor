package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhangfeng on 2014/10/28.
 */
public class TestEnum {
    public enum Light {
        // 利用构造函数传参
        RED (1), GREEN (3), YELLOW (2);

        // 定义私有变量
        private int nCode ;

        // 构造函数，枚举类型只能为私有
        private Light( int _nCode) {
            this . nCode = _nCode;
        }

        @Override
        public String toString() {
            return String.valueOf ( this . nCode );
        }
    }

    public static void main(String args[]){

//        for(int i = 0; i < 20 ; i ++){
//            System.out.println("i = " + i);
//            if(i == 12 ){
//                break;
//            }
//        }
//172.16.5.50_0_1417076922000_0
//        Date date = new Date();
//        date.setTime(1417395712635l);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(format.format(date));

        String test = "sssseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeettttttttttttttttttttttttttttttt";
        String str = "";
        for(int i = 0 ; i < 100; i++){
            str += test;
        }
        System.out.println(str.length());
       byte[] json = null;
        try {
            json = compressToByte(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(json.length);

        try {
            String unjson = uncompressToString(json,"utf-8");
            System.out.println("uncompress length "+ unjson.length());
            System.out.println("uncompress "+ unjson);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*
       * 字节数组解压缩后返回字符串
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

    /*
     * 字节数组解压缩后返回字符串
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

    /*
    * 字符串压缩为字节数组
    */
    public static byte[] compressToByte(String str) throws Exception{
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("utf-8"));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /*
     * 字符串压缩为字节数组
     */
    public static byte[] compressToByte(String str,String encoding){
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }



}
