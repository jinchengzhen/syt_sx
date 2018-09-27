package com.school.scoreManage.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取配置文件 
 */
public class Config {
	/**
	 * @FieldName logger
	 * @author yangpf
	 * @description 记录日志信息
	 * @date 2017-03-21 12:13:43 AM
	 */
	private static final Logger logger = Logger.getLogger(Config.class);
	/**
	 * @FieldName configMap
	 * @author yangpf
	 * @description 记录properties配置文件内容
	 * @date 2017-03-21 12:13:43 AM
	 */
	private static Map<String, Object> configMap = new HashMap<String, Object>();
	/**
	 * @MethodName release
	 * @author yangpf
	 * @description 销毁配置存储信息
	 * @date 2017-03-21 12:13:43 AM
	 */
	public static void release() {
		logger.info("正在销毁配置存储信息.....");
		configMap.clear();
	}
	
	public static void addConfig(String key,String value){
		configMap.put(key, value);
	}
	
	
	/**
	 * @MethodName init
	 * @author yangpf
	 * @description 增加配置存储信息
	 * @date 2017-03-21 12:13:43 AM
	 */
	public static void init(String ... files) {
		//第一步，初始化Properties对象
		Properties conFig = new Properties();
		//第二步，初始化InputStream对象
		InputStream inputStream = null;
		for(String file :files){
			try {
				//第三步，读取配置文件，转换成流对象
				inputStream = new FileInputStream(file);
				//第四步，Properties对象加载流对象信息
				conFig.load(inputStream);
				//第五步，判断是否为空对象
				if (conFig.isEmpty()) {
					logger.info("增加配置存储信息失败，原因为："+file+"文件中没有数据");
				} else {
					Iterator<Map.Entry<Object, Object>> it = conFig.entrySet()
							.iterator();
					while (it.hasNext()) {
						Map.Entry<Object, Object> entry = it.next();
						Object key = entry.getKey();
						Object value = entry.getValue();
						configMap.put(key.toString(), value.toString());
					}
					logger.info("增加配置存储信息成功");
				}
			} catch (Exception e) {
				logger.error("增加配置存储信息失败，原因为:"+e.getMessage());
			} finally {
				//第六步，关闭流对象信息
				try {
					if(inputStream != null){
						inputStream.close();
					}
				} catch (IOException e) {
					logger.error("关闭流对象信息失败，原因为:"+e.getMessage());
				}
			}
		}
	}

	/**
	 * @MethodName getValue
	 * @author yangpf
	 * @description 读取以${key}为键的值，返回为String类型
	 * @date 2017-03-21 12:30:47 AM
	 * @param key
	 * @return String 类型数据
	 */
	public static String getValue(final String key) {
		if(key != null && !key.equals("")){
			Object object = configMap.get(key);
			final String result = object == null ?"":object.toString().trim();
			return result;
		}
		return "";
	}
	/**
	 * @MethodName getValue
	 * @author yangpf
	 * @description 读取以${key}为键的值，返回为String类型
	 * @date 2017-03-21 12:30:47 AM
	 * @param key
	 * @return String 类型数据
	 */
	public static Integer getValueInt(final String key) {
		if(key != null && !key.equals("")){
			Object object = configMap.get(key);
			final Integer result = object == null ?0:Integer.parseInt(object.toString().trim());
			return result;
		}
		return 0;
	}
}
