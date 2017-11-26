package io.github.jhipster.sample.service.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class DBSource {
	private String name;
	private Parameter parameter;
	private boolean reader;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public boolean isReader() {
		return reader;
	}

	public void setReader(boolean reader) {
		this.reader = reader;
	}

	public DBSource(boolean reader,String dbType,Parameter parameter){
		this.reader = reader;
		name = getType(reader, dbType);
		this.parameter = parameter;
	}	
	
	private String getType(boolean reader, String dbType){
		String postfix = "";
		if(reader){
			postfix = "reader";
		}
		else{
			postfix = "writer";
		}
		
		return dbType + postfix;
	}
	
	public String toString(){
		JSONObject res = new JSONObject();
		try{
			res.put("name", name);
			JSONObject parameterJson = new JSONObject(parameter.toString());
			res.put("parameter", parameterJson);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res.toString();
	}
	
	public static void main(String[] args){
		DBConnection dbConn = new DBConnection("mysql", "127.0.0.1","123", "test");
		List<String> column = new ArrayList<String>();
		column.add("name");
		column.add("id");
		Parameter parameter = new Parameter(true, "root", "123", dbConn, "user", column);
		System.out.println(parameter.toString());
		
		DBSource reader = new DBSource(true,"mysql",parameter);
		System.out.println(reader.toString());
	}
	
}
