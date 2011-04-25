package uk.ac.ebi.age.admin.server.mng;

import java.io.File;

import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.mng.submission.SubmissionManager;
import uk.ac.ebi.age.service.submission.SubmissionDB;

public class Configuration
{
 public static final String SESSION_COOKIE_NAME = "AGEADMSESS";
 
 public static final String pubResRelPath="public";
 public static final String modelRelPath="model";
 public static final String userRelPath="user";
 public static final String submissionRelPath="submission";
 
 private static Configuration defaultConfig = new Configuration();
 
 public static Configuration getDefaultConfiguration()
 {
  return defaultConfig;
 }

 private UserDatabase userDatabase;
 private SessionPool sessionPool;
 private UploadManager uploadManager;
 private SubmissionDB submissionDB;
 private SubmissionManager submissionManager;

 private File tmpDir;
 private File baseDir;
 private File userBaseDir;
 
 private File publicModelDir;

 private FileSourceManager fileSourceManager;


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

 public File getSubmissionDbDir()
 {
  return new File(baseDir,submissionRelPath);
 }


 public SubmissionDB getSubmissionDB()
 {
  return submissionDB;
 }


 public void setSubmissionDB(SubmissionDB submissionDB)
 {
  this.submissionDB = submissionDB;
 }


 public SubmissionManager getSubmissionManager()
 {
  return this.submissionManager;
 }


 public void setSubmissionManager(SubmissionManager submissionManager)
 {
  this.submissionManager = submissionManager;
 }


 public FileSourceManager getFileSourceManager()
 {
  return fileSourceManager;
 }


 public void setFileSourceManager(FileSourceManager fileSourceManager)
 {
  this.fileSourceManager = fileSourceManager;
 }
 


}


