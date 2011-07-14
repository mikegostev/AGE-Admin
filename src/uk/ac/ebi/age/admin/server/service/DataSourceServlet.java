package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceBackendService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceRequest.RequestType;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceResponse;
import uk.ac.ebi.age.admin.shared.ds.DSDef;
import uk.ac.ebi.age.admin.shared.ds.DSField;
import uk.ac.ebi.age.authz.Session;

import com.pri.util.StringUtils;
import com.pri.util.collection.MapIterator;
import com.smartgwt.client.rpc.RPCResponse;

public class DataSourceServlet extends ServiceServlet
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

 protected void service(HttpServletRequest request, HttpServletResponse response, Session sess ) throws ServletException, IOException
 {
//  String query = request.getQueryString();
  
//  System.out.println("Query: "+query);
  DataSourceRequest dsr = new DataSourceRequest();
 
  Enumeration<?> prmNames = request.getParameterNames();
  
  while( prmNames.hasMoreElements() )
  {
   String pName = prmNames.nextElement().toString();
   
   for(String pv : request.getParameterValues(pName))
    System.out.println(pName+"="+pv);
   
   dsr.addRequestParameter(pName, request.getParameter(pName));
  }
  
  String dest  = request.getParameter("_dataSource");
  
  String prm = request.getParameter("_operationType");
  
  if( "fetch".equals(prm) )
   dsr.setRequestType( RequestType.FETCH );
  else if( "update".equals(prm) )
   dsr.setRequestType( RequestType.UPDATE );
  else if( "add".equals(prm) )
   dsr.setRequestType( RequestType.ADD );
  else if( "remove".equals(prm) )
   dsr.setRequestType( RequestType.DELETE );
  else
  {
   sendInvRequetError(response);
   return;
  }
  
  prm = request.getParameter("_endRow");
  
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
  
  DataSourceResponse dsresp = srv.processRequest(dsr);
  
  try
  {
   
   response.setContentType("application/json; charset=utf-8");
  
   ServletOutputStream out = response.getOutputStream();
   
   
   OutputStreamWriter outw = new OutputStreamWriter(out, Charset.forName("UTF-8"));
 
   if( dsr.getRequestType() != RequestType.FETCH )
   {
    if( dsresp.getErrorMessage() != null )
     out.print("{" +
       "response:{" +
       " status: " + RPCResponse.STATUS_LOGIN_SUCCESS+
       ", data: '"+StringUtils.escapeByBackslash(dsresp.getErrorMessage(), '\'')+"'"+
       "}}");
    else
     out.print("{" +
        "response:{" +
        " status: 0}}");
    
    return;
   }
 
   outw.write("{" +
     "response:{" +
     " status: 0" +
     ", startRow: " +dsr.getBegin()+
     ", endRow: " +(dsr.getBegin()+dsresp.getSize())+
     ", totalRows: " +dsresp.getTotal()+
     ", data: ["
   
   );
   
   MapIterator<DSField, String> dataIter = dsresp.getIterator();
   
   boolean first = true;
   while( dataIter.next() )
   {
    if( first )
    {
     outw.write('{');
     first=false;
    }
    else
     outw.write(",{");
    
    for( DSField dsf : dsd.getFields() )
    {
     String val = dataIter.get(dsf);
     
     val = val==null?"":StringUtils.escapeByBackslash(val,'\'');
     
     outw.write("'"+StringUtils.escapeByBackslash(dsf.getFieldId(),'\'')+"': '"+val+"',");
    }
     
    outw.write("}");
 
   }
  
   outw.write("] }}"); 
 
   outw.close();
  }
  finally
  {
   dsresp.release();
  }
 }

 private void sendInvRequetError(HttpServletResponse response) throws IOException
 {
  ServletOutputStream out = response.getOutputStream();

  out.print("{" +
    "response:{" +
    " status: " + RPCResponse.STATUS_FAILURE +
    ", data: 'Invalid request'"+
    "}}");

 }

 private void sendNoServiceError(HttpServletResponse response) throws IOException
 {
  ServletOutputStream out = response.getOutputStream();

  out.print("{" +
    "response:{" +
    " status: " + RPCResponse.STATUS_FAILURE +
    ", data: 'Service Not Supported'"+
    "}}");
 }

}
