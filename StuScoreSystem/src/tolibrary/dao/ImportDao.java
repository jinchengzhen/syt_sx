package tolibrary.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school.scoreManage.bean.User;
import com.school.scoreManage.util.SQLservice;

import dao.impl.MySQLOperationBySQLImpl;
import util.MySQLConnection;


public class ImportDao {
	public static void main(String[] args) {
		MySQLConnection.init("127.0.0.1","root", "123456", "jin");
	}
	
	//添加数据
	public static int adduser(List<String> sqls) {
		MySQLConnection.init("127.0.0.1","root", "123456", "jin");
		int result = new MySQLOperationBySQLImpl().addInformation(sqls);
		return result;
	}
}
