package io.github.jhipster.sample.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;



public class AlgorithmParaService {
    private static String ProjectPathPrefix = "src/main/webappfiles/Project/";
    private static FileUtil fileUtil = new FileUtil();
    private HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();

    private String getParaRoot(String project, String task){
    	String para_root = ProjectPathPrefix + project + '/' + task + "/para/";
    	return para_root;
    }
    
    public boolean addParameterConfig(String project, String task, String jsonConfig, boolean hdfs){
    	String para_root = getParaRoot(project, task);
    	File f = new File(para_root);
    	if(! f.exists()){
    		return false;
    	}
    	try {
			JSONObject para_config = new JSONObject(jsonConfig);
			String name = para_config.getString("name") + ".json";
			if (getParameterConfig(project, task, name)!=null && !hdfs){
				return false;
			}
			if(hdfs){
				name = para_config.getString("name") + "_hdfs.json";
			}
			String filePath = para_root + name ;
			fileUtil.saveToFile(filePath, para_config);
			if(hdfs){
				hdfsFileUtil.upload(filePath, hdfsFileUtil.root()+project+"/"+task+"/para/", true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    public String getParameterConfig(String project, String task, String name){
    	String para_root = getParaRoot(project, task);
    	String filePath =  para_root + name;
    	File f = new File(filePath);
    	if(! f.exists()){
    		return null;
    	}
    	try{
  		   BufferedReader reader =new BufferedReader(new FileReader(filePath));
  		   StringBuffer buffer = new StringBuffer();
  		   String tempString = null;
  		   while((tempString=reader.readLine())!=null){
  			   buffer.append(tempString);
  		   }
  		   reader.close();
  		   return buffer.toString();

  	    }catch (Exception e) {
  		   e.printStackTrace();
  		   return null;
  	    }
    }
    
    public List<String> listParameterConfigs(String project, String task){
    	String para_root = getParaRoot(project, task);
    	List<String> files = fileUtil.list_files(para_root);
    	return files;
    }
    
    public String runLocal(String project, String task, String algo_file, String jsonConfig) throws JSONException{
        JSONObject json_param = new JSONObject(jsonConfig);
        
    	String py_path = ProjectPathPrefix + project + '/' + task + "/algo/" + algo_file;
        String cmd = "python " + py_path;
        JSONArray para_items = (JSONArray) json_param.get("col");
        
        for(int i=0;i<para_items.length();i++){
        	JSONObject item = (JSONObject) para_items.get(i);
        	String value = item.getString("value");
        	String name = item.getString("name");
        	boolean type = item.getBoolean("type");
        	if(type){
        		value = ProjectPathPrefix + project + '/' + task + "/data/" + value;
        	}
            cmd += " --" + name + " " + value;

        }
        
        System.out.println(cmd);
 
        StringBuffer result = new StringBuffer();
        String info = "Project: "+ project + " task:" + task +" Py File: "+ algo_file;
        result.append(info);
        result.append("\n--------run log-------------\n");
        try {
            Process pr = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	System.out.println(line);
                result.append(line);
            }
            
            result.append("\n------error log--------\n");
            BufferedReader e_in = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
	        while((line = e_in.readLine())!=null){
	        	System.out.println(line);
	        	result.append(line);	        	
	        }
            
			in.close();
	        pr.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result.toString();
    }
    
}
