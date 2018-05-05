package io.github.jhipster.sample.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import io.github.jhipster.sample.service.util.TemplateFillTask;
import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

@Service
public class TemplateFillService {
	
	private final String PYTHON_PATH = "src/main/java/io/github/jhipster/sample/web/rest/python/templateFill";
	private static FileUploadingUtil fileUtil = new FileUploadingUtil();
	private static String ProjectPathPrefix = "src/main/webappfiles/Project/TemplateFill";	


	private boolean transform(String task_path){
		File f = new File(task_path);
		//must be absolute path
		String[] pargs = new String[]{"python",PYTHON_PATH+"/doc_transform.py",f.getAbsolutePath()};
		for(String s:pargs){
			System.out.println(s);
		}
		return excute(pargs);
	}
	
	private boolean run(String task_path, boolean xianlu){
		ArrayList<String> pargs = new ArrayList<String>();
		File f = new File(task_path);
		String ab_taskpath = f.getAbsolutePath();
		pargs.add("python");
		pargs.add(PYTHON_PATH+"/main.py");
		pargs.add(ab_taskpath);
		pargs.add(PYTHON_PATH);
		if(!xianlu){
			pargs.add("false");
		}
		
		String[] args = pargs.toArray(new String[0]);
		return excute(args);
	}
	
	public boolean runTask(String taskname){
		String task_path = ProjectPathPrefix +'/'+taskname;
		String txt_path = task_path + "/newdir";
		String docx_path = task_path + "/docx";
		init_path(txt_path);
		init_path(docx_path);
		
		boolean flag_t = transform(task_path);
		if(!flag_t){
			return flag_t;
		}
		String type = getTask(taskname).getType();
		boolean xianlu = false;
		if(type.equals("线路")){
			xianlu = false;
		}
		return run(task_path,xianlu);
	}
	
	private void init_path(String path){
		File file = new File(path);
		if (file.exists()){
			for(File f:file.listFiles()){
				f.delete();
			}
		}
		else{
			file.mkdir();
		}
		System.out.println(file.exists());
	}
	
	private boolean excute(String[] pargs) {
		try {
			Process process = Runtime.getRuntime().exec(pargs);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));  
	        String line;  
	        BufferedReader e_in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	        while ((line = in.readLine()) != null) {  
	        	System.out.println(line); //测试时使用，完成时删除
	
	        }  
	        while((line = e_in.readLine())!=null){
	        	System.out.println(line);
	        }
	        in.close();  	        
	        e_in.close();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	public boolean addTask(String taskname,String type,String username){
		   TemplateFillTask task = new TemplateFillTask(taskname, username,type);
	       if(!task.is_valid()){
	    	   return false;
	       }
	        
		   File file = new File(ProjectPathPrefix + '/' + taskname);
	       if(file.exists()){
	        	return false;
	       }
	       file.mkdir();
	       String original_path = ProjectPathPrefix + '/' + taskname + "/original";
	       	File original_dir = new File(original_path);
	       	if(! original_dir.exists()){
	       		original_dir.mkdir();
	       	}
	       boolean flag = fileUtil.createFile(ProjectPathPrefix+'/'+taskname+'/', "log.txt", task.toString());
	       return flag;
	}
	
	
   public boolean delTask(String taskname){
    	
    	String dirPath = ProjectPathPrefix + '/'+taskname;
    	boolean flag = fileUtil.deleteDirectory(dirPath);
    	return flag;
    }
   
   /**
    * 现在只支持修改task的类型，之后可能会增加其他功能
    * @param taskname
    * @param type
    * @return
    */
   public boolean updateTask(String taskname, String type){
	   String file_path = ProjectPathPrefix +'/'+taskname+'/'+"log.txt";
	   try{
		   BufferedReader reader =new BufferedReader(new FileReader(file_path));
		   String taskinfo = reader.readLine();
		   TemplateFillTask task = new TemplateFillTask(taskinfo);
		   reader.close();
		   task.setType(type);
		   if(task.is_valid()){
			   FileUploadingUtil.writeFileContent(file_path, task.toString());
		   }
		   else{
			   return false;
		   }
	   }catch (Exception e) {
		   e.printStackTrace();
		   return false;
	   }
	   return true;
   }
   
