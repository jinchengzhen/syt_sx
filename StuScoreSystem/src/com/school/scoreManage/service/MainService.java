package com.school.scoreManage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.school.scoreManage.bean.Classes;
import com.school.scoreManage.bean.Score;
import com.school.scoreManage.bean.User;
import com.school.scoreManage.constant.DictLoader;


public class MainService {
	private ToSQLService ser=new ToSQLService();
	/**
	 * 通用操作
	 * @param stuID
	 */
	//登录权限查询
	public int loginAut(User user) {
		//按ID、password查询user表数据
		List<User> list=ser.selectuser(user,null,null);
		if(!list.isEmpty()) {
			return 1;
		}
		return -1;
	}
	//修改密码
	public int changekey(User user) {
		return ser.changekey(user);
	}
	//查询个人信息（用户ID、密码、姓名、性别、出生日期、电话号码、所在班级、人员类型、所选课程）
	public JSONArray personInfo(User user,Integer currpage,Integer pagesize){
		JSONArray array = new JSONArray();
		//查数据总数
		if(pagesize!=null&&currpage!=null) {
			double total = ser.selectUsercout(user);
			JSONObject obj0 = new JSONObject();
			obj0.put("totalpage", Math.ceil(total/pagesize));
			array.add(obj0);
		}
		//基本信息
		List<User> list=ser.selectuser(user,currpage,pagesize);
		if(!list.isEmpty()) {
			for(int i=0;i<list.size();i++) {
				JSONObject obj = new JSONObject();
				obj.put("name", list.get(i).getName());
				obj.put("userID", list.get(i).getUserID());
				obj.put("password", list.get(i).getPassword());
				obj.put("type",list.get(i).getType());
				obj.put("sex", list.get(i).getSex());
				obj.put("birth", list.get(i).getBirth());
				obj.put("phone", list.get(i).getPhone());
				obj.put("gradeID", list.get(i).getGradeID());
				//所选课程
				if(currpage==null&&pagesize==null) {
					List<String> classes = ser.selectClass(list.get(i).getType(),list.get(i).getUserID(),null,null);
					String subject = "";
					for(String cla : classes) {
						subject += cla+";";
					}
					obj.put("classes", subject);
				}
				array.add(obj);
			}
		}
		return array;
	}
	//按课程查个人信息
	public JSONArray userInfoWithTerm(String className,String type,Integer currpage,Integer pagesize) {
		JSONArray array = new JSONArray();
		//查数据总数
		if(pagesize!=null&&currpage!=null) {
			double total = ser.class_Usercout(className, type);
			JSONObject obj0 = new JSONObject();
			obj0.put("totalpage", Math.ceil(total/pagesize));
			array.add(obj0);
		}
		List<User> list = ser.selectuserWithTerm(className, type,currpage,pagesize);
		if(!list.isEmpty()) {
			for(int i=0;i<list.size();i++) {
				JSONObject obj = new JSONObject();
				obj.put("name", list.get(i).getName());
				obj.put("userID", list.get(i).getUserID());
				obj.put("password", list.get(i).getPassword());
				obj.put("type",type);
				obj.put("sex", list.get(i).getSex());
				obj.put("birth", list.get(i).getBirth());
				obj.put("phone", list.get(i).getPhone());
				obj.put("gradeID", list.get(i).getGradeID());
//				List<String> classes = ser.selectClass(list.get(i).getType(),list.get(i).getUserID(),null,null);
//				obj.put("classes", classes);
				array.add(obj);
			}
		}
		return array;
	}
	/**
	 * 对学生页面的操作
	 * @param stuID
	 */
	//查询学生个人成绩
	private JSONObject jsonobj;
	public JSONArray personScore(User user){
		JSONArray array = new JSONArray();
		Map<String, Double> scorelist=ser.selectscore("classID", user.getUserID(),null,null,null);
		for (Entry<String, Double> entry : scorelist.entrySet()) { 
			jsonobj = new JSONObject();
			if(!"总分".equals(entry.getKey())) {
				String className = DictLoader.classIDoName(entry.getKey());
				jsonobj.put("className", className);
			}else {
				jsonobj.put("className", entry.getKey());
			}
			jsonobj.put("score",entry.getValue() );
			array.add(jsonobj);
		}
		return array;
	}
	//查询所选课程的任课教师（姓名、性别、电话）
	public JSONObject selectTeacher(Classes cla) {
		jsonobj=new JSONObject();
		User user = new User();
		String userID = ser.selectTea(cla.getClassName(), cla.getStuID());
		user.setUserID(userID);
		List<User> userInfo = ser.selectuser(user,null,null);
		if(!userInfo.isEmpty()) {
			for(int i=0;i<userInfo.size();i++) {
				jsonobj.put("name", userInfo.get(i).getName());
				jsonobj.put("userID", userInfo.get(i).getUserID());
				jsonobj.put("sex", userInfo.get(i).getSex());
				jsonobj.put("phone", userInfo.get(i).getPhone());
			}
		}
		return jsonobj;
	}
			
