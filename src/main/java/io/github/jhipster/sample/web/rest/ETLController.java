package io.github.jhipster.sample.web.rest;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FileUtil;
import org.json.JSONArray;
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
import io.github.jhipster.sample.service.ETLService;
import io.github.jhipster.sample.service.util.Transform;
import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

@RestController
@RequestMapping("/etl")
public class ETLController {
    private static FileUploadingUtil fileUtil = new FileUploadingUtil();
    private static String ProjectPathPrefix = "src/main/webappfiles/Project/etl";


		
	@PostMapping("/runJob")
	@ResponseBody
	public String runJob(@RequestBody Map<String, Object> data) throws JSONException{
		ETLService service = new ETLService();
		String job = service.getJob(data);
		JSONObject res = new JSONObject();
		if(service.runJob(job)){
			res.put("message", "success");
		}
		else{
			res.put("message", "fail");	
		}
		return res.toString();
	}
	@PostMapping("/tables")
	@ResponseBody
	public String getTables(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();
		List<String> tables = service.getTables(data);
		
		JSONArray array = new JSONArray(tables);
		return array.toString();
	}
	@PostMapping("/columns")
	@ResponseBody
	public String getColumns(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();

		List<String> tables = service.getColumns(data);
		
		JSONArray array = new JSONArray(tables);
		return array.toString();
	}
	@PostMapping("/checkConn")
	@ResponseBody
	public String checkConnection(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();
		if(service.checkConn(data)){
			return "success";
		}
		return "fail";
	}
	
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
                                   @RequestParam(value = "skipHead")  boolean skipHead
                                   ) throws JSONException, UnsupportedEncodingException, IOException{
        JSONObject object = new JSONObject();
	    ETLService service = new ETLService();

        String fileName = file.getOriginalFilename();
        String fileExtension = "";
        fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (! ".txt".equals(fileExtension.toLowerCase())){
        	   object.put("result", "fail");
        	   object.put("msg", "invalid file type, must be txt file");
               return object.toString();
        }
        boolean flagUpload;
        try{
            service.deleteDirFiles(ProjectPathPrefix+"/data");
            flagUpload = fileUtil.uploadFile(file,ProjectPathPrefix+"/data/");
        }catch (Exception e){
            object.put("result", "fail");
            return object.toString();
        }
      
        object.put("result", flagUpload ? "success" : "fail");
        if (flagUpload){
            object.put("project_dir", ProjectPathPrefix+"/data");
            InputStreamReader reader = new InputStreamReader(file.getInputStream(),"utf-8");
            BufferedReader br = new BufferedReader(reader);
            String firstLine = br.readLine();
            System.out.println("firstLine:"+firstLine);
            if(skipHead){
            	firstLine = br.readLine();
            }
            if(firstLine == null || firstLine.isEmpty()){
            	object.put("msg", "文件为空");
            }
            else{
                String[] cols;
                if(firstLine.contains(",")){
            	    cols = firstLine.split(",");
                }
                else{
            	    cols = firstLine.split(" ");
                }
                object.put("cols", cols);
            }
        }
        return object.toString();
    }
    /**
     * 运行文本转换
     * @param data
     * @return
     * @throws JSONException
     * @throws IOException
     */
    @PostMapping("/runTextJob")
	@ResponseBody
	public String runTextJob(@RequestBody Map<String, Object> data) throws JSONException, IOException{
	    ETLService service = new ETLService();

    	JSONObject job_res = new JSONObject();
	    JSONObject job = new JSONObject();
	    JSONObject content = new JSONObject();
	    JSONObject reader = new JSONObject();
	    
		List<Map<String,Object>> transform_map = (List<Map<String, Object>>) data.get("transform");
	    Transform transform = service.getTransform(transform_map);
		content.put("transformer", new JSONArray(transform.toString()));

	    JSONObject parameter = new JSONObject();
	    reader.put("name", "txtfilereader");
	    ArrayList<String> path = new ArrayList<String>();
	    path.add(ProjectPathPrefix+"/data");
	    parameter.put("path", path);
	    parameter.put("column", data.get("column"));
	    parameter.put("fieldDelimiter", ",");
	    reader.put("parameter", parameter);
	    
	    JSONObject writer = new JSONObject();
	    JSONObject writer_para = new JSONObject();
	    writer_para.put("path",ProjectPathPrefix+"/result");
	    writer_para.put("fileName", "result");
	    writer_para.put("writeMode", "truncate");
	    writer_para.put("fileFormat", "text");
	    writer.put("name", "txtfilewriter");
	    writer.put("parameter", writer_para);
	    
	    content.put("reader", reader);
	    content.put("writer", writer);
	    
	    JSONArray arr = new JSONArray();
	    arr.put(content);
	    job.put("content", arr);
	    
	    JSONObject setting = new JSONObject();
		JSONObject speed = new JSONObject();
		try{
		speed.put("channel", "1");
		setting.put("speed", speed);
		}catch (Exception e) {
			e.printStackTrace();
		}
		job.put("setting", setting);
		
	    job_res.put("job", job);
	    
		JSONObject res = new JSONObject();

		service.deleteDirFiles(ProjectPathPrefix+"/result");
		
		if(service.runJob(job_res.toString())){
			res.put("message", "success");
		}
		else{
			res.put("message", "fail");	
		}
		String result_etl_file_path = "";
		File dirFile = new File(ProjectPathPrefix+"/result");
		for(File file : dirFile.listFiles()){
        	result_etl_file_path = file.getAbsolutePath();
        }
		res.put("result_file_path", result_etl_file_path);
		return res.toString();
	}
    @GetMapping(value="/download")
	@ResponseBody
	public void FileDownload(String result_file_path){
		HttpServletResponse response=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		InputStream inputStream = null;  
        OutputStream outputStream = null;
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
