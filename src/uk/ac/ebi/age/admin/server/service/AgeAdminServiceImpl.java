package uk.ac.ebi.age.admin.server.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.server.mng.AgeAdmin;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AgeAdminServiceImpl extends RemoteServiceServlet implements AgeAdminService
{
 private AgeAdmin adm;
 
 public void init() throws ServletException
 {
  super.init();
  
  adm=AgeAdmin.getDefaultInstance();
 }

 @Override
 public String login(String uname, String pass) throws UserAuthException
 {
  HttpServletRequest req = getThreadLocalRequest();
    
  return adm.login(uname, pass, req.getRemoteAddr()).getSessionKey();
 }


 @Override
 public ModelImprint getModelImprint()
 {
  return adm.getModelImprint();
 }
}