	/**
	 * 对教师页面的操作（教师可以查看学生个人成绩）
	 * @param teaID
	 */
	//查单门课所有人的成绩（按班级/所有人）
	public JSONArray selectscore(String className,String grade,String sortway,Integer currpage,Integer pagesize){
		JSONArray array = new JSONArray();
		String classID=DictLoader.classNameToID(className);
		Map<String, Double> scorelist=new HashMap<String, Double>();
		if(pagesize!=null&&currpage!=null) {
			double total = ser.class_stucout(classID, grade);
			JSONObject obj0 = new JSONObject();
			obj0.put("totalpage", Math.ceil(total/pagesize));
			array.add(obj0);
		}
		if(!"".equals(grade)&&grade!=null) {
			//按班级
			scorelist = ser.selectGradescore(grade, classID,sortway,currpage,pagesize);
		}else {
			//所有人
			scorelist = ser.selectscore("stuID", classID,sortway,currpage,pagesize);
		}
		if(!scorelist.isEmpty()) {
			for (Entry<String, Double> entry : scorelist.entrySet()) { 
				jsonobj=new JSONObject();
				if(!"平均分".equals(entry.getKey())) {
					String name = DictLoader.userID2Name(entry.getKey());
					jsonobj.put("stuName", name);
				}
				jsonobj.put("stuID", entry.getKey());
				jsonobj.put("score",entry.getValue() );
				array.add(jsonobj);
			}
		}
		return array;
	}
	//修改、录入成绩
	public int updateScore(List<Score> scores) {
		return ser.updateScore(scores);
	}
	public int addScore(List<Classes> classlist) {
		return ser.add_stu_score(classlist);
	}
	/**
	 * 对管理员页面的操作（管理员可查看教师、学生个人信息）
	 * @param teaID
	 */
	//增加个人信息（用户ID、密码、姓名、性别、出生日期、电话号码、人员类型、所在班级、所选课程）
	public int addUserInfo(List<User> userlist,List<Classes> classlist) {
		if(!userlist.isEmpty()) {
			int resUser = ser.adduser(userlist);
			if(resUser>0) {
				int resCla = ser.addclasses(classlist);
				if(resCla>0) {
					if("1".equals(userlist.get(0).getType())){
						int resSco = ser.add_stu_score(classlist);
						if(resSco>0) {
							return 1;
						}else {
							return -1;
						}
					}
					return 1;
				}
			}
		}
		return -1;
	}
	//删除个人信息
	public int deleteUserInfo(Map<String,String> userInfo) {
		Classes cla = new Classes();
		String type = userInfo.get("type");
		String userID = userInfo.get("userID");
		int resUser = ser.deleteuser(userID);
		if(resUser>0) {
			List<Classes> classes = new ArrayList<Classes>();
			if("1".equals(type)) {
				//学生
				cla.setStuID(userID);
			}else if("2".equals(type)) {
				//教师
				cla.setTeaID(userID);
			}
			classes.add(cla);
			int resCla = ser.deleteClass(classes);
			if(resCla>0) {
				return 1;
			}
		}
		return -1;
	}
	//新增单（多门）门课程
	public int addCla(List<Classes> classes) {
		return ser.addclasses(classes);
	}
	//删除单门选课信息
	public int deleteCla(List<Classes> classes) {
		return ser.deleteClass(classes);
	}
	//获取所有课程及课程任课教师列表
	public JSONArray classtealist(String type) {
		Map<String,List<String>> map = DictLoader.getClasstea();
		JSONArray array = new JSONArray();
		for (Entry<String, List<String>> entry : map.entrySet()) { 
			//学生
			if("1".equals(type)) {
				//添加父节点-class
				JSONObject object0 = new JSONObject();
				String classID = DictLoader.classNameToID(entry.getKey());
				object0.put("id", classID);
				object0.put("pId", 1);
				object0.put("name", entry.getKey());
				object0.put("open", false);
				object0.put("chkDisabled", true);
				array.add(object0);
				//添加子节点-教师
				for(String tea:entry.getValue()) {
					JSONObject object1 = new JSONObject();
					object1.put("id", tea);
					object1.put("pId", classID);
					String usersName = DictLoader.userID2Name(tea);
					object1.put("name", usersName);
					array.add(object1);
				}
			}
			//老师
			else if("2".equals(type)){
				JSONObject object0 = new JSONObject();
				String classID = DictLoader.classNameToID(entry.getKey());
				object0.put("id", classID);
				object0.put("pId", 1);
				object0.put("name", entry.getKey());
				object0.put("open", false);
				array.add(object0);
			}
			
		}
		return array;
	}
	//获取班级信息
	public JSONArray gradelist() {
		List<String> list = DictLoader.getGradeID();
		JSONArray array = new JSONArray();
		if(!list.isEmpty()) {
			for(String gradeid:list) {
				if(gradeid!=null&&!"".equals(gradeid)) {
					if(!gradeid.contains(";")) {
						JSONObject object0 = new JSONObject();
						object0.put("id", gradeid);
						object0.put("pId", 0);
						object0.put("name", gradeid);
						array.add(object0);
					}
				}
			}
		}
		return array;
	}
}

