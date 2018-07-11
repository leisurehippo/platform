package io.github.jhipster.sample.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
		/**
		 * 注意运行的python环境必须为2.7,不能为3。在Eclipse测试时，更改环境后需重启Eclipse
		 */
		
		System.out.println("prepare to execute transform");
		String[] pargs = new String[]{"python",dataXPath,jsonPath};
		System.out.println(Arrays.toString(pargs));
		FileUtil fileUtil = new FileUtil();
		if(!fileUtil.checkFile(jsonPath)){
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
	        BufferedReader e_in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	        while((line = e_in.readLine())!=null){
	        	System.out.println(line);
	        }
            in.close(); 
            e_in.close();
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
