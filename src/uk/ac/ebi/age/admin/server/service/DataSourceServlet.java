package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.client.ClientConfig;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;

public class DataSourceServlet extends HttpServlet
{
 private static final long serialVersionUID = 1L;

 /**
  * @see HttpServlet#HttpServlet()
  */
 public DataSourceServlet()
 {
  super();
  // TODO Auto-generated constructor stub
 }

 /**
  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
  *      response)
  */
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
 {
  // TODO Auto-generated method stub
 }

 /**
  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
  *      response)
  */
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
 {
  String query = request.getQueryString();
  
  System.out.println("Query: "+query);
  
  Enumeration<?> prmNames = request.getParameterNames();
  
  while( prmNames.hasMoreElements() )
  {
   String pName = prmNames.nextElement().toString();
   
   for(String pv : request.getParameterValues(pName))
    System.out.println(pName+"="+pv);
  }
  
  String dest  = request.getParameter(ClientConfig.dsServiceParam);
  
  DataSourceRequest dsr = new DataSourceRequest();
  
  Configuration.getDefaultConfiguration().getDataSourceServiceRouter().processRequest(dest, dsr );
 }

}
