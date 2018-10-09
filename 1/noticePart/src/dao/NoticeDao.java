package com.announce.dao;

import java.util.ArrayList;
import java.util.List;

import com.announce.bean.Notice;

import dao.impl.MySQLOperationBySQLImpl;

public class NoticeDao {
	public boolean excute(String sql){
		return new MySQLOperationBySQLImpl().excute(sql);
	}
	public List<Notice> select(String sql){
		List<Notice> reslist = new ArrayList<Notice>();
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			for(Object[] array:list){
				Notice notice = new Notice();
				notice.setBH(array[0].toString());//BH
				notice.setFBRXM(array[1].toString());//FBRXM
				notice.setGGNR(array[2].toString());//GGNR
				notice.setFBSJ(array[3].toString());//FBSJ
				notice.setZT(array[4].toString());//ZT
				reslist.add(notice);
			}
		}
		return reslist;
	}
	public List<String> selectAlert(String sql){
		List<String> reslist = new ArrayList<String>();
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			for(Object[] array:list){
				reslist.add(array[0].toString());
			}
		}
		return reslist;
	}
	public int count(String sql){
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			String totaldata = list.get(0)[0].toString();
			return Integer.parseInt(totaldata);
		}
		return -1;
	}
	public int selectlastnum(String sql) {
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			String lastnum = list.get(0)[0].toString();
			return Integer.parseInt(lastnum);
		}
		return -1;
	}
}
