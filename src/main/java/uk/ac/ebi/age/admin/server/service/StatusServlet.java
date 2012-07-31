package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.AgeAdmin;

public class StatusServlet extends HttpServlet
{
 private static final long serialVersionUID = 1L;

 @Override
 protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException
 {
  resp.setContentType("text/plain");
  
  if( AgeAdmin.getDefaultInstance().isOnlineMode() )
   resp.getWriter().println("OK");
  else
   resp.getWriter().println("OFFLINE");
 }

}
