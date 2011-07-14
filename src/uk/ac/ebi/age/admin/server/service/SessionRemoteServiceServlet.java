package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.authz.Session;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SessionRemoteServiceServlet extends RemoteServiceServlet
{

 private static final long serialVersionUID = 1L;

 @Override
 protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
 {
  String sessID = null;
  
  sessID = req.getParameter(Configuration.getDefaultConfiguration().getSessionCookieName());
  
  if( sessID == null )
  {
   Cookie[] cuks = req.getCookies();
   
   if (cuks != null && cuks.length != 0)
   {
    for (int i = cuks.length - 1; i >= 0; i--)
    {
     if (cuks[i].getName().equals(Configuration.getDefaultConfiguration().getSessionCookieName()) )
     {
      sessID = cuks[i].getValue();
      break;
     }
    }
   }
  }
  
  Session sess = null;
  
  if( sessID != null )
   sess = Configuration.getDefaultConfiguration().getSessionManager().checkin( sessID );
  

  try
  {
   super.service(req,resp);
  }
  finally
  {
   if( sess != null )
    Configuration.getDefaultConfiguration().getSessionManager().checkout();
  }
 }
}
