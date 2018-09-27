package com.school.scoreManage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school.scoreManage.bean.Classes;
import com.school.scoreManage.bean.Score;
import com.school.scoreManage.bean.User;
import com.school.scoreManage.dao.ClassDao;
import com.school.scoreManage.dao.CommonDao;
import com.school.scoreManage.dao.ScoreDao;
import com.school.scoreManage.dao.UserDao;
import com.school.scoreManage.util.SQLservice;

public class ToSQLService {
	private UserDao userdao=new UserDao();
	private ClassDao classdao=new ClassDao();
	private ScoreDao scoredao=new ScoreDao();
	private CommonDao commondao=new CommonDao();
	
	private final String table1="user";
	private final String table2="score";
	private final String table3="class";
	
	//查用户信息
	public List<User> selectuser(User user,Integer currpage,Integer pagesize){
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds= {"*"};
		if(user.getUserID()!=null) {
			condition.put("userID",user.getUserID() );
		}
		if(user.getPassword()!=null) {
			condition.put("password",user.getPassword() );
		}
		if(user.getType()!=null) {
			condition.put("type",user.getType() );
		}
		if(user.getGradeID()!=null) {
			condition.put("gradeID",user.getGradeID() );
		}
		if(user.getName()!=null) {
			condition.put("name",user.getName() );
		}
		String sql=SQLservice.select(table1, fileds, condition);
		if(currpage!=null&&pagesize!=null) {
			sql+=SQLservice.limitpage((currpage-1)*pagesize, pagesize);
		}
		return userdao.userInfo(sql);
	}
	//按班级、姓名查user数据总数
	public double selectUsercout(User user) {
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds= {"count(*)"};
		if(user.getUserID()!=null) {
			condition.put("userID",user.getUserID() );
		}
		if(user.getPassword()!=null) {
			condition.put("password",user.getPassword() );
		}
		if(user.getType()!=null) {
			condition.put("type",user.getType() );
		}
		if(user.getGradeID()!=null) {
			condition.put("gradeID",user.getGradeID() );
		}
		if(user.getName()!=null) {
			condition.put("name",user.getName() );
		}
		String sql=SQLservice.select(table1, fileds, condition);
		return commondao.select(sql);
	}
	//按课程查找用户信息
	public List<User> selectuserWithTerm(String className,String type,Integer currpage,Integer pagesize){
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds = {"*"};
		String table = table1+" u, "+table3+" c ";
		condition.put("c.className",className );
		condition.put("u.type",type );
		String ID = "";
		if("1".equals(type)) {
			ID = "c.stuID";
		}else if("2".equals(type)) {
			ID = "c.teaID";
		}
		condition.put("u.userID",ID );
		String sql=SQLservice.select(table, fileds, condition);
		sql += " GROUP BY "+ID+" ";
		if(currpage!=null&&pagesize!=null) {
			sql+=SQLservice.limitpage((currpage-1)*pagesize, pagesize);
		}
		return userdao.userInfo(sql);
	}
	//按课程查user数据总数
	public double class_Usercout(String className,String type) {
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds= {"count(*)"};
		String table = table1+" u, "+table3+" c ";
		condition.put("c.className",className );
		condition.put("u.type",type );
		String ID = "";
		if("1".equals(type)) {
			ID = "c.stuID";
		}else if("2".equals(type)) {
			ID = "c.teaID";
		}
		condition.put("u.userID",ID );
		String sql=SQLservice.select(table, fileds, condition);
		sql += " GROUP BY "+ID+" ";
		return commondao.select(sql);
	}
	//查学生总数（按课程、班级）
	public double class_stucout(String classID,String gradeID) {
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds= {"count(*)"};
		String table = table1+" u, "+table2+" s ";
		condition.put("s.classID",classID );
		condition.put("u.gradeID",gradeID );
		condition.put("u.userID","s.stuID" );
		String sql=SQLservice.select(table, fileds, condition);
		sql += " GROUP BY s.stuID ";
		return commondao.select(sql);
	}
	//修改密码
	public int changekey(User user) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		List<Map<String,String>> filedlist=new ArrayList<Map<String,String>>();
		Map<String,String> condition=new HashMap<String,String>();
		Map<String,String> filed=new HashMap<String,String>(); 
		if(user.getUserID()!=null) {
			condition.put("userID",user.getUserID() );
		}
		if(user.getPassword()!=null) {
			filed.put("password",user.getPassword() );
		}
		filedlist.add(filed);
		conditionlist.add(condition);
		List<String> sqls=SQLservice.update(table1, filedlist, conditionlist);
		return userdao.updatepassword(sqls);
	}
	//添加人员
	public int adduser(List<User> userlist) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		for(User user:userlist) {
			if(user.getUserID()!=null) {
				Map<String,String> condition=new HashMap<String,String>();
				condition.put("userID",user.getUserID() );
				condition.put("password",user.getPassword() );
				condition.put("type",user.getType() );
				condition.put("name",user.getName());
				condition.put("sex",user.getSex() );
				condition.put("birth",user.getBirth() );
				condition.put("phone",user.getPhone() );
				condition.put("gradeID",user.getGradeID() );
				conditionlist.add(condition);
			}
		}
		List<String> sqls=SQLservice.add(table1, conditionlist);
		return userdao.adduser(sqls);
	}
	//删除人员信息
	public int deleteuser(String userID) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		Map<String,String> condition=new HashMap<String,String>();
		condition.put("userID", userID);
		conditionlist.add(condition);
		List<String> sqls=SQLservice.delete(table1, conditionlist);
		return userdao.deleteuser(sqls);
	}
	/**
	 *  table2:score
	 */
	//新增学生成绩数据
	public int add_stu_score(List<Classes> classlist) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		for(Classes cla:classlist) {
				Map<String,String> condition=new HashMap<String,String>();
				condition.put("classID",cla.getClassID());
				condition.put("stuID",cla.getStuID());
				conditionlist.add(condition);
		}
		List<String> sqls=SQLservice.add(table2, conditionlist);
		return commondao.add(sqls);
	}
	//查询单人多门成绩//查多人单门成绩（所有人）
	public Map<String,Double> selectscore(String key,String ID,String sortway,Integer currpage,Integer pagesize){
		String[] fileds= {key,"score"};
		Map<String,String> condition=new HashMap<String,String>();
		if(ID!=null&&!"".equals(ID)) {
			if("stuID".equals(key)) {
				condition.put("classID",ID );
			}else {
				condition.put("stuID",ID );
			}
		}
		String sql=SQLservice.select(table2, fileds, condition);
		if(sortway!=null) {
			sql+=SQLservice.sortlist("score", sortway);
		}
		if(currpage!=null&&pagesize!=null) {
			sql+=SQLservice.limitpage((currpage-1)*pagesize, pagesize);
		}
		Map<String,Double> result=scoredao.selectScore(sql);
		double sum=0;
		for(Double onescore:result.values()) {
			sum += onescore;
		}
		if("classID".equals(key)) {
			result.put("总分", sum);
		}else if("stuID".equals(key)) {
			result.put("平均分", sum/result.size());
		}
		return result;
	}
	//查多人单门成绩（班级）
		public Map<String,Double> selectGradescore(String grade,String ID,String sortway,Integer currpage,Integer pagesize){
			String[] fileds= {"stuID","score"};
			Map<String,String> condition=new HashMap<String,String>();
			if(ID!=null&&!"".equals(ID)) {
				condition.put("classID",ID );
			}
			if(grade!=null&&!"".equals(grade)) {
				condition.put("gradeID",grade );
			}
			condition.put("g.type", "1");
			condition.put("s.stuID", "g.userID");
			String table=table2+" s,"+table1+" g";
			String sql=SQLservice.select(table, fileds, condition);
			if(sortway!=null) {
				sql+=SQLservice.sortlist("s.score",sortway);
			}
			if(currpage!=null&&pagesize!=null) {
				sql+=SQLservice.limitpage((currpage-1)*pagesize, pagesize);
			}
			Map<String,Double> result=scoredao.selectScore(sql);
			if(!result.isEmpty()) {
				double sum=0;
				for(Double onescore:result.values()) {
					if(onescore!=null&&!"".equals(onescore)) {
						sum += onescore;
					}
				}
				result.put("平均分", sum/result.size());
			}
			return result;
		}
	
	//修改、录入成绩
	public int updateScore(List<Score> scores) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		List<Map<String,String>> filedlist=new ArrayList<Map<String,String>>();
		Map<String,String> filed=new HashMap<String,String>();
		if(!scores.isEmpty()) {
			for(Score score:scores) {
				Map<String,String> condition=new HashMap<String,String>();
				if(score.getStuID()!=null) {
					condition.put("stuID",score.getStuID() );
				}
				if(score.getClassID()!=null) {
					condition.put("classID",score.getClassID() );
				}
				if(score.getScore()!=null) {
					filed.put("score",score.getScore() );
				}
				filedlist.add(filed);
				conditionlist.add(condition);
			}
		}
		List<String> sqls=SQLservice.update(table2, filedlist, conditionlist);
		return userdao.updatepassword(sqls);
	}
	/**
	 *  table3:class
	 */
	//查用户所选课程
	public List<String> selectClass(String type,String userID,Integer currpage,Integer pagesize){
		String[] fileds= {"className"};
		Map<String,String> condition=new HashMap<String,String>();
		if(userID!=null&&!"".equals(userID)) {
			if("1".equals(type)) {
				condition.put("stuID",userID );
			}else if("2".equals(type)) {
				condition.put("teaID",userID );
			}
		}
		String sql=SQLservice.select(table3, fileds, condition);
		sql += " GROUP BY className ";
		if(currpage!=null&&pagesize!=null) {
			sql+=SQLservice.limitpage((currpage-1)*pagesize, pagesize);
		}
		return classdao.selectClass(sql);
	}
	//查教师ID
	public String selectTea(String className,String stuID) {
		Map<String,String> condition=new HashMap<String,String>();
		String[] fileds= {"teaID"};
		condition.put("className",className );
		condition.put("stuID",stuID );
		String sql=SQLservice.select(table3, fileds, condition);
		List<String> res = classdao.selectClass(sql);
		if(!res.isEmpty()) {
			return res.get(0);
		}
		return null;
	}
	//添加课程
	public int addclasses(List<Classes> classes) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		 for(Classes cla:classes) {
			 Map<String,String> condition=new HashMap<String,String>();
			 condition.put("classID",cla.getClassID() );
			 condition.put("className",cla.getClassName() );
			 condition.put("stuID",cla.getStuID() );
			 condition.put("teaID",cla.getTeaID() );
			 conditionlist.add(condition);
		 }
		List<String> sqls=SQLservice.add(table3, conditionlist);
		return userdao.adduser(sqls);
	}
	//删除课程（按课程、学生、教师）
	public int deleteClass(List<Classes> classes) {
		List<Map<String,String>> conditionlist=new ArrayList<Map<String,String>>();
		for(Classes cla:classes) {
			Map<String,String> condition=new HashMap<String,String>();
			if(cla.getClassID()!=null) {
				condition.put("className", cla.getClassName());
			}
			if(cla.getStuID()!=null) {
				condition.put("stuID", cla.getStuID());
			}
			if(cla.getTeaID()!=null) {
				condition.put("teaID", cla.getTeaID());
			}
			conditionlist.add(condition);
		}
		List<String> sqls=SQLservice.delete(table3, conditionlist);
		return userdao.deleteuser(sqls);
	}
	/**
	 *  table4:grade
	 */
}
