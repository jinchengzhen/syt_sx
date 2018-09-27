package com.school.scoreManage.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import dao.impl.MySQLOperationBySQLImpl;

	public class ScoreDao {
	//查询单人多门成绩、查多人单门成绩
	public Map<String,Double> selectScore(String sql){
		Map<String,Double> result=new LinkedHashMap<String,Double>();
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				if(list.get(i)[1]==null) {
					result.put(list.get(i)[0].toString(),null);
				}else {
					result.put(list.get(i)[0].toString(),Double.parseDouble(list.get(i)[1].toString()));
				}
			}
		}
		return result;
	}
	//修改、录入成绩
	public int updateScore(List<String> sqls) {
		int result = new MySQLOperationBySQLImpl().updateInformation(sqls);
		return result;
	}
}
