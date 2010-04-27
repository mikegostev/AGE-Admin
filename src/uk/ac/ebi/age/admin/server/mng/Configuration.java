package uk.ac.ebi.age.admin.server.mng;

import java.io.File;

import uk.ac.ebi.age.admin.client.common.Constants;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;

public class Configuration
{
 private static Configuration defaultConfig = new Configuration();
 
 public static Configuration getDefaultConfiguration()
 {
  return defaultConfig;
 }

 private UserDatabase userDatabase;
 private SessionPool sessionPool;
 private UploadManager uploadManager;
 private File tmpDir;
 
 
 
 public UserDatabase getUserDatabase()
 {
  return userDatabase;
 }


 public SessionPool getSessionPool()
 {
  return sessionPool;
 }


 public String getSessionCookieName()
 {
  return Constants.sessionCookieName;
 }


 public UploadManager getUploadManager()
 {
  return uploadManager;
 }


 public void setUserDatabase(UserDatabase userDatabase)
 {
  this.userDatabase = userDatabase;
 }


 public void setSessionPool(SessionPool sessionPool)
 {
  this.sessionPool = sessionPool;
 }


 public void setUploadManager(UploadManager uploadManager)
 {
  this.uploadManager = uploadManager;
 }


 public File getTmpDir()
 {
  return tmpDir;
 }
 
 public void setTmpDir( File f)
 {
  tmpDir=f;
 }

}
