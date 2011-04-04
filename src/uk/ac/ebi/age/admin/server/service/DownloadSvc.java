package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.mng.FileSource;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.shared.Constants;


public class DownloadSvc extends ServiceServlet
{

 @Override
 protected void service(HttpServletRequest req, HttpServletResponse resp, Session sess) throws ServletException, IOException
 {
  if( ! sess.getUserProfile().isDownloadsAllowed() )
  {
   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
   return;
  }
  
  boolean isGet = false;
  
  if( req.getMethod().equalsIgnoreCase("GET") )
   isGet = true;
  else if( ! req.getMethod().equalsIgnoreCase("HEAD") )  
  {
   resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
   return;
  }
  
  Map<String,String> paramMap = new HashMap<String,String>();
  
  Enumeration<String> pn = req.getParameterNames();
  String handler = null;
  
  while( pn.hasMoreElements() )
  {
   String param = pn.nextElement();
   
   if( param.equals(Constants.downloadHandlerParameter))
    handler = req.getParameter( param );
   else
    paramMap.put(param, req.getParameter( param ) );
  }
  
  if( handler == null )
  {
   resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
   return;
  }
  
  FileSource fc = Configuration.getDefaultConfiguration().getFileSourceManager().getFileSource( handler );
  
  File f = fc.getFile(paramMap);
  
  if( f == null )
  {
   resp.sendError(HttpServletResponse.SC_NOT_FOUND);
   return;
  }

  Downloader.processRequest(req, resp, isGet, f, getServletContext().getMimeType(f.getName()) );
  
 }

}
