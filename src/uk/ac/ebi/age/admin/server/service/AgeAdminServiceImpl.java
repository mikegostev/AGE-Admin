package uk.ac.ebi.age.admin.server.service;

import javax.servlet.http.HttpServletRequest;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.server.user.Login;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AgeAdminServiceImpl extends RemoteServiceServlet implements AgeAdminService
{

 @Override
 public String login(String uname, String pass) throws UserAuthException
 {
  HttpServletRequest req = getThreadLocalRequest();
    
  return Login.login(uname, pass, req.getRemoteAddr()).getSessionKey();
 }

}
