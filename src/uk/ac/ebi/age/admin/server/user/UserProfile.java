package uk.ac.ebi.age.admin.server.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserProfile
{
 private boolean isUploadAllowed;
 private String userName;
 private String passwordHash;

 public boolean checkPassword(String password)
 {
  return passwordHash.equals(hashPassword(password));
 }

 public void setPassword(String password)
 {
  passwordHash = hashPassword(password);
 }
 
 public boolean isUploadAllowed()
 {
  return isUploadAllowed;
 }

 private String hashPassword( String pass )
 {
  StringBuffer message = new StringBuffer(100);

  try
  {
   MessageDigest md5d = MessageDigest.getInstance("MD5");

   byte[] digest = md5d.digest(pass.getBytes());

   for(int i = 0; i < digest.length; i++)
   {
    String hex = Integer.toHexString(digest[i]);
    
    if(hex.length() < 2 )
     message.append('0').append(hex.charAt(0));
    else 
     message.append(hex.substring(hex.length()-2));
   }
   
   
   return message.toString();
  }
  catch(NoSuchAlgorithmException ex)
  {
   ex.printStackTrace();
  }
  
  return null;
 }

 public void setUserName(String string)
 {
  userName = string;
 }

 public String getUserName()
 {
  return userName;
 }

 public void setUploadAllowed(boolean isUploadAllowed)
 {
  this.isUploadAllowed = isUploadAllowed;
 }
}
