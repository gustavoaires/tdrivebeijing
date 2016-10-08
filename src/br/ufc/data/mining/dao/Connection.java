package br.ufc.data.mining.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

	
	private static java.sql.Connection CONNECTION;
	
	private Connection(){
		
	}
	
	public static java.sql.Connection getConnection(){
		
		if(CONNECTION == null){
			try{
				Class.forName("org.postgresql.Driver");
			}catch(ClassNotFoundException e){
				e.getMessage();
			}
			
			String url = "jdbc:postgresql://localhost:5432/drive";
			String usr = "postgres";
			String password = "root";
			try{
				Connection.CONNECTION = DriverManager.getConnection(url, usr,password);
			}catch(SQLException e2){
				e2.printStackTrace();
			}
			
		}
		return Connection.CONNECTION;
	}
}
