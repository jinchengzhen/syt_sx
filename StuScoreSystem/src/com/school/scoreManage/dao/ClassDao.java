package com.school.scoreManage.dao;

import java.util.ArrayList;
import java.util.List;

import com.school.scoreManage.constant.DictLoader;

import dao.impl.MySQLOperationBySQLImpl;

public class ClassDao {
	//查用户所选课程名称（查任课教师）
	public List<String> selectClass(String sql){
		List<String> result=new ArrayList<String>();
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			for(Object [] obj:list){
				result.add(obj[0].toString());
			}
		}
		return result;
	}
	//添加课程
	public int addClasses(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().addInformation(sqls);
		return result;
	}
	//删除课程
	public int deleteClasses(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().deleteInformation(sqls);
		return result;
	}
}
