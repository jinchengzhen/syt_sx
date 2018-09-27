package com.school.scoreManage.bean;

public class Classes {
	private String classID;
	private String className;
	private String stuID;
	private String teaID;
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getStuID() {
		return stuID;
	}
	public void setStuID(String stuID) {
		this.stuID = stuID;
	}
	public String getTeaID() {
		return teaID;
	}
	public void setTeaID(String teaID) {
		this.teaID = teaID;
	}
	@Override
	public String toString() {
		return "Classes [classID=" + classID + ", className=" + className + ", stuID=" + stuID + ", teaID=" + teaID
				+ "]";
	}
}
