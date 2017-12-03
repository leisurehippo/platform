package io.github.jhipster.sample.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.jhipster.sample.config.ETLConfig;
import io.github.jhipster.sample.service.util.DBConnection;
import io.github.jhipster.sample.service.util.DBSource;
import io.github.jhipster.sample.service.util.DBUtil;
import io.github.jhipster.sample.service.util.EtlJobUtil;
import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.service.util.Parameter;
import io.github.jhipster.sample.service.util.Transform;
import io.github.jhipster.sample.service.util.TransformItem;

@Service
public class ETLService {

	@SuppressWarnings("unchecked")
	public String getJob(Map<String, Object>data){
		Map<String,Object> reader_map = (Map<String, Object>) data.get("reader");
		Map<String,Object> writer_map = (Map<String, Object>) data.get("writer");
		List<Map<String,Object>> transform_map = (List<Map<String, Object>>) data.get("transform");
		
		DBSource reader = getSource(true, reader_map);
		DBSource writer = getSource(false, writer_map);
		Transform transform = getTransform(transform_map);
		String job = EtlJobUtil.getJob(reader, writer, transform);
		System.out.println(job);
		return job;
	}
	
	public boolean runJob(String job, String dataXPath, String jsonPath){
		FileUtil.saveToFile(jsonPath, job);
		boolean flag = EtlJobUtil.runJob(dataXPath, jsonPath);
		FileUtil.removeFile(jsonPath);
		return flag;
	}
	
	public boolean runJob(String job){
		// 这两个可以从配置文件中读取，也可以直接在这里配置，这里暂时直接设置
		String url = ETLConfig.DATAXURL;
		String dataXPath = url + "/bin/datax.py";
		String jsonPath = url + "/bin/job.json";
		return runJob(job, dataXPath, jsonPath);
	}
	
	@SuppressWarnings("unchecked")
	public DBSource getSource(boolean reader,Map<String,Object> source){
		String dbType = (String) source.get("type");
		String username = (String) source.get("username");
		String password = (String) source.get("password");
		DBConnection dbConn = new DBConnection(dbType, (String)source.get("ip"), (String)source.get("port"),(String)source.get("database"));
		List<String> column = (List<String>) source.get("column");
		String table = (String) source.get("table");
		Parameter parameter = new Parameter(reader, username, password, dbConn, table, column);
		
		return new DBSource(reader, dbType, parameter);
	}
	
	@SuppressWarnings("unchecked")
	public Transform getTransform(List<Map<String, Object>> transform_map){
		Transform transform = new Transform();
		for (Map<String,Object> item : transform_map){
			String name = (String) item.get("name");
			int columnIndex=  (int) item.get("columnIndex");
			List<String> paras = (List<String>) item.get("paras");
			TransformItem tItem = new TransformItem(name, columnIndex, paras);
			transform.add(tItem);
		}
		return transform;
	}
	
	public List<String> getTables(Map<String, Object> data){
		String dbType = (String) data.get("type");
		String username = (String) data.get("username");
		String password = (String) data.get("password");
		String database = (String) data.get("database");
		String ip = (String)data.get("ip");
		String port = (String)data.get("port");
		
		DBConnection dbConn = new DBConnection(dbType, ip, port, database, username, password);
		return DBUtil.getTables(dbConn);
	}
	
	public List<String> getColumns(Map<String, Object> data){
		String dbType = (String) data.get("type");
		String username = (String) data.get("username");
		String password = (String) data.get("password");
		String database = (String) data.get("database");
		String ip = (String)data.get("ip");
		String port = (String)data.get("port");
		String tableName = (String)data.get("tableName");
		
		DBConnection dbConn = new DBConnection(dbType, ip, port, database, username, password);
		return DBUtil.getColumnsOfTable(dbConn, tableName);
	}
	
	public boolean checkConn(Map<String, Object> data){
		String dbType = (String) data.get("type");
		String username = (String) data.get("username");
		String password = (String) data.get("password");
		String database = (String) data.get("database");
		String ip = (String)data.get("ip");
		String port = (String)data.get("port");
		
		DBConnection dbConn = new DBConnection(dbType, ip, port, database, username, password);
		return DBUtil.checkConn(dbConn);
				
	}
}
