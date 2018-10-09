package com.announce.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.announce.service.NoticeService;

import util.MySQLConnection;

import dao.impl.MySQLOperationBySQLImpl;

public class GenerateData {
	private static final String[] namearray = {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张"};
	private static final String str = "新,百,家,姓,是,指,某,些,机,构,研,究,的,消,息,被,一,些,媒,体,广,泛,传,播,王,李,张,刘,陈,杨,黄,赵,周,吴,徐,孙,马,胡,朱,郭,何,罗,高,林,为,全,国,前,大";
	private static long keynum;
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	/**
	 * @author jincz
	 * @className main
	 * @description
	 * @param args
	 * 
	 * @date 2018-9-30 上午10:24:32
	 */
	public static void main(String[] args) {
		MySQLConnection.init("127.0.0.1","root", "123456", "qby");
		keynum = selectlastnum();
		// TODO Auto-generated method stub
//		System.out.println(GenerateData.createName());
//		System.out.println(GenerateData.createStr(10));
//		System.out.println(GenerateData.createNumber(10));
		System.out.println(df.format(new Date()));
		for(int i=0;i<50;i++){
			createData(10000);
		}
		System.out.println(df.format(new Date()));
	}
	private static long selectlastnum() {
		MySQLConnection.init("127.0.0.1","root", "123456", "qby");
		String sql = "SELECT  BH FROM t_it_notice order by BH desc LIMIT 1";
		List<Object[]> list = new MySQLOperationBySQLImpl().queryInformation(sql);
		if(!list.isEmpty()){
			String lastnum = list.get(0)[0].toString();
			return (Integer.parseInt(lastnum)+1);
		}
		return -1;
	}
	public static String createStr(int n){
		String[] array = str.split(",");
		StringBuffer br = new StringBuffer();
		for(int i=0;i<n;i++){
			int length = array.length-1;
			int num = (int) Math.floor(Math.random()*length);
			br.append(array[num]);
		}
		return br.toString();
	}
	public static String createName(){
		String[] array = str.split(",");
		StringBuffer br = new StringBuffer();
		int first = (int) Math.floor(Math.random()*namearray.length);
		br.append(namearray[first]);
		int n = 1+(int)Math.round(Math.random());
		for(int i=0;i<n;i++){
			int length = array.length-1;
			int num = (int) Math.floor(Math.random()*length);
			br.append(array[num]);
		}
		return br.toString();
	}
	public static String createNumber(int n){
		int length = (int)Math.pow(10, n);
		int num = length+(int) Math.floor(Math.random()*length);
		return ""+num;
	}
	//插入数据库
	public static int createData(int n){
		int k = 0;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i=0;i<n;i++){
			Map<String,String> map = new HashMap<String, String>();
			String bh = ""+keynum++;
			map.put("BH", bh);
			String fbrid = createNumber(6);
			map.put("FBR", fbrid);
			String fbrxm = createName();
			map.put("FBRXM", fbrxm);
			String fbrdw = createStr(5);
			map.put("FBRDW", fbrdw);
			String ggnr = createStr(10);
			map.put("GGNR", ggnr);
			String fbsj = createDate(k++);
			map.put("FBSJ", fbsj);
			list.add(map);
		}
		List<String> sqls = SQLservice.add("t_it_notice", list);
		return new MySQLOperationBySQLImpl().addInformation(sqls);
	}
	//获取日期时间
//	calendar.add(Calendar.YEAR, -1);//当前时间减去一年，即一年前的时间    
//	calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间    
//	calendar.getTime();//获取一年前的时间，或者一个月前的时间    
	public static String createDate(int n){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		n = n%365;
		calendar.add(Calendar.DAY_OF_YEAR, -n);
		String datestr = df.format(calendar.getTime());
		return datestr;
	}
}
