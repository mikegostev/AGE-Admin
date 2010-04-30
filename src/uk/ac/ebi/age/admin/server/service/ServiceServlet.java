package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.user.Session;

public abstract class ServiceServlet extends HttpServlet
{


 private static final long serialVersionUID = 1L;

 @Override
 protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
 {
  String sessID = null;
  
  Cookie[] cuks = req.getCookies();

  if (cuks.length != 0)
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

  if (sessID == null)
  {
   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
   return;
  }

  Session sess = Configuration.getDefaultConfiguration().getSessionPool().getSession( sessID );
  
  if (sess == null)
  {
   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
   return;
  }

  
  service(req,resp,sess);
 }
 
 abstract protected void service(HttpServletRequest req, HttpServletResponse resp, Session sess) throws ServletException, IOException;

}
