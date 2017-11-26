package io.github.jhipster.sample.service.util;



public class DBConnection {

	private String type;
	private String ip;
	private String port;
	private String database;
	private String username;
	private String password;
	
	public DBConnection(String type, String ip, String port, String database) {
		this.database = database;
		this.ip = ip;
		this.port = port;
		this.type = type;
	}
	
	public DBConnection(String type, String ip, String port, String database,String username, String password) {
		this.database = database;
		this.ip = ip;
		this.port = port;
		this.type = type;
		this.setUsername(username);
		this.setPassword(password);
	}
	
	public String getJdbcDriver(){
		String driver = "";
		switch (type) {
			case "mysql":
				driver = "com.mysql.jdbc.Driver";
				break;
			case "oracle":
				driver = "oracle.jdbc.driver.OracleDriver";
				break;
			case "postgresql":
				driver = "org.postgresql.Driver";
				break;
			case "sqlserver":
				driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
				break;
			
			default:
				break;
		}
		return driver;
	}
	
	public String getJdbcUrl(){
		String url = "";
		switch (type) {
			case "mysql":
				url += "jdbc:mysql://" + ip + ":" + port + "/" + database +"?serverTimezone=UTC";
				break;
			case "oracle":
				url += "jdbc:oracle:thin:@" + ip + ":" + port + ":" + database;
				break;
			case "postgresql":
				url += "jdbc:postgresql://" + ip + ":" + port + "/" + database; 
				break;
			case "sqlserver":
				url += "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + database;
				break;
			
			default:
				break;
		}
		return url;
	}
	
	public static void main(String[] args){
		DBConnection conn = new DBConnection("oracle","127.0.0.1","8080","test");
		System.out.println(conn.getJdbcUrl());
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
}
