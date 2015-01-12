package com.testservlet.demo;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestProcess extends HttpServlet {
	
/* Constructor of the object.*/
	public RequestProcess() {
		super();
	}

/*  Destruction of the servlet. */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		 
		try{
		    PrintWriter pw=response.getWriter();
		    String make=request.getParameter("Make");		
		    String yearf=request.getParameter("YearF");
			String yeart=request.getParameter("YearT");
			String conditionf=request.getParameter("ConditionF");
			String milesf=request.getParameter("MilesF");
			String milest=request.getParameter("MilesT");			
			String pricef=request.getParameter("PriceF");
			String pricet=request.getParameter("PriceT");
			String saledate=request.getParameter("SaleDate");
			
			String sql;
						
/*connect to database*/
			
			try{
				Class.forName("org.gjt.mm.mysql.Driver").newInstance();
				
			   // Class.forName("com.MySQL.jdbc.Driver"); 
			}catch(Exception ex){
				pw.println("loading database fail.."+ex.getMessage());
			}
             conn = DriverManager.getConnection( "jdbc:MySQL://127.0.0.1:3306/sakila","root","123456");
             
   /*create statement using result set */
			 st =conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             //st =conn.createStatement();
             sql=GenSql(make,yearf,yeart,conditionf,milesf, milest,pricef,pricet,saledate);
			 
	/*Output the result */		 
             rs=st.executeQuery(sql);
             
			 if(rs.next()){
			     while (rs.next()){				    	 
	        		pw.println(rs.getString(1)+" ");
	        		pw.println(rs.getString(2)+" ");
	        		pw.println(rs.getString(3)+" ");
	        		pw.println(rs.getString(4)+" ");
	        		pw.println(rs.getString(5)+" ");
	        		pw.println(rs.getString(6)+" ");
	        		pw.println(rs.getString(7)+" ");
	        		pw.println(rs.getString(8)+" ");
	        		pw.println(rs.getString(9)+" ");
	        		pw.println(rs.getString(10)+" ");
	        		pw.println(rs.getString(11)+" ");
	        		pw.println(rs.getString(12)+" ");
	        		pw.println(rs.getString(13)+" ");
	        		pw.println(rs.getString(14)+" ");
	        		pw.println(rs.getString(15)+"\n");
				}
			 }else{
				 pw.println("No record");}
				 //response.sendRedirect("RequestPage");  //the url of the aimming servlet
	   
         }catch(Exception ex){
	        ex.printStackTrace();	
         }finally
            {
/*close connection to database*/
            	try{
            	if(rs!=null){rs.close();}
            	if(st!=null){st.close();}
            	if(conn!=null){conn.close();}
            	}catch(Exception ex){
                     ex.printStackTrace();
            	}
            }
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	/*Generate SQL query*/
	public static String GenSql(String make, String yearf, String yeart, String conditionf, String milesf, String milest,
			String pricef, String pricet, String saledate){
		
		    if(make=="" && yearf=="" && yeart=="" && conditionf=="" && milesf=="" && milest=="" && pricef=="" && pricet=="" && saledate==""){
		    	String sql = "SELECT * from car";
		        return sql;
		        }
		
		    String sql = "SELECT * from car where ";
		
		    if(make!=""){sql=sql+ "Make='" + make +"' ";}
		    
		    if(yearf!=""){
		        if(make!=""){sql=sql+"and";}	
		    	sql=sql+ "Year>=" +yearf+" ";
		        }
		    
		    if(yeart!=""){sql=sql+ "and Year<=" +yeart+" ";}
		    if(conditionf!=""){sql=sql+ "and 'Condition'>=\"" +conditionf+"\" " ;}

		    if(milesf!=""){sql=sql+ "and Miles>=" +milesf+" ";}
		    if(milest!=""){sql=sql+ "and Miles<=" +milest+" ";}
		    
		    if(pricef!=""){sql=sql+ "and Price>=" +pricef+" ";}
		    if(pricet!=""){sql=sql+ "and Price<=" +pricet+" ";}
		    if(saledate!=""){sql=sql+ "and SaleDate='" + saledate +"'";}	
		    
		    return sql;
		}
		
}
	
	


