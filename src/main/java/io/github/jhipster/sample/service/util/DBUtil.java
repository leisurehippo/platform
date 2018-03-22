package io.github.jhipster.sample.service.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
	
	public static Connection getConnection(DBConnection dbConn){
		Connection conn = null;
		try{
			System.out.print(dbConn.getJdbcDriver());

			Class.forName(dbConn.getJdbcDriver());
			conn = DriverManager.getConnection(dbConn.getJdbcUrl(),dbConn.getUsername(), dbConn.getPassword());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConn(Connection conn){
		if (conn == null){
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkConn(DBConnection dbConn){
		System.out.print("check");
		Connection conn = null;
		try{
			Class.forName(dbConn.getJdbcDriver());
			conn = DriverManager.getConnection(dbConn.getJdbcUrl(),dbConn.getUsername(), dbConn.getPassword());
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(conn == null){
			return false;
		}
		closeConn(conn);
		return true;
	}
	
	public static List<String> getTables(DBConnection dbConn){
		List<String> tables = new ArrayList<String>();
		
		Connection conn = null;
		DatabaseMetaData dbmd = null;
		
		try{
			conn = getConnection(dbConn);
			if(conn==null){
				return tables;
			}
			dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
			
			while(rs.next()){
				String tableName = rs.getString("TABLE_NAME");
				System.out.print(tableName);
				tables.add(tableName);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		closeConn(conn);
		return tables;
	}
	
	public static List<String> getColumnsOfTable(DBConnection dbConn, String tableName){
		List<String> columns = new ArrayList<String>();
		Connection conn = null;
		DatabaseMetaData dbmd = null;
		try{
			System.out.println("get columns:" + tableName);
			conn = getConnection(dbConn);
			dbmd = conn.getMetaData();
			//ResultSet rs = dbmd.getColumns(null, getSchema(conn), tableName.toUpperCase(), "%");
			ResultSet rs = dbmd.getColumns(conn.getCatalog(), conn.getSchema(), tableName, "%");
		
			while(rs.next()){
				String colName = rs.getString("COLUMN_NAME");
				System.out.print(colName);
				columns.add(colName);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		closeConn(conn);
		return columns;
	}
	
	private static String getSchema(Connection conn) throws Exception {  
        String schema;  
        //schema = conn.getMetaData().getUserName();  
        schema = conn.getSchema();
        if ((schema == null) || (schema.length() == 0)) {  
            throw new Exception("ORACLE数据库模式不允许为空");  
        }  

        System.out.println("schema"+  conn.getSchema());
        //return schema.toUpperCase().toString();  
        return schema;
  
    }
	


}