   /**
    * 根据任务名，返回任务对象
    * @param taskname
    * @return
    */
   public TemplateFillTask getTask(String taskname){
	   String task_path = ProjectPathPrefix +'/'+taskname;
	   File file = new File(task_path);
	   TemplateFillTask task = null;
	   if(!file.exists()){
		   return null;
	   }
	   String file_path = ProjectPathPrefix +'/'+taskname+'/'+"log.txt";
	   try{
		   BufferedReader reader =new BufferedReader(new FileReader(file_path));
		   String taskinfo = reader.readLine();
		   task = new TemplateFillTask(taskinfo);
		   reader.close();

	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return task;
	   
   }
   /**
    * 
    * @param taskname
    * columns = ['报告名称','故障名称', '单位名称', '变电站名称', '塔号','电压等级', '设备类型','型号','生产厂家','投运时间', '故障发现时间','测试时天气'
          ,'测试环境温度', '测试环境湿度', '地理位置','缺陷等级', '保护动作','保护装置','故障前情况','故障时情况','其他时间']
    * columns = {"file_name","fault_name","unit_name","substation","tower_id","voltaget","device_type","device_id","manufacturer",
          "run_date","find_date","weather","temperature","humidity",
          "location","bad_level","protective_action","protective_device","pre_situation","in_situation","other_time"};
    * @return
    */
   public JSONArray getResult(String taskname){
	   String file_path = ProjectPathPrefix +'/'+taskname+'/'+"result.txt";
	   File file = new File(file_path);
	   JSONArray result_infos = new JSONArray();
	   String []columns = {"file_name","fault_name","unit_name","substation","tower_id","voltaget","device_type","device_id","manufacturer",
	              "run_date","find_date","weather","temperature","humidity",
	              "location","bad_level","protective_action","protective_device","pre_situation","in_situation","other_time"};
	   try{
		   BufferedReader reader =new BufferedReader(new FileReader(file_path));
		   String line = "";
		   while ((line = reader.readLine()) != null) { 
			    String[] result_info = line.split(" ");
			    /*
			    JSONObject info = new JSONObject();
			    for(int i=0;i<result_info.length;i++){
	        		info.put(columns[i], result_info[i]);
	        	}
			    result_infos.put(info);
			    */
			    result_infos.put(result_info);
	
	        }  
		   reader.close();

	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	   return result_infos;
   }
   
   public List<TemplateFillTask> getAllTasks(){
		ArrayList<TemplateFillTask> tasks = new ArrayList<TemplateFillTask>();	
		File file = new File(ProjectPathPrefix);
		if(file.exists()){
			for(File file2 : file.listFiles()){
				if(file2.isDirectory()){
					String taskname = file2.getName();
					TemplateFillTask task = getTask(taskname);
					if(task != null){
						tasks.add(task);
					}
				}
			}
		}

		return tasks;
   }
   
   
   public List<String> getTaskFiles(String taskname){
	   List<String> files = new ArrayList<String>();
	   File file = new File(ProjectPathPrefix+"/"+taskname+"/original");
		if(file.exists()){
			for(File file2 : file.listFiles()){
				files.add(file2.getName());
			}
		}
	   
	   return files;
   }
   
   /**
    * 查找操作。因为任务数量相对而言很少，所以直接从文件系统中读取。后续可能根据需求换成对数据库读取
    * @param time
    * @return
    */
   public List<TemplateFillTask> searchTaskByTime(String beginTime, String endTime){
	   List<TemplateFillTask> tasks = getAllTasks();
	   LocalDate begin = LocalDate.parse(beginTime);
	   LocalDate end = LocalDate.parse(endTime);
	   List<TemplateFillTask> res = new ArrayList<TemplateFillTask>();
	   for(TemplateFillTask task : tasks){
		   if(task.getDate().compareTo(begin)>=0 && task.getDate().compareTo(end)<=0){
			   res.add(task);
		   }
	   }
	   return res;
   }
   
   /**
    * 查找操作。
    * @param type
    * @return
    */
   public List<TemplateFillTask> searchTaskByType(String type){
	   List<TemplateFillTask> tasks = getAllTasks();
	   List<TemplateFillTask> res = new ArrayList<TemplateFillTask>();
	   for(TemplateFillTask task : tasks){
		   if(task.getType().equals(type)){
			   res.add(task);
		   }
	   }
	   return res;
   }
   
   public static void main(String[] args){
	  TemplateFillService service = new TemplateFillService();
	  
	  System.out.println(service.getResult("test"));
   }

   
   
}
