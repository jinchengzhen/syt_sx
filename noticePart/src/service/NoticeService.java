package com.announce.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.announce.bean.Notice;
import com.announce.dao.NoticeDao;

public class NoticeService {
	private NoticeDao dao = new NoticeDao();
	public JSONArray select(Map<String,String> map){
		JSONArray array = new JSONArray();
		String sql = "";
		if(map==null){
			sql += "SELECT BH,FBRXM,GGNR,FBSJ,ZT  FROM t_it_notice Where FBSJ>=(Select FBSJ From t_it_notice order by FBSJ LIMIT 1) LIMIT 8";
		}else{
			sql += CreatSQL.select(map);
		}
		List<Notice> list = dao.select(sql);
		if(!list.isEmpty()){
			for(Notice notice:list){
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("BH",notice.getBH() );
				jsonobj.put("FBR",notice.getFBR() );
				jsonobj.put("FBRXM",notice.getFBRXM());//✔
				jsonobj.put("FBRDW",notice.getFBRDW() );
				jsonobj.put("GGNR",notice.getGGNR() );//✔
				jsonobj.put("FBSJ",notice.getFBSJ() );//✔
				jsonobj.put("ZT",notice.getZT() );//✔
				array.add(jsonobj);
			}
		}
		return array;
	}
	public JSONArray selectAlert(String datestr) {
		JSONArray array = new JSONArray();
		String sql = CreatSQL.selectAlert(datestr);
		List<String> list = dao.selectAlert(sql);
		if(!list.isEmpty()){
			for(String s : list){
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("GGNR",s );
				array.add(jsonobj);
			}
		}
		return array;
	}
	public int count(Map<String, String> map) {
		String sql = CreatSQL.count(map);
		return dao.count(sql);
	}
	public boolean delete(String bh) {
		String sql = CreatSQL.delete(bh);
		return dao.excute(sql);
	}
	public boolean setType(String bH, String zT) {
		String sql = CreatSQL.setType(bH,zT);
		return dao.excute(sql);
	}
	public boolean add(Notice notice) {
		String sql = CreatSQL.add(notice);
		return dao.excute(sql);
	}
	public String selectlastnum() {
		String sql = CreatSQL.selectlastnum();
		int lastnum = dao.selectlastnum(sql);
		if(lastnum != -1){
			return ""+(lastnum+1);
		}
		return "";
	}
	public JSONArray selectbigdata(Map<String, String> map) {
		JSONArray array = new JSONArray();
		String sql = CreatSQL.selectbigdata(map);
		List<String> bhlist = dao.selectAlert(sql);
		List<Notice> list = dao.select(CreatSQL.selectlimitAll(bhlist));
		if(!list.isEmpty()){
			for(Notice notice:list){
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("BH",notice.getBH() );
				jsonobj.put("FBR",notice.getFBR() );
				jsonobj.put("FBRXM",notice.getFBRXM());//✔
				jsonobj.put("FBRDW",notice.getFBRDW() );
				jsonobj.put("GGNR",notice.getGGNR() );//✔
				jsonobj.put("FBSJ",notice.getFBSJ() );//✔
				jsonobj.put("ZT",notice.getZT() );//✔
				array.add(jsonobj);
			}
		}
		return array;
	}
	
}
