package com.school.scoreManage.bean;

public class Score {
	private String classID;
	private String stuID;
	private String score;
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getStuID() {
		return stuID;
	}
	public void setStuID(String stuID) {
		this.stuID = stuID;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "Score [classID=" + classID + ", stuID=" + stuID + ", score=" + score + "]";
	}
	
}
