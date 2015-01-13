package test;

import cc.julong.monitor.QueryStatusManager;
import cc.julong.monitor.bean.QueryBean;
import cc.julong.monitor.bean.Record;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 精确查询线程
 * @author zhangfeng
 *
 */
public class PostThread implements Runnable {

	private String url ;
	private String json ;

	public PostThread(String url ,String json){
		this.json = json;
		this.url = url;
	}

	public void run() {
		try {
			postRequest(json, url);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 发送请求到http服务然后接收返回报文
	 * @param jsonStr 请求的json格式的字符串
	 * @param path   请求的http服务的路径
	 * @return 返回请求的响应信息
	 * @throws java.io.IOException
	 */
	public static String postRequest(String jsonStr, String path)
			throws IOException {
		// 得到请求报文的二进制数据
		byte[] data = jsonStr.getBytes();
		java.net.URL url = new java.net.URL(path);
		//openConnection() 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接
		java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();// 打开一个连接
		conn.setRequestMethod("POST");// 设置post方式请求
		conn.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒  JDK1.5以上才有此方法
		conn.setReadTimeout(1200 * 1000);// 设置读取超时时间为20秒 JDK1.5以上才有此方法
		// 打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true
		conn.setDoOutput(true);
		// 这里只设置内容类型与内容长度的头字段根据传送内容决定
		// 内容类型Content-Type:
		// application/x-www-form-urlencoded、text/xml;charset=GBK
		// 内容长度Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
		// 把二进制数据写入是输出流
		outStream.write(data);
		// 内存中的数据刷入
		outStream.flush();
		//关闭流
		outStream.close();

		String msg = "";// 保存调用http服务后的响应信息
		// 如果请求响应码是200，则表示成功
		if (conn.getResponseCode() == 200) {
			// HTTP服务端返回的编码是UTF-8,故必须设置为UTF-8,保持编码统一,否则会出现中文乱码
			BufferedReader in = new BufferedReader(new InputStreamReader(
					(InputStream) conn.getInputStream(), "UTF-8"));//返回从此打开的连接读取的输入流
			msg = in.readLine();
			System.out.println("===========: " + msg);
			in.close();
		}
		conn.disconnect();// 断开连接
		return msg;
	}

}
