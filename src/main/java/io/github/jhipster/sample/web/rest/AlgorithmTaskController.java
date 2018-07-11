package io.github.jhipster.sample.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import io.github.jhipster.sample.service.AlgorithmParaService;
import io.github.jhipster.sample.service.AlgorithmTaskFileService;
import io.github.jhipster.sample.service.AlgorithmTaskService;
import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

@RestController
@RequestMapping("/algo_task")
public class  AlgorithmTaskController{
	
    private static AlgorithmTaskService service = new AlgorithmTaskService();    
    private static AlgorithmTaskFileService algo_file_service = new AlgorithmTaskFileService();
    private static AlgorithmParaService para_service = new AlgorithmParaService(); 
    

		
    @GetMapping("/getTasks")
 	@ResponseBody
	public String getTasks(String project) throws JSONException{
		JSONObject res = new JSONObject();
		res.put("tasks", service.getAllTasks(project));
		
		return res.toString();
	}
	
    @GetMapping("/addTask")
  	@ResponseBody
 	public String addTask(String project, String task, String desc) throws JSONException, IllegalArgumentException, IOException{
 		 JSONObject res = new JSONObject();
 	     if (! service.checkProject(project)){
 	    	 res.put("msg","project not exist");
 	    	 res.put("flag",false);
 	     }else{
 	    	 res.put("flag", service.addTask(project, task, desc));
 	     }
         
 		return res.toString();
 	}
    
    @GetMapping("/delTask")
  	@ResponseBody
 	public String delTask(String project, String task) throws JSONException{
 		 JSONObject res = new JSONObject();
 	     if (! service.checkProject(project)){
 	    	 res.put("msg","project not exist");
 	    	 res.put("flag",false);
 	     }else{
 	    	 res.put("flag", service.delTask(project, task));
 	     }
         
 		return res.toString();
 	}
    
    
    @GetMapping("/searchByTime")
  	@ResponseBody
 	public String SearchTaskByTime(String project, String beginTime, String endTime) throws JSONException{
 		JSONObject res = new JSONObject();
 		res.put("tasks", service.searchTaskByTime(project, beginTime, endTime));
 		return res.toString();
 	} 
    
    @GetMapping("/searchByName")
  	@ResponseBody
 	public String SearchTaskByName(String project, String task_name) throws JSONException{
 		JSONObject res = new JSONObject();
 		res.put("tasks", service.searchTaskByName(project,task_name));
 		return res.toString();
 	} 
    
    /**
     * 这个更新后续可根据需求改变
     * @param project
     * @param task_name
     * @return
     * @throws JSONException
     * @throws IOException 
     */
    @GetMapping("/editTask")
  	@ResponseBody
 	public String editTask(String project, String task_name, String desc) throws JSONException, IOException{
 		JSONObject res = new JSONObject();
 		boolean flag = service.editTask(project, task_name, desc);
 		res.put("flag", flag);
 		return res.toString();
 	} 
    
    
    
    
    
	  /**
     * 文件上传具体实现方法, 直接上传文件到Project目录下;
     * @param file
     * @return
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
     */
    @PostMapping("/uploadAlgoTaskFile")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "dataType")  String dataType,
                                   @RequestParam(value = "project")  String project,
                                   @RequestParam(value = "task") String task,
                                   @RequestParam(value = "hdfs") boolean hdfs
                                   ) throws JSONException, UnsupportedEncodingException, IOException{
        JSONObject object = new JSONObject();
        boolean flagUpload = algo_file_service.upload(file, project, task, dataType,hdfs);      
        object.put("result", flagUpload ? "success" : "fail");
        return object.toString();
    }
    
    @GetMapping("/delTaskFile")
  	@ResponseBody
 	public String delTaskFile(String project, String task, String dataType, String fileName, boolean hdfs) throws JSONException{
 		JSONObject res = new JSONObject();
 		boolean flag = algo_file_service.deleteTaskFile(project, task, dataType, fileName, hdfs);
 		res.put("flag", flag);
 		return res.toString();
 	} 
    
    @GetMapping("/getTaskFiles")
  	@ResponseBody
 	public String getTaskFile(String project, String task, String dataType, boolean hdfs) throws JSONException, IOException{
 		JSONObject res = new JSONObject();
 		res.put("files", algo_file_service.getTaskFiles(project, task, dataType, hdfs));
 		return res.toString();
 	} 
    
    @PostMapping("/addPara")
  	@ResponseBody
    public String addParameterConfig(@RequestBody Map<String, Object> data) 
							throws JSONException{
 		JSONObject res = new JSONObject();
 		String project = (String) data.get("project");
 		String task = (String) data.get("task");
 		boolean hdfs = (boolean) data.get("hdfs");
 		Map<String, Object> para = (Map<String, Object>) data.get("para");
 		boolean flag = para_service.addParameterConfig(project, task, para.toString(), hdfs);
 		res.put("flag",flag);
 		return res.toString();
    }

    /**
     * todo: add hdfs default: para
     * @param project
     * @param task
     * @param para
     * @return
     * @throws JSONException
     */
    @GetMapping("/getPara")
  	@ResponseBody
 	public String getPara(String project, String task, String para) throws JSONException{
 		JSONObject res = new JSONObject();
 		res.put("para",para_service.getParameterConfig(project, task, para));
 		return res.toString();
 	} 
    
    @PostMapping("/runLocal")
   	@ResponseBody
     public String runLocal(@RequestBody Map<String, Object> data) 
 							throws JSONException{
  		JSONObject res = new JSONObject();
  		String project = (String) data.get("project");
  		String task = (String) data.get("task");
  		String algo_file = (String) data.get("algo_file");
  		Map<String, Object> para = (Map<String, Object>) data.get("para");
  		String log = para_service.runLocal(project, task, algo_file, para.toString());
  		res.put("log",log);
  		return res.toString();
     }
    
    @GetMapping(value="/download")
	@ResponseBody
	public void FileDownload(String project, String task, String dataType, String fileName, boolean hdfs){
		HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		InputStream inputStream = null;  
        OutputStream outputStream = null;
        
		 try{
				File file= algo_file_service.download(project, task, dataType, fileName, hdfs);
				if(file==null) return;
	        	inputStream = new FileInputStream(file);  
	        	outputStream = new BufferedOutputStream(response.getOutputStream());   
	          
	            byte[] buffer = new byte[inputStream.available()];  
	            inputStream.read(buffer);
	            inputStream.close();
	            
	            response.reset();
	         // 设置response的Header  
	        	//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
	            response.setContentType("multipart/form-data");  
	            response.addHeader("Content-Disposition","attachment;filename=" +file.getName());  
	            response.addHeader("Content-Length", "" + file.length());  
	            
	            outputStream.write(buffer);
	            outputStream.close();
	            outputStream.flush();
	            if(hdfs){
	            	file.delete();
	            }
	        	
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	}

}

