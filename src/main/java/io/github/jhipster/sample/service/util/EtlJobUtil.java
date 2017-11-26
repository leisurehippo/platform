package io.github.jhipster.sample.service.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

public class EtlJobUtil {

	public static String getJob(DBSource reader, DBSource writer, Transform transform){
		JSONObject res = new JSONObject();
		JSONObject job = new JSONObject();
		JSONObject content = new JSONObject();
		try{
		content.put("reader", new JSONObject(reader.toString()));
		content.put("writer", new JSONObject(writer.toString()));
		content.put("transformer", new JSONArray(transform.toString()));
		JSONArray contentArr = new JSONArray();
		contentArr.put(content);
		
		job.put("setting", new JSONObject(getSetting()));
		job.put("content", contentArr);
		res.put("job", job);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
		
	}
	//暂时不可编辑setting
	private static String getSetting(){
		JSONObject setting = new JSONObject();
		JSONObject speed = new JSONObject();
		try{
		speed.put("channel", "1");
		setting.put("speed", speed);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return setting.toString();
	}
	
	public static boolean runJob(String dataXPath, String jsonPath){
		System.out.println("prepare to execute transform");
		String[] pargs = new String[]{"python",dataXPath,jsonPath};
		System.out.println(jsonPath);
		if(!FileUtil.checkFile(jsonPath)){
			return false;
		}
		boolean flag = true;
		try {
			Process process = Runtime.getRuntime().exec(pargs);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line;  
  
            while ((line = in.readLine()) != null) {  
            	System.out.println(line); //测试时使用，完成时删除
                if (line.contains("ERROR")){
                	System.out.println("Fail");
                	flag = false;
                	break;
                }
            }  
            in.close();  
            process.waitFor();  
            System.out.println("end"); 
            return flag;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	
}
