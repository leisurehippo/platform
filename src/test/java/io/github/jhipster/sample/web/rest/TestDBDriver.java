package io.github.jhipster.sample.web.rest;

public class TestDBDriver {

	public static void main(String[] args) throws ClassNotFoundException{
		
		String oracleDriver = "oracle.jdbc.driver.OracleDriver";
		String sqlserverDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String postsqlDriver = "org.postgresql.Driver";
		Class.forName(oracleDriver);
		Class.forName(postsqlDriver);
		Class.forName(sqlserverDriver);
		System.out.println("all drivers are avaliable!");
	}
}
