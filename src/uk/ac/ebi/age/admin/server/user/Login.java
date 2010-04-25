package uk.ac.ebi.age.admin.server.user;

import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.server.mng.Configuration;

public class Login
{
 public static Session login(String userName, String password, String clientAddr ) throws UserAuthException
 {
  UserDatabase udb = Configuration.getDefaultConfiguration().getUserDatabase();
  
  UserProfile prof = null;
  
  if( userName == null || userName.length() == 0  )
   prof = udb.getAnonymousProfile();
  else
  {
   prof = udb.getUserProfile( userName );
   
   if( prof == null )
    throw new UserAuthException("Invalid user name");
   
   if( ! prof.checkPassword(password) )
    throw new UserAuthException("Invalid user password");
   
  }

  Session sess = Configuration.getDefaultConfiguration().getSessionPool().createSession( prof, new String[]{ userName, clientAddr } );
  
  return sess;
 }
}
