package uk.ac.ebi.age.admin.server.service;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.server.mng.AgeAdmin;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.user.Session;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AgeAdminServiceImpl extends RemoteServiceServlet implements AgeAdminService
{
 private AgeAdmin adm;
 
 public void init() throws ServletException
 {
  super.init();
  
  adm=AgeAdmin.getDefaultInstance();
 }
 
 private Session getUserSession() throws UserAuthException
 {
  HttpServletRequest req = getThreadLocalRequest();
  for(Cookie cck : req.getCookies() )
  {
   if( cck.getName().equals(Configuration.SESSION_COOKIE_NAME) )
   {
    Session s = adm.getSession(cck.getValue());
    
    if( s != null )
     return s;
   }
  }
  
  throw new UserAuthException("Session expired");
 }

 @Override
 public String login(String uname, String pass) throws UserAuthException
 {
  HttpServletRequest req = getThreadLocalRequest();
  
  String sess = adm.login(uname, pass, req.getRemoteAddr()).getSessionKey();

  getThreadLocalResponse().addCookie( new Cookie(Configuration.SESSION_COOKIE_NAME, sess) );
  
  return sess;
 }


 @Override
 public ModelImprint getModelImprint()
 {
  return adm.getModelImprint();
 }

 @Override
 public ModelStorage getModelStorage() throws UserAuthException
 {
  return adm.getModelStorage( getUserSession() );
 }
}
