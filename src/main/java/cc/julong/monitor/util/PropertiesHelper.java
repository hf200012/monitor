package cc.julong.monitor.util;


import java.io.IOException;
import java.util.Properties;


/**
 * Properties处理器
 */
public class PropertiesHelper {
	
	private static Properties pro;
	
	private static PropertiesHelper helper;
	

	
	/**
	 * 构造函数
	 * @param is 属性文件输入流
	 * @throws Exception
	 */
	private PropertiesHelper() {
		pro = new Properties();
		try {
			pro.load(this.getClass().getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    /**
     * 获取属性值
     * @param key 指定Key值，获取value
     * @return String 返回属性值
     */
	public String getValue(String key){
		return pro.getProperty(key);
	}
	
	public static PropertiesHelper getInstance(){
		if(helper == null){
			helper = new PropertiesHelper();
		}
		return helper;
	}

	
	public static void main(String args[]){
		String value = PropertiesHelper.getInstance().getValue("hbase.zookeeper.quorum");
		System.out.println(value);
	}
}
