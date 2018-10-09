package com.announce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.announce.bean.Notice;
import com.announce.util.SQLservice;

public class CreatSQL {
	private static String tableA = "t_it_notice";
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
//		map.put("BH", "123");
//		map.put("currpage", "1");
//		map.put("pagesize", "3");
//		System.out.println(CreatSQL.select(map));
	}
	//查询sql
	public static String  select(Map<String,String> map){
		String[] fileds = {"BH","FBRXM","GGNR","FBSJ","ZT"};
		Map<String,String> condition = new HashMap<String, String>();
		String currpagestr = map.get("currpage");
		String pagesizestr = map.get("pagesize");
		String sortway = map.get("sortway");
		if(!map.isEmpty()){
			for(String key:map.keySet()){
				if(!"".equals(map.get(key))&&map.get(key)!=null){
					if("currpage".equals(key)||"pagesize".equals(key)||"sortway".equals(key)){
						
					}else{
						condition.put(key, map.get(key));
					}
				}
			}
		}
		String sql = SQLservice.select(tableA, fileds, condition);
		if(!"".equals(sortway)&&sortway!=null){
			sql += SQLservice.sortlist("FBSJ", sortway);
		}
		if(!"".equals(currpagestr)&&currpagestr!=null&&!"".equals(pagesizestr)&&pagesizestr!=null){
			Integer currpage = Integer.parseInt(currpagestr);
			Integer pagesize = Integer.parseInt(pagesizestr);
			if(currpage>0){
				int first = (currpage-1)*pagesize;
				sql += SQLservice.limitpage(first, pagesize);
			}
		}
		return sql;
	}
	public static String  count(Map<String,String> map){
		String[] fileds = {"count(BH)"};
		Map<String,String> condition = new HashMap<String, String>();
		if(!map.isEmpty()){
			for(String key:map.keySet()){
				if(!"".equals(map.get(key))&&map.get(key)!=null){
					condition.put(key, map.get(key));
				}
			}
		}
		String sql = SQLservice.select(tableA, fileds, condition);
		return sql;
	}
	public static String delete(String bh) {
		String sql = "DELETE FROM "+tableA+" WHERE BH = "+bh;
		return sql;
	}
	public static String setType(String bH, String zT) {
		String sql = "UPDATE "+tableA+" SET ZT="+zT+" WHERE BH = "+bH;
		return sql;
	}
	public static String selectAlert(String datestr) {
		return "SELECT GGNR FROM "+tableA+" WHERE ZT='1' AND FBSJ >= (Select FBSJ From t_it_notice WHERE FBSJ LIKE '"+datestr+"%' LIMIT 1) ORDER BY FBSJ DESC";
	}
	public static String add(Notice notice) {
		String sql = "INSERT INTO "+tableA+" (BH, FBR,FBRXM,FBRDW,GGNR,FBSJ) VALUES ('"+notice.getBH()+"','"+notice.getFBR()+"','"+notice.getFBRXM()+"','"+notice.getFBRDW()+"','"+notice.getGGNR()+"','"+notice.getFBSJ()+"')";
		return sql;
	}
	public static String selectlastnum() {
		String sql = "SELECT  BH FROM t_it_notice order by BH desc LIMIT 1";
		return sql;
	}
	public static String selectbigdata(Map<String, String> map) {
		String currpagestr = map.get("currpage");
		String pagesizestr = map.get("pagesize");
		String FBSJ = map.get("FBSJ");
		String FBRXM = map.get("FBRXM");
		String sql = "Select BH From "+tableA+" WHERE 1=1 ";
		if(!"".equals(FBSJ)&&FBSJ!=null){
			sql += " AND FBSJ ="+FBSJ;
		}
		if(!"".equals(FBRXM)&&FBRXM!=null){
			sql += " AND FBRXM ="+FBRXM;
		}
		int first = 0;
		if(!"".equals(currpagestr)&&currpagestr!=null&&!"".equals(pagesizestr)&&pagesizestr!=null){
			Integer currpage = Integer.parseInt(currpagestr);
			Integer pagesize = Integer.parseInt(pagesizestr);
			if(currpage>0){
				first = (currpage-1)*pagesize;
			}
			sql += "order by FBSJ DESC LIMIT "+first+","+pagesize;
		}
		
		return sql;
	}
	public static String selectlimitAll(List<String> list){
		StringBuffer sql = new StringBuffer();
		if(!list.isEmpty()&&list.size()>1){
			sql.append("SELECT BH,FBRXM,GGNR,FBSJ,ZT FROM t_it_notice WHERE BH = '"+list.get(0)+"'");
			for(int i=1;i<list.size();i++){
				sql.append(" UNION SELECT BH,FBRXM,GGNR,FBSJ,ZT FROM t_it_notice WHERE BH = '"+list.get(i)+"'");
			}
		}
		return sql.toString();
	}
}
