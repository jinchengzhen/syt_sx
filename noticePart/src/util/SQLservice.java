package com.announce.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @className SQLservice
 * @author jincz
 * @description 生成sql
 * @date 2018年9月23日下午11:16:13
 */


public class SQLservice {

	//查询SQL
	public  static String select(String table,String[] fileds,Map<String,String> condition) {
		StringBuffer sql=new StringBuffer("SELECT ");
		for(int i=0;i<fileds.length;i++) {
			sql.append(fileds[i]+" ,");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" FROM "+table+" ");
		sql.append(" WHERE ");
		for (Entry<String, String> entry : condition.entrySet()) { 
			if(entry.getValue().contains(".")) {
				sql.append(entry.getKey()+"="+entry.getValue()+" AND ");
			}else {
				sql.append(entry.getKey()+"='"+entry.getValue()+"' AND ");
			}
		}
		sql.delete(sql.length()-6, sql.length());
		return sql.toString();
	}
	//新增SQL
	public static List<String> add(String table,List<Map<String,String>> condition) {
		List<String> sqls= new ArrayList<String>();
		if(!condition.isEmpty()&&condition.size()>0) {
			for(int i=0;i<condition.size();i++) {
				StringBuffer sql=new StringBuffer();
				sql.append("INSERT INTO  "+table+"(");
				for(String key:condition.get(i).keySet()) {
					sql.append(key+" ,");
				}
				sql.replace(sql.length()-1, sql.length(), ")");
				sql.append("VALUES (");
				for(String value:condition.get(i).values()) {
					sql.append("'"+value+"' ,");
				}
				sql.replace(sql.length()-1, sql.length(), ")");
				sqls.add(sql.toString()+"\r\n");
			}
		}
		return sqls;
	}
	//修改SQL
	public static List<String> update(String table,List<Map<String,String>> fileds,List<Map<String,String>> condition) {
		List<String> sqls= new ArrayList<String>();
		if(!condition.isEmpty()&&!fileds.isEmpty()&&fileds.size()==condition.size()) {
			//正则 判断字符串是否为数字
			Pattern pattern = Pattern.compile("\\d[0-9.]+"); 
			for(int i=0;i<fileds.size();i++) {
				StringBuffer sql=new StringBuffer();
				sql.append("UPDATE  "+table+" SET ");
				for (Entry<String, String> entry : fileds.get(i).entrySet()) { 
					Matcher isNum = pattern.matcher(entry.getKey());
					if(isNum.matches() ){
						sql.append(entry.getKey()+"="+entry.getValue()+" ,");
					}else {
						sql.append(entry.getKey()+"='"+entry.getValue()+"' ,");
					}
				}
				sql.delete(sql.length()-1, sql.length());
				sql.append(" WHERE ");
				for (Entry<String, String> entry : condition.get(i).entrySet()) { 
					sql.append(entry.getKey()+"='"+entry.getValue()+"' AND ");
				}
				sql.delete(sql.length()-5, sql.length());
				sqls.add(sql.toString());
			}
		}
		return sqls;
	}
	//删除SQL
	public static List<String> delete(String table,List<Map<String,String>> condition) {
		List<String> sqls= new ArrayList<String>();
		if(!condition.isEmpty()&&condition.size()>0) {
			for(int i=0;i<condition.size();i++) {
				StringBuffer sql=new StringBuffer();
				sql.append("DELETE FROM  "+table+" WHERE ");
				for (Entry<String, String> entry : condition.get(i).entrySet()) { 
					sql.append(entry.getKey()+"='"+entry.getValue()+"' AND ");
				}
				sql.delete(sql.length()-5, sql.length());
				sqls.add(sql.toString());
			}
		}
		return sqls;
	}
	//排序（升序asc、降序desc）
	public static String sortlist(String filed,String flag) {
		return " ORDER BY "+filed+" "+flag;
	}
	//分页limit 1,1;
	public static String limitpage(Integer first,Integer size) {
		return " limit "+first+","+size;
	}
}
