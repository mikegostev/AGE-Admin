package uk.ac.ebi.age.admin.server.service;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.age.admin.server.mng.AgeAdmin;
import uk.ac.ebi.age.admin.shared.Constants;

public class LoginSvc extends HttpServlet
{

 private static final long serialVersionUID = 1L;

 protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
 {
  String uname = req.getParameter("username");
  String pass = req.getParameter("password");
  
  try
  {
   String sessId =  AgeAdmin.getDefaultInstance().login(uname, pass, req.getRemoteAddr()).getSessionKey();
   
   resp.addCookie( new Cookie(Constants.sessionKey, sessId) );
   
  }
  catch (Exception e) 
  {
   resp.getWriter().println("Failed");
   return;
  }
  
  resp.getWriter().println("OK");

 }
}
