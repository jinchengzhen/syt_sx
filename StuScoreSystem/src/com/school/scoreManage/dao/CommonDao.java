package com.school.scoreManage.dao;

import java.util.List;

import com.school.scoreManage.constant.DictLoader;

import dao.impl.MySQLOperationBySQLImpl;

public class CommonDao {
	//查数据总数
	public Double select(String sql) {
		double res = 0;
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			if(list.get(0)[0]!=null) {
				String countstr = list.get(0)[0].toString();
				res = Double.parseDouble(countstr);
			}
		}
		return res;
	}
	public int add(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().addInformation(sqls);
		return result;
	}
	public int delete(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().deleteInformation(sqls);
		return result;
	}
	public int update(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().updateInformation(sqls);
		return result;
	}
}
