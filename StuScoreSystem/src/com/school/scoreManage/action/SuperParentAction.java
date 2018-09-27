package com.school.scoreManage.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @ClassName SuperParentAction
 * @author yangpf
 * @description Action辅助类，继承了struts2的辅助类，实现了request,response,session,application注入
 * @date 9:29:30 AM
 */
public class SuperParentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware, SessionAware,ApplicationAware{
	private InputStream inputStream;
	private static final long serialVersionUID = -4128991870986733900L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> session;
	protected Map<String, Object> application;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void writeToData(String message) {
		try {
			message = message == null || message.equals("") ? "" : message
					.trim();
			inputStream = new ByteArrayInputStream(message.getBytes("utf-8"));
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public void writeToTEXT(String text) {
		PrintWriter os = null;
		try {
			response.setCharacterEncoding("UTF-8");
			os = response.getWriter();
			os.println(text);
			os.close();
		} catch (UnsupportedEncodingException e) {
			try {
				os.println("");
				os.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToXML(String xml) {
		OutputStream os = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-type", "text/xml;charset=UTF-8");
			os = response.getOutputStream();
			os.write(xml.getBytes("UTF-8"));
			os.close();
		} catch (UnsupportedEncodingException e) {
			try {
				os.write("".getBytes("UTF-8"));
				os.close();
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			} catch (IOException ec) {
				ec.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToHTML(String html) {
		OutputStream os = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			os = response.getOutputStream();
			os.write(html.getBytes("UTF-8"));
			os.close();
		} catch (UnsupportedEncodingException e) {
			try {
				os.write("".getBytes("UTF-8"));
				os.close();
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			} catch (IOException ec) {
				ec.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToJson(String json) {
		OutputStream os = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-json");
			os = response.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.close();
		} catch (UnsupportedEncodingException e) {
			try {
				os.write(null);
				os.close();
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			} catch (IOException ec) {
				ec.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String dataToUTF8(String str) {
		try {
			String temp = URLDecoder.decode(request.getParameter(str), "utf-8")
					.toString().trim();
			if (temp.equals("undefined") || temp.equals("") || temp == null) {
				return "";
			} else {
				return temp;
			}
		} catch (UnsupportedEncodingException e) {
			String temp = request.getParameter(str).toString().trim();
			return temp;
		} catch (Exception e) {
			String temp = "";
			try {
				temp = request.getParameter(str).toString().trim();
				if (temp.equals("") || temp == null) {
					return temp;
				}
			} catch (Exception ex) {
				return "";
			}
			return "";

		}
	}
}
