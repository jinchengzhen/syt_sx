package com.school.scoreManage.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.school.scoreManage.action.SuperParentAction;
import com.school.scoreManage.bean.Classes;
import com.school.scoreManage.bean.Score;
import com.school.scoreManage.bean.User;
import com.school.scoreManage.constant.DictLoader;
import com.school.scoreManage.service.MainService;
import com.school.scoreManage.service.ToSQLService;
import com.school.scoreManage.util.RecordHelper;;
public class ScoreManageAction extends SuperParentAction{

	private static final long serialVersionUID = 1L;
	private MainService service=new MainService();
	private ToSQLService ser=new ToSQLService();
	JSONObject jsonData = new JSONObject();
	//登录用户验证
	public void login() {
		User user=new User();
		String userID = request.getParameter("userID");
		String password = request.getParameter("password");
		String type = request.getParameter("type");
		user.setUserID(userID);
		user.setPassword(password);
		user.setType(type);
		int result=service.loginAut(user);
		if(result>0) {
			jsonData.put("state", "1");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "用户不存在或密码错误！");
		}
		writeToJson(jsonData.toString());
	}
	//修改密码
	public void changekey() {
		User user=new User();
		String userID = request.getParameter("userID");

		String password = request.getParameter("password");
		user.setUserID(userID);
		user.setPassword(password);
		int result=service.changekey(user);
		if(result>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "修改成功！");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "修改失败！");
		}
		writeToJson(jsonData.toString());
	}
	//查看个人信息
	public void personInfo() {
		User user=new User();
		String userID = request.getParameter("userID");
		String type = request.getParameter("type");
		user.setUserID(userID);
		user.setType(type);
		JSONArray info = service.personInfo(user,null,null);
		if(!info.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", info.getJSONObject(0));
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "暂无本人信息");
		}
		writeToJson(jsonData.toString());
	}
	/**
	 * 学生操作功能（修改密码、查看成绩、查看个人信息）
	 */
	//查看成绩
	public void stuScore() {
		User user=new User();
		String userID = request.getParameter("userID");

		user.setUserID(userID);
		JSONArray scoreInfo = service.personScore(user);
		if(!scoreInfo.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", scoreInfo);
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "成绩查询失败");
		}
		writeToJson(jsonData.toString());
	}
	//查看所选课程教师（姓名、性别、电话）
	public void selectTeacher() {
		Classes cla = new Classes();
		String className = request.getParameter("className");
		String stuID = request.getParameter("userID");
		cla.setClassName(className);
		cla.setStuID(stuID);
		JSONObject teaInfo = service.selectTeacher(cla);
		if(!teaInfo.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", teaInfo);
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "未找到教师信息！");
		}
		writeToJson(jsonData.toString());
	}
	/**
	 * 教师操作功能：
	 * 查看学生（所有人、班级、个人）成绩
	 * 修改学生课程成绩
	 */
	
	//修改、录入学生课程成绩
	public void updateScore() {
		String userIDstr = request.getParameter("IDstr");
		String scorestr = request.getParameter("scorestr");
		String className = request.getParameter("className");
		String classID = DictLoader.classNameToID(className);
		String[] userIDs = userIDstr.split(",");
		String[] scores = scorestr.split(",");
		List<String> userIDlist = Arrays.asList(userIDs);
		List<String> scorelist = Arrays.asList(scores);
		List<Score> scoreData = new ArrayList<Score>();
		//单条修改，支持批量修改(注意：userIDlist和scorelist长度应保持相同)
		if(!userIDlist.isEmpty()&&!scorelist.isEmpty()) {
			for(int i=0;i<userIDlist.size();i++) {
				Score s = new Score();
				s.setClassID(classID);
				s.setStuID(userIDlist.get(i));
				s.setScore(scorelist.get(i));
				scoreData.add(s);
			}
		}
		int result = service.updateScore(scoreData);
		if(result>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "已录入！");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "录入失败");
		}
		writeToJson(jsonData.toString());
	}
	//查看学生成绩（班级/所有人）
	public void selectScore() {
		String className = request.getParameter("className");
		String grade = request.getParameter("gradeID");
		String currpagestr = request.getParameter("currpage");
		String pagesizestr = request.getParameter("pagesize");
		Integer currpage = Integer.parseInt(currpagestr);
		Integer pagesize = Integer.parseInt(pagesizestr);
		//排序方式
		String sortway = request.getParameter("sortway");
		JSONArray scorelist = service.selectscore(className, grade+"班",sortway,currpage,pagesize);
		jsonData.put("state", "1");
		jsonData.put("message", scorelist);
		writeToJson(jsonData.toString());
	}
	/**
	 * 管理员操作功能
	 * 新增（删除）人员、新增（删除）课程、新增（删除）班级
	 * 修改人员信息（密码初始化）
	 * 查询人员信息（人员类型、班级、课程））
	 */
	//选课信息列表
	public void classTree() {
		String type = request.getParameter("type");
		JSONArray array = service.classtealist(type);
		if(!array.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", array);
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "暂无选课信息！");
		}
		writeToJson(jsonData.toString());
	}
	//所有班级列表
	public void gradeTree() {
		JSONArray array = service.gradelist();
		if(!array.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", array);
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "暂无班级信息！");
		}
		writeToJson(jsonData.toString());
	}
	//新增用户信息：人员基本信息、所选课程、所选班级（按用户ID）
	public void addInfo() {
		String userID = request.getParameter("userID");
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String birth = request.getParameter("birth");
		String sex = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String grade = request.getParameter("grade");
		String classstr = request.getParameter("classes");
		String gradeID = grade.replace("@", "");
		List<User> userlist = new ArrayList<User>();
		User user = new User();
		user.setUserID(userID);
		user.setType(type);
		user.setName(name);
		user.setPassword(password);
		user.setBirth(birth);
		user.setSex(sex);
		user.setPhone(phone);
		user.setGradeID(gradeID);
		userlist.add(user);
		List<String> classdata = Arrays.asList(classstr.split("@"));
		List<Classes> classlist =new ArrayList<Classes>();
		if("1".equals(type)) {
			for(int i=0;i<classdata.size()-1;) {
				Classes cla = new Classes();
				cla.setClassID(classdata.get(i));
				String className = DictLoader.classIDoName(classdata.get(i++));
				cla.setClassName(className);
				cla.setStuID(userID);
				cla.setTeaID(classdata.get(i++));
				classlist.add(cla);
			}
		}else if("2".equals(type)){
			for(int i=0;i<classdata.size();i++) {
				Classes cla = new Classes();
				cla.setClassID(classdata.get(i));
				String className = DictLoader.classIDoName(classdata.get(i));
				cla.setClassName(className);
				cla.setTeaID(userID);
				classlist.add(cla);
			}
		}
		int res = service.addUserInfo(userlist, classlist);
		if(res>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "新增人员成功！");
			DictLoader.init();
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "新增人员失败！");
		}
		writeToJson(jsonData.toString());
	}
	//删除用户信息
	public void deleteInfo() {
		String userID = request.getParameter("userID");

		Map<String,String> userInfo = new HashMap<String, String>();
		String type = request.getParameter("type");
		userInfo.put("userID",userID );
		userInfo.put("type",type );
		int res = service.deleteUserInfo(userInfo);
		if(res>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "删除人员成功！");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "删除人员失败！");
		}
		writeToJson(jsonData.toString());
	}
	//新增课程信息
	public void addCla() {
		String userID = request.getParameter("userID");

//		String type = request.getParameter("type");
		String classstr = request.getParameter("classes");
		String stuID = request.getParameter("stuID");
		String teaID = request.getParameter("teaID");
		List<String> classes = Arrays.asList(classstr.split(","));
		List<Classes> classlist = new ArrayList<Classes>();
		for(String className:classes) {
			Classes cla = new Classes();
			String classID = DictLoader.classNameToID(className);
			cla.setClassID(classID);
			cla.setClassName(className);
			if(stuID==null||"".equals(stuID)) {
				stuID = userID;
			}
			if(teaID==null||"".equals(teaID)) {
				teaID = userID;
			}
			cla.setStuID(stuID);
			cla.setTeaID(teaID);
			classlist.add(cla);
		}
		int res = service.addCla(classlist);
		if(res>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "课程添加成功！");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "课程添加失败！");
		}
		writeToJson(jsonData.toString());
		
	}
	//删除选课信息
	public void deleteCla() {
		String userID = request.getParameter("userID");

//		String type = request.getParameter("type");
		String classstr = request.getParameter("classes");
		String stuID = request.getParameter("stuID");
		String teaID = request.getParameter("teaID");
		List<String> classes = Arrays.asList(classstr.split(","));
		List<Classes> classlist = new ArrayList<Classes>();
		for(String className:classes) {
			Classes cla = new Classes();
			String classID = DictLoader.classNameToID(className);
			cla.setClassID(classID);
			cla.setClassName(className);
			if(stuID==null||"".equals(stuID)) {
				stuID = userID;
			}
			if(teaID==null||"".equals(teaID)) {
				teaID = userID;
			}
			cla.setStuID(stuID);
			cla.setTeaID(teaID);
			classlist.add(cla);
		}
		int res = service.deleteCla(classlist);
		if(res>0) {
			jsonData.put("state", "1");
			jsonData.put("message", "删除课程成功！");
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "删除课程失败！");
		}
		writeToJson(jsonData.toString());
	}
	Pattern pattern = Pattern.compile("\\d[0-9]+"); 
	//查询人员信息（按课程、班级、人员类型、姓名）
	public void selectInfo() {
		User user=new User();
		String type = request.getParameter("type");//人员类型
		String findway = request.getParameter("findway");//查找方式
		String val = request.getParameter("val");//输入信息
		String currpagestr = request.getParameter("currpage");
		String pagesizestr = request.getParameter("pagesize");
		Integer currpage = Integer.parseInt(currpagestr);
		Integer pagesize = Integer.parseInt(pagesizestr);
		JSONArray array = new JSONArray();
		String className = null;
		String gradeID = null;
		String name = null;
		String userID = null;
		if("classway".equals(findway)) {
			className = val;
		}else if("gradeway".equals(findway)) {
			gradeID = val;
		}else if("userway".equals(findway)) {
			Matcher isNum = pattern.matcher(val);
			if(isNum.matches()) {
				userID = val;
			}else {
				name = val;
			}
		}
		if(!"".equals(className)&&className!=null) {
			array = service.userInfoWithTerm(className, type,currpage,pagesize);
		}else {
			user.setType(type);
			if(name!=null) {
				user.setName(name);
			}
			if(userID!=null) {
				user.setUserID(userID);
			}
			if(gradeID!=null) {
				user.setGradeID(gradeID);
			}
			array = service.personInfo(user,currpage,pagesize);
		}
		if(!array.isEmpty()) {
			jsonData.put("state", "1");
			jsonData.put("message", array);
		}else {
			jsonData.put("state", "0");
			jsonData.put("message", "查询结果为空！");
		}
		writeToJson(jsonData.toString());
	}
	//导出excel文件
	public void exportExcel() {
		String webPath = "";
		List<String[]> listContent=new ArrayList<String[]>();
		//登录人员类型（'0'为管理员、'2'为教师'、1'为学生）
		String login_type = request.getParameter("login_type");
		if("2".equals(login_type)) {
			//班级成绩
			String className = request.getParameter("className");
			String gradeID = request.getParameter("gradeID");
			String sortway = request.getParameter("sortway");
			String classID = DictLoader.classNameToID(className);
			String filename = "grade_"+gradeID+"@class"+classID+"_score.xls";
			String[] title = {"班级","序号","学号","姓名","课程名称","成绩"};
			int num = 1;
			Map<String, Double> scorelist = ser.selectGradescore(gradeID+"班", classID,sortway,null,null);
			if(!scorelist.isEmpty()) {
				for (Entry<String, Double> entry : scorelist.entrySet()) { 
					String[] data = new String[title.length];
					if(!"平均分".equals(entry.getKey())) {
						data[0] = gradeID+"班";
						data[1] = (num++)+"";
						data[2] = entry.getKey();
						data[3] = DictLoader.userID2Name(entry.getKey());
						data[4] = className;
						data[5] = entry.getValue().toString();
					}else {
						data[4] = "班级平均分";
						data[5] = entry.getValue().toString();
					}
					listContent.add(data);
				}
				String path = request.getSession().getServletContext().getRealPath(filename); 
				File file=new File(path);
				RecordHelper.exportExcel(file, title, listContent);
				webPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+filename;
			}
		}else if("1".equals(login_type)) {
			//学生个人成绩
			String userID = request.getParameter("loginer_ID");
			String username = DictLoader.userID2Name(userID);
			String filename = userID+"_score.xls";
			Map<String, Double> scorelist=ser.selectscore("classID",userID ,null,null,null);
			if(!scorelist.isEmpty()) {
				String[] title= new String[scorelist.size()+2];
				String[] data = new String[title.length];
				title[0] = "姓名";data[0] = username;
				title[1] = "学号";data[1] = userID;
				int k = 2;
				for (Entry<String, Double> entry : scorelist.entrySet()) { 
					if("总分".equals(entry.getKey())) {
						title[k] = "总分";
					}else {
						title[k] = DictLoader.classIDoName(entry.getKey());
					}
					data[k++] = entry.getValue().toString();
				}
				listContent.add(data);
				String path = request.getSession().getServletContext().getRealPath(filename); 
				File file=new File(path);
				RecordHelper.exportExcel(file, title, listContent);
				webPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+filename;
			}
		}else if("0".equals(login_type)) {
			String type = request.getParameter("type");//人员类型
			String findway = request.getParameter("findway");//查找方式
			String val = request.getParameter("val");//输入信息
			String filename = "user_info.xls";
			String className = null;
			String gradeID = null;
			String name = null;
			String userID = null;
			if("classway".equals(findway)) {
				className = val;
			}else if("gradeway".equals(findway)) {
				gradeID = val;
			}else if("userway".equals(findway)) {
				Matcher isNum = pattern.matcher(val);
				if(isNum.matches()) {
					userID = val;
				}else {
					name = val;
				}
			}
			List<User> list = new ArrayList<User>();
			if(!"".equals(className)&&className!=null) {
				list = ser.selectuserWithTerm(className, type,null,null);
			}else {
				User user = new User();
				user.setType(type);
				if(name!=null) {
					user.setName(name);
				}
				if(userID!=null) {
					user.setUserID(userID);
				}
				if(gradeID!=null) {
					user.setGradeID(gradeID);
				}
				list=ser.selectuser(user,null,null);
			}
			String[] title= {"用户ID","密码","姓名","性别","所在班级","人员类型","出生日期","联系电话"};
			if(!list.isEmpty()) {
				for(int i=0;i<list.size();i++) {
					String[] data = new String[title.length];
					data[0] = list.get(i).getUserID();
					data[1] = list.get(i).getPassword();
					data[2] = list.get(i).getName();
					data[3] = DictLoader.sex2Name(list.get(i).getSex());
					data[4] = list.get(i).getGradeID();
					data[5] = DictLoader.type2Name(list.get(i).getType());
					data[6] = list.get(i).getBirth();
					data[7] = list.get(i).getPhone();
					listContent.add(data);
				}
				String path = request.getSession().getServletContext().getRealPath(filename); 
				File file=new File(path);
				RecordHelper.exportExcel(file, title, listContent);
				webPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+filename;
			}
			
		}
		writeToTEXT(webPath);
	}
	
}
