package com.announce.bean;

public class Notice {
	private String BH;
	private String FBR;
	private String FBRXM;
	private String FBRDW;
	private String GGNR;
	private String FBSJ;
	private String ZT;
	public String getZT() {
		return ZT;
	}
	public void setZT(String zT) {
		ZT = zT;
	}
	public String getBH() {
		return BH;
	}
	public void setBH(String bH) {
		BH = bH;
	}
	public String getFBR() {
		return FBR;
	}
	public void setFBR(String fBR) {
		FBR = fBR;
	}
	public String getFBRXM() {
		return FBRXM;
	}
	public void setFBRXM(String fBRXM) {
		FBRXM = fBRXM;
	}
	public String getFBRDW() {
		return FBRDW;
	}
	public void setFBRDW(String fBRDW) {
		FBRDW = fBRDW;
	}
	public String getGGNR() {
		return GGNR;
	}
	public void setGGNR(String gGNR) {
		GGNR = gGNR;
	}
	public String getFBSJ() {
		return FBSJ;
	}
	public void setFBSJ(String fBSJ) {
		FBSJ = fBSJ;
	}
	@Override
	public String toString() {
		return "Notice [BH=" + BH + ", FBR=" + FBR + ", FBRXM=" + FBRXM
				+ ", FBRDW=" + FBRDW + ", GGNR=" + GGNR + ", FBSJ=" + FBSJ
				+ "]";
	}
	
}
