package io.github.jhipster.sample.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.python.antlr.PythonParser.factor_return;
import org.springframework.web.multipart.MultipartFile;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.util.FactoryUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

public class AlgorithmTaskFileService {
	private FactoryUtil factor = FactoryUtil.getFactory();
    private  FileUtil fileUtil = new FileUtil();
    private HDFSFileUtil hdfsFileUtil = factor.getHDFSUtil();
    private SparkUtil sparkUtil = factor.getSparkUtil();


    public List<String> getTaskFiles(String project, String task, String dataType, boolean hdfs) throws IOException{
    	if(hdfs){
    		String path = hdfsFileUtil.root()+project+'/'+task+'/'+dataType+'/';
    		return hdfsFileUtil.list(path);
    	}
		String task_path = fileUtil.ProjectPathPrefix + project + '/' +task;
		String file_dir = task_path + '/' + dataType;
		return fileUtil.list_files(file_dir);
    }
    
    public boolean deleteTaskFile(String project, String task, String dataType, String fileName, boolean hdfs){
    	if(hdfs){
    		String path = hdfsFileUtil.root()+project+'/'+task+'/'+dataType+'/'+fileName;
    		try {
				return hdfsFileUtil.delFile(path, hdfs);
			} catch (IllegalArgumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
    	}
    	String file_path = fileUtil.ProjectPathPrefix+project+'/'+task+'/'+dataType+'/'+fileName;
		File file = new File(file_path);
		file.delete();
		return file.delete();
		
    }
	public boolean upload(MultipartFile file, String project, String task, String dataType, boolean hdfs){
        String task_path = fileUtil.ProjectPathPrefix + project + '/' + task;
        String hdfs_path = hdfsFileUtil.root() + project + '/' + task + '/';
        boolean flag = false;
        try{
	        flag = fileUtil.uploadFile(file, task_path + "/" + dataType + '/');
        	if(hdfs){
        	 String filename = file.getOriginalFilename();
        	 System.out.println(filename);
	         hdfsFileUtil.upload(task_path +'/' + dataType +'/' + filename, hdfs_path+dataType, true);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }
        return flag;
		
	}
	
	public File download(String project, String task, String dataType, String fileName, boolean hdfs){
        String task_path = fileUtil.ProjectPathPrefix + project + '/'+task+'/'+dataType+'/'+fileName;
        String hdfs_path = hdfsFileUtil.root() + project + '/' +task+'/'+dataType+'/'+fileName;
        boolean flag = false;
        try{
        	
	        if(hdfs){
	        	System.out.println(task_path);
	        	hdfsFileUtil.download(hdfs_path, task_path, false);
	        	return new File(task_path);
	        }
	        else{
	        	return new File(task_path);
	        }
	        
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;

	}
	
	//暂时把header设为默认true
	public List<String> getJsonData(String path, boolean hdfs, String dataFormat) throws Exception{
		String dataType = "LOCAL";
		if(hdfs){
			dataType = "HDFS";
		}
		Dataset<Row> data = sparkUtil.readData(path, dataType, dataFormat);
		List<String> jsonList = data.toJSON().collectAsList();
		return jsonList;
	}
}
