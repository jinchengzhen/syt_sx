package com.school.scoreManage.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.school.scoreManage.constant.DictLoader;
import com.school.scoreManage.util.Config;

import util.MySQLConnection;



public class ZookeeperRegListener implements ServletContextListener{
	private static Logger logger = Logger.getLogger(ZookeeperRegListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
	@Override
	public void contextInitialized(ServletContextEvent event){
		String realPath = event.getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"classes"+File.separator+"log4j.properties";
		PropertyConfigurator.configure(realPath);
		//读取自定义配置文件目录集合
		String configRealPath = event.getServletContext().getRealPath("/")+"WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"conf.properties";
		//增加缓存map作为配置临时存储位置
		Config.init(configRealPath);
		logger.info("初始化mysql数据库连接信息......");
		MySQLConnection.init(Config.getValue("mysqlIP"), Config.getValue("mysqlUserName"), Config.getValue("mysqlPassWord"), Config.getValue("mysqlDBName_analysis"));
		logger.info("初始化加载字典数据信息......");
		DictLoader.init();
	}
	protected void deleteImg(String filePaths) {
		String filePath=filePaths+"imgCar/temp";
		File file=new File(filePath);
		if(file.exists()){//判断文件夹是否存在
			File[] files=file.listFiles();
			int len = files.length;
			if(len > 0){
				for(int i=0;i<len;i++){
					files[i].delete();//删除temp文件夹所有图片
				}
			}
		}
	}
}
