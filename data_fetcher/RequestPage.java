package com.testservlet.demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestPage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RequestPage() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try{
			PrintWriter pw=response.getWriter();
			pw.println("<!DOCTYPE html>");
			pw.println("<html>");
			pw.println("<body>");
			pw.println("<h1>search page</h1>");
			pw.println("<form action=RequestProcess method=get>");
			pw.println("Make *:<input type=text name=Make><br>");
			pw.println("<br>");
			pw.println("Year From *:<input type=number name=YearF min=1990  max=2015>");
			pw.println("Year To *:<input type=number name=YearT min=1990  max=2015><br/>");
			pw.println("<br>");
			pw.println("Condition Higher Than *:<input type=number name=ConditionF  min=0.0  max=4><br>");
			pw.println("<br>");
			pw.println("Miles From *:<input type=number name=MilesF  min=0  max=300000>");
			pw.println("Miles To *:<input type=number name=MilesT min=0  max=300000><br/>");
			pw.println("<br>");
			/*
			pw.println("Color:<input type=checkbox name=Color value=Black><br>");
			pw.println("Color:<input type=checkbox name=Color value=White><br>");
			pw.println("Color:<input type=checkbox name=Color value=Silver><br>");
			pw.println("Color:<input type=checkbox name=Color value=Gray><br>");
			pw.println("Color:<input type=checkbox name=Color value=Blue><br>");
			pw.println("Color:<input type=checkbox name=Color value=Red><br>");
			pw.println("Color:<input type=checkbox name=Color value=Brown><br>");
			pw.println("Color:<input type=checkbox name=Color value=Orange><br>");
			pw.println("Color:<input type=checkbox name=Color value=Green><br>");
			pw.println("Color:<input type=checkbox name=Color value=Burgundy><br>");
			pw.println("Color:<input type=checkbox name=Color value=Beige><br>");
			*/
			
			pw.println("Price From *:<input type=number name=PriceF min=0  max=100000>");
			pw.println("Price To *:<input type=number name=PriceT min=0  max=100000><br/>");
			pw.println("<br>");
			pw.println("SaleDate *:<input type=date name=SaleDate><br>");
			pw.println("<br>");
			pw.println("<input type=submit value=Search><br/>");
			pw.println("<br>");
			pw.println("<input type=reset value=reset><br/>");
			pw.println("</form>");
			pw.println("</body>");
			pw.println("</html>");
		   
	   }catch(Exception ex){
		   ex.printStackTrace();	   
	   }
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
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

}
