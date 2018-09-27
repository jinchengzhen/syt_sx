package com.school.scoreManage.bean;

public class User {
	private String userID;
	private String name;
	private String password;
	private String type;
	private String sex;
	private String birth;
	private String phone;
	private String gradeID;
	public String getGradeID() {
		return gradeID;
	}
	public void setGradeID(String gradeID) {
		this.gradeID = gradeID;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	@Override
	public String toString() {
		return "User [userID=" + userID + ", name=" + name + ", password=" + password + ", type=" + type + ", sex="
				+ sex + ", birth=" + birth + ", phone=" + phone + "]";
	}
	
}
