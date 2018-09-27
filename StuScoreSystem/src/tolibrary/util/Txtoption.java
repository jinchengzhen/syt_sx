package tolibrary.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Txtoption {
	 public static void write2txt(List<List<String>> alllist,String filepath) {
		 try {
		        /* 写入Txt文件 */
		            File writename = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
		            writename.createNewFile(); // 创建新文件
		            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writename),"gbk"));
		           for(List<String> list:alllist) {
		        	   for(String sql:list) {
		        		   out.write(sql+"\r\n"); // \r\n即为换行
		        	   }
		           }
		            out.flush(); // 把缓存区内容压入文件
		            out.close(); // 最后记得关闭文件
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	 }
	 public static String txt2String(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	        	//编码格式：gbk 或 utf-8
	            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	    
    public static void main(String[] args){
//        System.out.println(txt2String(file));
        String filepath = "C:\\Users\\Administrator\\Desktop/a.txt";
        File file = new File(filepath);
       String res = txt2String(file);
       String[] t = res.split("\r\n");
       Map<Double,Double> score = new HashMap<Double,Double>();
       for(String s : t) {
    	   if(!"".equals(s)) {
    		   String[] demo = s.split(":");
    		   if(demo.length>1) {
    			   double jd = hs(Double.parseDouble(demo[0]));
    			   score.put(jd, Double.parseDouble(demo[1]));
    		   }
    	   }
       }
       double sum = 0;
       double mul = 0;
       for(Entry<Double,Double> entry:score.entrySet() ) {
    	   mul += entry.getKey()*entry.getValue();
    	   sum += entry.getValue();
       }
       System.out.println(mul/sum);
//       System.out.println(res);
//        try {
//        	List<String> list = readfile(filepath);
//        	
//        	for(String str : list) {
//        		System.out.println(str);
//        	}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
    
    
    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        List<String> fileNamelist = new ArrayList<String>();
    	try {

                File file = new File(filepath);
                if (!file.isDirectory()) {
//                        System.out.println("文件");
//                        System.out.println("path=" + file.getPath());
//                        System.out.println("absolutepath=" + file.getAbsolutePath());
//                        System.out.println("name=" + file.getName());
                			fileNamelist.add(file.getName());

                } else if (file.isDirectory()) {
//                        System.out.println("文件夹");
                        String[] filelist = file.list();
                        for (int i = 0; i < filelist.length; i++) {
                                File readfile = new File(filepath + "\\" + filelist[i]);
                                if (!readfile.isDirectory()) {
//                                        System.out.println("path=" + readfile.getPath());
//                                        System.out.println("absolutepath="
//                                                        + readfile.getAbsolutePath());
//                                        System.out.println("name=" + readfile.getName());
                                	fileNamelist.add(readfile.getName());
                                } else if (readfile.isDirectory()) {
                                        readfile(filepath + "\\" + filelist[i]);
                                }
                        }

                }

        } catch (FileNotFoundException e) {
                System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return fileNamelist;
}
    	public static double hs(double key) {
    		double rs = 0;
    		if(key>=95&&key<=100) {
    			rs = 4.33;
    		}else if(key>=90&&key<=94) {
    			rs = 4.0;
    		}else if(key>=85&&key<=89) {
    			rs = 3.67;
    		}else if(key>=82&&key<=84) {
    			rs = 3.33;
    		}else if(key>=78&&key<=81) {
    			rs = 3;
    		}else if(key>=75&&key<=77) {
    			rs = 2.67;
    		}else if(key>=72&&key<=74) {
    			rs = 2.33;
    		}else if(key>=68&&key<=71) {
    			rs = 2;
    		}else if(key>=64&&key<=67) {
    			rs = 1.67;
    		}else if(key>=61&&key<=63) {
    			rs = 1.33;
    		}else if(key==60) {
    			rs = 1;
    		}else if(key>=0&&key<=59) {
    			rs = 0;
    		}
    		return rs;
    	}
}

