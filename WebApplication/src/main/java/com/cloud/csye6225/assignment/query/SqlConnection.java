///**
// * 
// */
//package com.cloud.csye6225.assignment.query;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// * @author jainh
// *
// */
//public class SqlConnection {
//	 public static Connection getConnection(){
//	        String driver="com.mysql.cj.jdbc.Driver";   
//	        String url="jdbc:mysql://localhost:3306/users_database?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; 
//	        String name="root";
//	        String pwd="password";
//	        try{
//	            Class.forName(driver);
//	            Connection conn=DriverManager.getConnection(url,name,pwd);
//	            return conn;
//	        }catch(ClassNotFoundException e){
//	            e.printStackTrace();
//	            return null;
//	        }catch(SQLException e){
//	            e.printStackTrace();
//	            return null;
//	        }
//	    }
//
//	    public static void closeAll(Connection conn,PreparedStatement ps,ResultSet rs){
//	        try{
//	            if(rs!=null){
//	                rs.close();
//	            }
//	        }catch(SQLException e){
//	            e.printStackTrace();
//	        }
//	        try{
//	            if(ps!=null){
//	                ps.close();
//	            }
//	        }catch(SQLException e){
//	            e.printStackTrace();
//	        }
//	        try{
//	            if(conn!=null){
//	                conn.close();
//	            }
//	        }catch(SQLException e){
//	            e.printStackTrace();
//	        }
//	    }
//
//	    public static void main(String[] args) throws SQLException
//	    {
//
//	        Connection cc=SqlConnection.getConnection();
//
//	        if(!cc.isClosed())
//
//	            System.out.println("Succeeded connecting to the Database!");
//	        Statement statement = cc.createStatement();
//	        String sql = "select * from users";
//	        ResultSet rs = statement.executeQuery(sql);
//	        while(rs.next()) {
//	            System.out.println(rs.getString("id")+"");
//	        }
//
//
//	    }
//}
