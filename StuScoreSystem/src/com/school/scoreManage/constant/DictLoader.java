package com.school.scoreManage.constant;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.school.scoreManage.util.Config;

import dao.impl.MySQLOperationBySQLImpl;


/**
 * @author zms
 * @ClassName DictLoader.java
 * @description 字典类
 * @date July 02, 2018 10:13:49 PM
 */
public class DictLoader {
	private static Logger log = Logger.getLogger(DictLoader.class);
	
	public static Map<String,String> users = null;  //人员ID转人员name
	public static Map<String,String> classes = null;  //课程ID转课程name
	public static Map<String,String> classToID = null;  //课程name转课程ID
	public static Map<String,String> sexToName = null;  //性别
	public static Map<String,String> typeToName = null;  //人员类型
	public static Map<String,List<String>> classtea = null;//任课教师
	public static List<String> gradeID = null;//班级ID
	


	public static void init(){
		Timer timer = new Timer();
		timer.schedule(new MyTimerTask(), new Date(), 12*60*60*1000);
	}
	
	public static void getDictInfo() {
		if(Integer.parseInt(Config.getValue("flag")) == 0){
			//mysql
			//人员类型、性别
			sexToName = new HashMap<String, String>();
			sexToName.put("1", "男");
			sexToName.put("0", "女");
			typeToName = new HashMap<String, String>();
			typeToName.put("0", "管理员");
			typeToName.put("1", "学生");
			typeToName.put("2", "教师");
			//人员ID,name
			users = new HashMap<String, String>();
			List<Object[]> result1 = new MySQLOperationBySQLImpl().queryInformation(Constant.SQL.SQL_user);
			if(result1!=null && result1.size()>0){
				for (Object[] obj : result1) {
					users.put(obj[0].toString(), obj[1].toString());
				}
				log.info("人员信息 加载成功 !");
			}
			
			//课程ID,课程name
			classes = new HashMap<String, String>();
			classToID = new HashMap<String, String>();
			List<Object[]> result2 = new MySQLOperationBySQLImpl().queryInformation(Constant.SQL.SQL_class);
			if(result2!=null && result2.size()>0){
				for (Object[] obj : result2) {
					classes.put(obj[0].toString(), obj[1].toString());
					classToID.put(obj[1].toString(), obj[0].toString());
				}
				log.info("课程信息 加载成功 !");
			}
			//获取任课教师id
			classtea = new HashMap<String, List<String>>();
			if(!classToID.isEmpty()) {
				for(String className:classToID.keySet()) {
					List<String> tealist = new ArrayList<String>();
					List<Object[]> result3 = new MySQLOperationBySQLImpl().queryInformation(Constant.SQL.SQL_classtea(className));
					if(result3!=null && result3.size()>0){
						for (Object[] obj : result3) {
							tealist.add(obj[0].toString());
						}
						classtea.put(className, tealist);
					}
				}
				log.info("任课教师信息 加载成功 !");
//				System.out.println("任课教师信息 加载成功 !");
			}
			//获取所有班级id
			gradeID = new ArrayList<String>();
			List<Object[]> result4 = new MySQLOperationBySQLImpl().queryInformation(Constant.SQL.SQL_grade);
			if(result4!=null && result4.size()>0){
				for (Object[] obj : result4) {
					gradeID.add(obj[0].toString());
				}
				log.info("班级信息 加载成功 !");
			}
		}else if(Integer.parseInt(Config.getValue("flag")) == 1){
			//oracle
		}
	}
	
	/**
	 * @author jcz
	 * @ClassName userID2Name
	 * @description 用户id转名称
	 * @param   用户id
	 * @return 用户名
	 * 
	 */
	public static String userID2Name(String userid){
		try{
			String str = users.get(userid);
			if(str == null){
				return "";
			}else{
				return str;
			}
		}catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * @author jcz
	 * @ClassName classIDoName
	 * @description 课程id转名称
	 * @param  课程id  
	 * @return 课程名称
	 */
	public static String classIDoName(String classID){
		try{
			String str = classes.get(classID);;
			if(str == null){
				return "";
			}else{
				return str;
			}
		}catch (Exception e) {
			return "";
		}
	}
	/**
	 * @author jcz 
	 * @ClassName classIDoName
	 * @description 课程name转ID
	 * @param 课程name  
	 * @return 课程ID
	 */
	public static String classNameToID(String className){
		try{
			String str = classToID.get(className);;
			if(str == null){
				return "";
			}else{
				return str;
			}
		}catch (Exception e) {
			return "";
		}
	}
	/**
	 * @author jcz 
	 * @ClassName classForteacher
	 * @description 课程任课教师
	 * @param 课程name  
	 * @return List<任课教师>
	 */
	public static List<String> classForteacher(String className){
		try{
			List<String> res = classtea.get(className);;
			if(res == null){
				return null;
			}else{
				return res;
			}
		}catch (Exception e) {
			return null;
		}
	}
	/**
	 * @author jcz 
	 * @ClassName classForteacher
	 * @description 人员类型id转名称
	 */
	public static String type2Name(String typeid){
		try{
			String str = typeToName.get(typeid);;
			if(str == null){
				return "";
			}else{
				return str;
			}
		}catch (Exception e) {
			return "";
		}
	}
	/**
	 * @author jcz 
	 * @ClassName classForteacher
	 * @description 课程任课教师
	 * @param 课程name  
	 * @return List<任课教师>
	 */
	public static String sex2Name(String sex){
		try{
			String str = sexToName.get(sex);;
			if(str == null){
				return "";
			}else{
				return str;
			}
		}catch (Exception e) {
			return "";
		}
	}
	/**
	 * @author jcz 
	 * @ClassName Teacherlist
	 * @description 所有课程任课教师
	 * @return map<课程，List<任课教师>>
	 */
	public static Map<String, List<String>> getClasstea() {
		return classtea;
	}
	/**
	 * 获取所有班级
	 */
	public static List<String> getGradeID() {
		return gradeID;
	}
	
}
class MyTimerTask extends TimerTask{
	private static Logger log = Logger.getLogger(MyTimerTask.class);
	@Override
	public void run() {
		// 执行后，每1小时重复一次
		log.info("字典正在更新中.......");
		DictLoader.getDictInfo();
		log.info("字典正在更新完成！！！！！");
	}
}
