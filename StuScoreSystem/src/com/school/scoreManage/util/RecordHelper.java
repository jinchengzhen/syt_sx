package com.school.scoreManage.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

public class RecordHelper{
	private static Logger logger = Logger.getLogger(RecordHelper.class);
	public static void downLoadFromUrl(String urlStr, String fileName,
			File savePath){
		try {
			// 第一步，将字符串路径转为url对象
			URL url = new URL(urlStr);
			// 第二步,获取url的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 第三步，设置连接超时
			conn.setConnectTimeout(3 * 1000);
			// 第四步，获取url的流对象
			InputStream inputStream = conn.getInputStream();
			// 第五步，获取自己数组
			byte[] getData = readInputStream(inputStream);
			// 第六步，创建文件属性，获取文件名称
			File file = new File(savePath + File.separator + fileName);
			// 第七步，输出到文件夹
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			// 第八步，关闭各种流
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			logger.info("下载消息:文件"+fileName+"，下载成功");
		} catch (Exception e) {
			logger.info("下载消息:文件"+fileName+"，下载失败");
		}
	}

	private static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.flush();
		if(bos != null && bos.size() > 0){
			bos.close();
		}
		return bos.toByteArray();
	}

	public static void exportExcel(File file, String[] Title,List<String[]> listContent) {
		// 以下开始输出到EXCEL
		try {
			OutputStream out = new FileOutputStream(file);
			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(out);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < Title.length; i++) {
				sheet.addCell(new Label(i, 0, Title[i], wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
			Field[] fields = null;
			int i = 1;
			for (Object obj : listContent) {
				if(!obj.getClass().getSimpleName().equals("String[]")){
					fields = obj.getClass().getDeclaredFields();
					int j = 0;
					for (Field v : fields) {
						v.setAccessible(true);
						Object va = v.get(obj);
						if (va == null) {
							va = "";
						}
						System.out.println("va:"+va);
						sheet.addCell(new Label(j, i, va.toString(), wcf_left));
						j++;
					}
					i++;
				}else if(obj.getClass().getSimpleName().equals("String[]")){
					String [] s = (String[])obj;
					int j = 0;
					for(String str:s){
						sheet.addCell(new Label(j, i, str, wcf_left));
						j++;
					}
					i++;
				}
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			if(workbook != null){
				workbook.close();
			}
			if(out != null){
				out.close();
			}
			logger.info("下载消息：文件"+file.getName()+"导出成功！");
		} catch (Exception e) {
			logger.info("下载消息：文件"+file.getName()+"导出失败，原因：" + e.toString());
		}
	}
	
}
