package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.AgeAdmin;

public class InstanceIdServlet extends HttpServlet
{
 private static final long serialVersionUID = 1L;

 @Override
 protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException
 {
  resp.setContentType("text/plain");
  
  resp.getWriter().println( AgeAdmin.getDefaultInstance().getInstanceName());
 }

}
