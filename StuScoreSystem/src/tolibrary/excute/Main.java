package tolibrary.excute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.school.scoreManage.util.SQLservice;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tolibrary.dao.ImportDao;
import tolibrary.util.Txtoption;

public class Main {

	public static void main(String[] args) {
		//读取txt
		String table = "system.t_sys_layerpoint";
		String filepath = "C:\\Users\\Administrator\\Desktop\\Address\\转换后的txt";
		String sqltxtpath = "C:\\Users\\Administrator\\Desktop\\Address\\result";
		try {
			List<String> list = Txtoption.readfile(filepath);
			if(!list.isEmpty()) {
				for(String txtname : list) {
					File file = new File(filepath+"\\"+txtname);
					String data = Txtoption.txt2String(file);
					//转成json数组
					JSONArray array = JSONArray.fromObject(data);
					//读取json
					for(int i=0;i<array.size();i++) {
						JSONObject jsondata = array.getJSONObject(i);
						JSONArray listall = jsondata.getJSONArray("ListAll");
						List<List<String>> allsql = new ArrayList<List<String>>();
						for(int j=0;j<listall.size();j++) {
							JSONObject childarray = listall.getJSONObject(j);
							JSONArray children = childarray.getJSONArray("children");
							
							List<Map<String,String>> condition = new ArrayList<Map<String,String>>();
							for(int k=0;k<children.size();k++) {
								Map<String,String> map = new HashMap<String,String>();
								JSONObject childdata = children.getJSONObject(k);
								String id = childdata.get("id").toString();
								String name = childdata.get("name").toString();
								String address = childdata.get("address").toString();
								String x = childdata.get("x").toString();
								String y = childdata.get("y").toString();
								String layer = childdata.get("layer").toString();
								String LX = childdata.get("LX").toString();
								String SSJGDM = childdata.get("SSJGDM").toString();
								String SSJGMC = childdata.get("SSJGMC").toString();
								String XIANJXZQHDM = childdata.get("XIANJXZQHDM").toString();
								String XIANJXZQH = childdata.get("XIANJXZQH").toString();
								map.put("id", id);
								map.put("name", name);
								map.put("address", address);
								map.put("x", x);
								map.put("y", y);
								map.put("layer", layer);
								map.put("LX", LX);
								map.put("SSJGDM", SSJGDM);
								map.put("SSJGMC", SSJGMC);
								map.put("XIANJXZQHDM", XIANJXZQHDM);
								map.put("XIANJXZQH", XIANJXZQH);
								condition.add(map);
							}
							//生成一条数据sql语句
							List<String> sqls = SQLservice.add(table, condition);
							//添加到txt文件数据中sql
//							allsql.add(sqls);
							//存入数据库
							ImportDao.adduser(sqls);
						}
						//写入txt文本中
//						Txtoption.write2txt(allsql,sqltxtpath+"\\SQL-"+txtname);
					}
					//
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
