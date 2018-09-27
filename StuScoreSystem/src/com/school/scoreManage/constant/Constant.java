package com.school.scoreManage.constant;

import com.school.scoreManage.util.Config;

/**
 * @author zms
 * @ClassName Constant.java
 * @description 字典sql
 * @date July 02, 2018 10:13:49 PM
 */
public interface Constant {
	public static interface SQL{
		 public static String SQL_user="SELECT userID ,name FROM  "+Config.getValue("mysqlDBName_analysis")+".user";
		 public static String SQL_class="SELECT classID ,className FROM  "+Config.getValue("mysqlDBName_analysis")+".class";
		 public static String SQL_grade="SELECT gradeID  FROM  "+Config.getValue("mysqlDBName_analysis")+".user  WHERE type='1' GROUP BY gradeID";
		 public static String SQL_classtea(String className) {
			 return "SELECT teaID  FROM  "+Config.getValue("mysqlDBName_analysis")+".class WHERE className='"+className+"' GROUP BY teaID";
		 }
	}
}
