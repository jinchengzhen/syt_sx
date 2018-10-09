package com.announce.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.announce.bean.Notice;
import com.announce.service.NoticeService;
import com.common.common.SuperParentAction;
import com.common.core.LoginConstants;
import com.ehl.frame.common.UserObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




public class AnnounceAction extends SuperParentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	private NoticeService ser = new NoticeService();
	//初始化查询
	public void selectInfo(){
		JSONObject jsonobj = new JSONObject();
		JSONArray array = ser.select(null);
		if(!array.isEmpty()){
			 jsonobj.put("state", 1);
			 jsonobj.put("mag", array);
		 }else{
			 jsonobj.put("state", 0);
			 jsonobj.put("mag", "暂无信息!");
		 }
		 writeToJson(jsonobj.toString());
	}
	//查询置顶公告（查询最近一个月）
	public void selectAlert(){
		JSONObject jsonobj = new JSONObject();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, -30);
		String datestr = df.format(calendar.getTime());
		JSONArray array = ser.selectAlert(datestr.substring(0, 10));
		if(!array.isEmpty()){
			 jsonobj.put("state", 1);
			 jsonobj.put("mag", array);
		 }else{
			 jsonobj.put("state", 0);
			 jsonobj.put("mag", "暂无公告!");
		 }
		 writeToJson(jsonobj.toString());
	}
	//查询（带条件）
	public void selectlimit(){
		JSONObject jsonobj = new JSONObject();
		 String FBRXM = request.getParameter("FBRXM");
		 String FBSJ = request.getParameter("FBSJ");
		 String currpage = request.getParameter("currpage");
		 String pagesize = request.getParameter("pagesize");
		 Map<String,String> map = new HashMap<String, String>();
		 if(!"".equals(FBRXM)&&FBRXM!=null){
			 map.put("FBRXM",FBRXM );
		 }
		 if(!"".equals(FBSJ)&&FBSJ!=null){
			 map.put("FBSJ",FBSJ );
		 }
		 int totaldata = ser.count(map);
		 JSONArray array = new JSONArray();
		 if(totaldata>=0){
			 if(!"".equals(currpage)&&currpage!=null){
				 map.put("currpage",currpage );
			 }
			 if(!"".equals(pagesize)&&pagesize!=null){
				 map.put("pagesize",pagesize );
			 }
			 map.put("sortway", "DESC");
			 if(totaldata<=100000){
				 array = ser.select(map);
			 }else{
				 array = ser.selectbigdata(map);
			 }
		 }
		 if(!array.isEmpty()){
			 jsonobj.put("state", 1);
			 jsonobj.put("mag", array);
			 jsonobj.put("totaldata", totaldata);
		 }else{
			 jsonobj.put("state", 0);
			 jsonobj.put("mag", "暂无信息!");
		 }
		 writeToJson(jsonobj.toString());
	}
	//新增
	public void addInfo(){
		JSONObject jsonobj = new JSONObject();
		Notice notice = new Notice();
		UserObject userObject = (UserObject) session.get(LoginConstants.USER);
		String GGNR = request.getParameter("GGNR");
		String BH = ser.selectlastnum();
		if("".equals(BH)){
			BH = "100000";
		}
		notice.setBH(BH);
		notice.setFBR(userObject.getPersonid());
		notice.setFBRXM(userObject.getUserRealName());
		notice.setFBRDW(userObject.getDepartmentName());
		notice.setGGNR(GGNR);
		notice.setFBSJ(df.format(new Date()));
		if(ser.add(notice)){
			 jsonobj.put("mag", "新增成功!");
		 }else{
			 jsonobj.put("mag", "新增失败!");
		 }
		 writeToJson(jsonobj.toString());
	}
	//删除
	public void deleteInfo(){
		JSONObject jsonobj = new JSONObject();
		 String BH = request.getParameter("BH");
		 if(ser.delete(BH)){
			 jsonobj.put("mag", "删除成功!");
		 }else{
			 jsonobj.put("mag", "删除失败!");
		 }
		 writeToJson(jsonobj.toString());
	}
	//设置置顶
	public void setType(){
		JSONObject jsonobj = new JSONObject();
		 String BH = request.getParameter("BH");
		 String ZT = request.getParameter("ZT");
		 if(ser.setType(BH,ZT)){
			 jsonobj.put("state", 1);
		 }else{
			 jsonobj.put("state", 0);
		 }
		 writeToJson(jsonobj.toString());
	
	}
	
	
}
