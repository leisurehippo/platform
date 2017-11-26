package io.github.jhipster.sample.service.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parameter {
	private boolean reader;
	private String username;
	private String password;
	private List<String> column;
	private DBConnection dbConn;
	private String table;
	//private String writeMode; //直接使用insert
	
	public boolean isReader() {
		return reader;
	}
	public void setReader(boolean reader) {
		this.reader = reader;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getColumn() {
		return column;
	}
	public void setColumn(List<String> column) {
		this.column = column;
	}
	public DBConnection getDbConn() {
		return dbConn;
	}
	public void setDbConn(DBConnection dbConn) {
		this.dbConn = dbConn;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	public Parameter(boolean reader, String username, String password, DBConnection conn,
			 String table, List<String>column){
		this.reader = reader;
		this.username = username;
		this.password = password;
		this.dbConn = conn;
		this.table = table;
		this.column = column;
	}
	
	public String toString(){
		JSONObject res = new JSONObject();
		try{
			res.put("username",username);
			res.put("password", password);
			res.put("column", column);
			if(!reader){
				res.put("writeMode", "insert");
			}
			
			//为了简单起见，jdbcUrl和table只能为单个。
			JSONArray conn = new JSONArray();
			JSONObject connOb = new JSONObject();
			if(reader){
				List<String> jdbcUrls = new ArrayList<String>();
				jdbcUrls.add(dbConn.getJdbcUrl());
				connOb.put("jdbcUrl", jdbcUrls);
			}
			else{
				connOb.put("jdbcUrl", dbConn.getJdbcUrl());
			}
			List<String> tables = new ArrayList<String>();
			tables.add(table);
			connOb.put("table", tables);
			
			conn.put(connOb);
			res.put("connection", conn);
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
	}
	

	
}
