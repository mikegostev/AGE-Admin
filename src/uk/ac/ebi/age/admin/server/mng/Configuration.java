package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.sql.Connection;

import uk.ac.ebi.age.admin.client.common.Constants;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;

public class Configuration
{
 public static final String SESSION_COOKIE_NAME = "AGEADMSESS";
 
 public static final String pubResRelPath="public";
 public static final String modelRelPath="model";
 public static final String userRelPath="user";
 public static final String h2DbPath = "h2db";
 
 private static Configuration defaultConfig = new Configuration();
 
 public static Configuration getDefaultConfiguration()
 {
  return defaultConfig;
 }

 private UserDatabase userDatabase;
 private SessionPool sessionPool;
 private UploadManager uploadManager;

 private File tmpDir;
 private File baseDir;
 private File userBaseDir;
 
 private File publicModelDir;

 private Connection dBconnection;
 
 
 
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


 public void setBaseDir(File file)
 {
  baseDir = file;
 }

 public File getPublicModelDir()
 {
  if( publicModelDir == null )
   publicModelDir = new File(baseDir,pubResRelPath+File.separatorChar+modelRelPath);
  
  return publicModelDir;
 }
 
 public File getUserBaseDir()
 {
  if( userBaseDir == null )
   userBaseDir = new File(baseDir,userRelPath);
  
  return userBaseDir;
 }
 
 public String getModelRelPath()
 {
  return modelRelPath;
 }


 public File getDbDir()
 {
  return new File(baseDir,h2DbPath);
 }
 
 public void setDbConnection(Connection conn)
 {
  dBconnection = conn;
 }


 public Connection getDbConnection()
 {
  return dBconnection;
 }

 
 

}
