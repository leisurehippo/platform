package io.github.jhipster.sample.service.util;

import java.io.File;
import java.io.FileWriter;
import org.json.JSONObject;

public class FileUtil {

	public static void saveToFile(String filePath, JSONObject resJson){
		File file = new File(filePath);
		System.out.println(filePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsolutePath(),false);
			fw.write(resJson.toString());
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveToFile(String filePath, String content){
		File file = new File(filePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsolutePath(),false);
			fw.write(content);
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkFile(String filePath){
		try{
			File file = new File(filePath);

			if (file.isFile() && file.exists())
	        { // 判断文件是否存在
	            return true;
	        }
	        else
	        {
	            System.out.println("找不到指定的文件");
	            return false;
	        }
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void removeFile(String filePath){
		try{
			File file = new File(filePath);
			if(checkFile(filePath)){
				file.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
	}
	}
}
