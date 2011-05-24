package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponce;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;

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

 protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
  
  String dest  = request.getParameter(Constants.dsServiceParam);
  
  DataSourceRequest dsr = new DataSourceRequest();
  
  String prm = request.getParameter("_endRow");
  
  if( prm != null )
   dsr.setEnd( Integer.parseInt(prm) );
  else
   dsr.setEnd(-1);

  prm = request.getParameter("_startRow");
  
  if( prm != null )
   dsr.setBegin( Integer.parseInt(prm) );

  DataSourceBackendService srv = Configuration.getDefaultConfiguration().getDataSourceServiceRouter().getService(dest);
  
  if( srv == null )
  {
   sendNoServiceError(response);
   return;
  }
  
  DSDef dsd = srv.getDSDefinition();
  
  for( DSField dsf : dsd.getFields() )
  {
   String val = request.getParameter(dsf.getFieldId());
   
   if( val != null )
    dsr.addFieldValue( dsf, val );
  }
  
  DataSourceResponce dsresp = srv.processRequest(dsr);
  
  Configuration.getDefaultConfiguration().getDataSourceServiceRouter().processRequest(dest, dsr );
  
  response.setContentType("application/json");
  response.getOutputStream().print("" +
  		" { " +
  		"response:{" +
  		" status: 0," +
  		" startRow: 0," +
  		" endRow: 1," +
  		" totalRows: 200," +
  		" data: [" +
  		"{ userid: 'vaspup', username: 'Vasya Pupkin'}," +   
        "{ userid: 'glasha', username: 'Glasha Lozhkina'},");

  int endRow = Integer.parseInt(request.getParameter("_endRow"));
  
  for( int i=2; i < endRow; i++ )
  {
   response.getOutputStream().print("{ userid: 'vaspup"+i+"', username: 'Vasya Pupkin "+i+"'},");
  }
  
  response.getOutputStream().print("] }}"); 

 }

 private void sendNoServiceError(HttpServletResponse response)
 {
  // TODO Auto-generated method stub
  
 }

}
