package com.increff.employee.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class connect{
	public static void main(String[] agrs) throws ClassNotFoundException,SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/employee","ujjwal","Ujjwal@123");
		Statement stmt=con.createStatement();
		
		ResultSet rs=stmt.executeQuery("Select * from employeepojo");
		while(rs.next())
		{
			System.out.println(rs.getInt(1)+" "+rs.getString(2));
		}
		con.close();
	}
}