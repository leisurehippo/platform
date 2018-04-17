package io.github.jhipster.sample.web.rest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import io.github.jhipster.sample.service.TemplateFillService;
import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

@RestController
@RequestMapping("/templatefill")
public class TemplateFillController {
	   private static FileUploadingUtil fileUtil = new FileUploadingUtil();
	   private static String ProjectPathPrefix = "src/main/webappfiles/Project/TemplateFill";	
	
	
	  /**
     * 文件上传具体实现方法, 直接上传文件到Project目录下;
     * @param file
     * @return
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
     */
    @PostMapping("/uploadETLFile")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "taskname")  String taskname
                                   ) throws JSONException, UnsupportedEncodingException, IOException{
        JSONObject res = new JSONObject();
        String task_path = ProjectPathPrefix +  "/" + taskname;
    	File f = new File(task_path);
    	if (!f.exists()){
    		res.put("flag", false);
    		res.put("msg", "task not exist");
    		return res.toString();
    	}
        String fileName = file.getOriginalFilename();
        String fileExtension = "";
        fileExtension = fileName.substring(fileName.lastIndexOf("."));
        fileExtension = fileExtension.toLowerCase();
        if(".doc".equals(fileExtension) || ".docx".equals(fileExtension) || ".txt".equals(fileExtension)){
        	String original_path = task_path + "/original";
        	File original_dir = new File(original_path);
        	if(! original_dir.exists()){
        		original_dir.mkdir();
        	}
        	boolean flag = fileUtil.uploadFile(file, original_path+"/");
        	res.put("flag", flag);
        }
        else{
        	res.put("flag", false);
        }
          
        return res.toString();
    }
    
    @RequestMapping("/getTasks")
	@ResponseBody
    public String getTasks() throws JSONException{
		JSONObject res = new JSONObject();
		
		List<String> tasks = fileUtil.listDir(ProjectPathPrefix, true);
		res.put("tasks",tasks);
		return res.toString();
    }
    
    @RequestMapping("delTasks")
    @ResponseBody
    public String delTasks(@RequestParam(value="taskname") String taskname){
    	JSONObject res = new JSONObject();
    	
    	String dirPath = ProjectPathPrefix + '/'+taskname;
    	boolean flag = fileUtil.deleteDirectory(dirPath);
    	return res.toString();
    }
    
    @RequestMapping("getTaskFiles")
    @ResponseBody
    public String getTaskFiles(@RequestParam(value="taskname") String taskname) throws JSONException{
    	JSONObject res = new JSONObject();
    	String dir_path = ProjectPathPrefix + "/newdir";
    	List<String> files = fileUtil.listDir(dir_path, false);
    	res.put("files",files);
    	return res.toString();
    }
    
    @RequestMapping("addTask")
    @ResponseBody
    public String addTask(@RequestParam(value="taskname") String taskname) throws JSONException{
    	JSONObject res = new JSONObject();
        File file = new File(ProjectPathPrefix + '/' + taskname);
        if(file.exists()){
        	res.put("flag", false);
        }
        else{
        	res.put("flag", true);
        	file.mkdirs();
        }
    	
    	return res.toString();
    }
    
    public String runTask(@RequestParam(value="taskname") String taskname,
            				@RequestParam(value = "xianlu")  boolean xianlu) throws JSONException{
    	TemplateFillService service = new TemplateFillService();
    	JSONObject res = new JSONObject();
    	String task_path = ProjectPathPrefix + "/" + taskname;
    	boolean flag = service.runTask(task_path, xianlu);
    	res.put("flag", flag);

    	return res.toString();
    }
    
    @GetMapping(value="/download")
	@ResponseBody
	public void FileDownload(@RequestParam(value="taskname") String taskname){
		HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		InputStream inputStream = null;  
        OutputStream outputStream = null;
        String result_file_path = ProjectPathPrefix +"/"+taskname+ "/result.txt";
        System.out.println(result_file_path);
		File file=new File(result_file_path);
		 try{
	        	inputStream = new FileInputStream(file);  
	        	outputStream = new BufferedOutputStream(response.getOutputStream());   
	          
	            byte[] buffer = new byte[inputStream.available()];  
	            inputStream.read(buffer);
	            inputStream.close();
	            
	            response.reset();
	         // 设置response的Header  
	        	//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
	            response.setContentType("multipart/form-data");  
	            response.addHeader("Content-Disposition","attachment;filename=" +"result.txt");  
	            response.addHeader("Content-Length", "" + file.length());  
	            
	            outputStream.write(buffer);
	            outputStream.close();
	            outputStream.flush();
	        	
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	}
    
}
