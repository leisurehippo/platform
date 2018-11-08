package io.github.jhipster.sample.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.model.SparkTransform;
import io.github.jhipster.sample.web.rest.util.FactoryUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

public class AlgorithmPrepreocessService {
	
	private FactoryUtil factory = FactoryUtil.getFactory();
	private HDFSFileUtil hdfsUtil = factory.getHDFSUtil();
	private SparkUtil sparkUtil = factory.getSparkUtil();
    private FileUtil fileUtil = new FileUtil();
    private SparkTransform transform = new SparkTransform();

    
	/**
	 * Preview the first n rows of file
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
    
    public List<String> supported(){
    	List<String> result = Arrays.asList(transform.support_transforms);
    	return result;
    } 
    
	public JSONObject previewFile(String project, String task, String dataType,
			String fileName, boolean hdfs, String delimiter, boolean header) throws IOException, JSONException{
		JSONObject result = new JSONObject();
		SparkSession spark = sparkUtil.getSession();
		String format = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		String prefix =  hdfsUtil.root();
		String path = prefix + project + "/" + task + "/" + dataType + "/" + fileName;
		if(!hdfs){
			String pattern = "yyyy_MM_dd_HH_mm_ss";
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			String date = dateFormat.format(new Date());
			String newFileName = fileName.substring(0,fileName.lastIndexOf(".")) + date + "."+format;
			String localPath = fileUtil.ProjectPathPrefix +  project + "/" + task + "/" + dataType + "/" + fileName;
			path = prefix + project + "/" + task + "/" + dataType + "/" + newFileName;
			try {
				hdfsUtil.upload(localPath, path, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
				
		Dataset<Row> data = null;
		System.out.println(format);
		if(format.equals("txt") || format.equals("csv")){
			data = spark.read().option("delimiter", delimiter).option("header", header).option("inferSchema",true).format("csv").load(path);
		}
		else if(format.equals("json")){
			data = spark.read().format("json").load(path);
		} 
		if(data!=null){	
			String info = data.showString(5, true);
			result.put("info", info);
			result.put("columns", data.columns());
			data.printSchema();
		}
		if(!hdfs){
			hdfsUtil.delFile(path, true);
			System.out.println("删除"+path);
		}
		return result;
	}
	
	
	/**
	 * Save dataset to file.
	 * file path must be hdfs path.
	 * @param data
	 * @param hdfsPath
	 * @param type
	 */
	public void save(Dataset<Row> data, String hdfsPath, String type){
		if(type.equals("txt") || type.equals("csv")){
			data.write().mode("Overwrite").option("delimiter", " ").option("inferSchema",true).option("header", true).format("csv").save(hdfsPath);
		}
		else if(type.equals("json")){
			data.write().mode("Overwrite").option("header", true).option("inferSchema",true).format("json").save(hdfsPath);
		}

	}
	
	public JSONObject transform(String project, String task, String dataType,
			String fileName, boolean hdfs, String delimiter, boolean header, String outType,
			String colMaps, String outFileName) throws JSONException, IllegalArgumentException, IOException{
		JSONObject col_json = new JSONObject(colMaps);
		
		JSONArray colMethods = col_json.getJSONArray("colMaps");
		ArrayList<String> log = new ArrayList<String>();
	
		log.add("Begin Transform");
		String file_prefix = fileName.substring(0,fileName.lastIndexOf("."));
		if (outFileName.length()==0){
			outFileName = file_prefix;
		}	
		String projectPath = project + "/" + task + "/" + dataType + "/";
		log.add("original File: "+projectPath+fileName);
		
		JSONObject result = new JSONObject();
		SparkSession spark = sparkUtil.getSession();
		String format = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		String prefix =  hdfsUtil.root();
		String path = prefix + projectPath + fileName;
		String target_path = prefix + projectPath + outFileName + "."+outType;
		
		if(!hdfs){
			String pattern = "yyyy_MM_dd_HH_mm_ss";
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			String date = dateFormat.format(new Date());
			String newFileName = file_prefix + date + "."+format;
			String localPath = fileUtil.ProjectPathPrefix +  projectPath + fileName;
			path = prefix + projectPath + newFileName;
			try {
				hdfsUtil.upload(localPath, path, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
				
		System.out.print(format);
		Dataset<Row> data = null;
		if(format.equals("txt") || format.equals("csv")){
			data = spark.read().option("delimiter", delimiter).option("header", true).option("inferSchema",true).format("csv").load(path);
		}
		else if(format.equals("json")){
			data = spark.read().format("json").option("inferSchema",true).load(path);
		} 
		
		data.show();
		
		log.add("columns and their operations:");
		if(data!=null){	
			for(int i=0;i<colMethods.length();i++){
				JSONObject colInfo = colMethods.getJSONObject(i);
				String colName = colInfo.getString("name");
				String method = colInfo.getString("operation");
				log.add("Column Name: " + colName + ", Operation: "+ method);
				data = transform.transform(data, method, colName);
			}
			String info = data.showString(5, true);
			result.put("info", info);
		}
		
		log.add("target file: " + target_path);
		data.show();
		
		save(data, target_path, outType); 
		if(!hdfs){
			hdfsUtil.delFile(path, true);
		}
		log.add("done");
		result.put("log", log);
		return result;
	}
	
	
}
