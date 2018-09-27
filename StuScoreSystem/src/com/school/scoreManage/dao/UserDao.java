package com.school.scoreManage.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school.scoreManage.bean.User;
import com.school.scoreManage.util.SQLservice;

import dao.impl.MySQLOperationBySQLImpl;
import util.MySQLConnection;


public class UserDao {
	public static void main(String[] args) {
		MySQLConnection.init("127.0.0.1","root", "123456", "jin");
		Map<String,String> condition=new HashMap<String,String>();
//		condition.put("userID", "2014110102");
		condition.put("password", "123456");
		String[] table={"user"};
		String[] fileds={"*"};
//		String sql = SQLservice.select(table,fileds,condition);
//		Map<String,String> result=new UserDao().userInfo(sql);
//		System.out.println(result.values().size());
	}
	//查用户信息
	public List<User> userInfo(String sql){
		List<User> result=new ArrayList<User>();
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			for(Object [] obj:list){
				User user=new User();
				user.setUserID(obj[0].toString());
				user.setName(obj[1].toString());
				user.setPassword(obj[2].toString());
				if(obj[3]!=null) {
					user.setGradeID(obj[3].toString());
				}
				user.setType(obj[4].toString());
				user.setSex(obj[5].toString());;
				user.setBirth(obj[6].toString());
				if(obj[7]!=null) {
					user.setPhone(obj[7].toString());
				}
				result.add(user);
			}
		}
		return result;
	}
	//修改密码
	public int updatepassword(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().updateInformation(sqls);
		return result;
	}
	//添加人员
	public int adduser(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().addInformation(sqls);
		return result;
	}
	//删除人员信息
	public int deleteuser(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().deleteInformation(sqls);
		return result;
	}
}
