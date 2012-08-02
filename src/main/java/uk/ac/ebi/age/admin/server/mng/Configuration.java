package uk.ac.ebi.age.admin.server.mng;

import java.io.File;

import org.apache.commons.transaction.file.FileResourceManager;

import uk.ac.ebi.age.admin.server.service.ds.DataSourceServiceRouter;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.annotation.AnnotationManager;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.PermissionManager;
import uk.ac.ebi.age.authz.SessionManager;
import uk.ac.ebi.age.mng.submission.SubmissionManager;
import uk.ac.ebi.age.parser.AgeTab2AgeConverter;
import uk.ac.ebi.age.parser.AgeTabSyntaxParser;
import uk.ac.ebi.age.parser.SyntaxProfile;
import uk.ac.ebi.age.service.submission.SubmissionDB;

public class Configuration
{
// public static final String SESSION_COOKIE_NAME = "AGEADMSESS";
 
 public static final String pubResRelPath="public";
 public static final String modelRelPath="model";
 public static final String userRelPath="user";
 public static final String submissionRelPath="submission";
 public static final String annotationRelPath="annotation";
 public static final String authRelPath="auth";

 public static final String webappErrorAttribute = "_sysError";
 
 private static Configuration defaultConfig = new Configuration();
 
 public static Configuration getDefaultConfiguration()
 {
  return defaultConfig;
 }

 private SessionManager sessionPool;
 private RemoteRequestManager remoteReqManager;
 private SubmissionDB submissionDB;
 private SubmissionManager submissionManager;
 private AnnotationManager annotationStorage;
 private FileResourceManager txResMngr;
 private PermissionManager permissionManager;
 private AgeTabSyntaxParser ageTabSyntaxParser;
 private AgeTab2AgeConverter ageTab2AgeConverter;
 private SyntaxProfile syntaxProfile;
 private boolean onlineMode;
 private String instanceName;
 
 private File tmpDir;
 private File baseDir;
 private File userBaseDir;
 
 private File publicModelDir;

 private FileSourceManager fileSourceManager;

 private DataSourceServiceRouter dsRouter;

 private AuthDB authDB;



 public SessionManager getSessionManager()
 {
  return sessionPool;
 }


 public String getSessionCookieName()
 {
  return Constants.sessionKey;
 }


 public RemoteRequestManager getRemoteRequestManager()
 {
  return remoteReqManager;
 }


 public void setSessionManager(SessionManager sessionPool)
 {
  this.sessionPool = sessionPool;
 }


 public void setRemoteRequestManager(RemoteRequestManager uploadManager)
 {
  this.remoteReqManager = uploadManager;
 }


 public File getTmpDir()
 {
  return tmpDir;
 }
 
 public void setTmpDir( File f)
 {
  tmpDir=f;
 }

 public File getBaseDir()
 {
  return baseDir;
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


 public AnnotationManager getAnnotationManager()
 {
  return annotationStorage;
 }


 public void setAnnotationManager(AnnotationManager annotationStorage)
 {
  this.annotationStorage = annotationStorage;
 }


 public DataSourceServiceRouter getDataSourceServiceRouter()
 {
  return dsRouter;
 }

 public void setDataSourceServiceRouter( DataSourceServiceRouter dsr )
 {
  dsRouter=dsr;
 }


 public void setAuthDB( AuthDB adb)
 {
  authDB = adb;
 }

 public AuthDB getAuthDB( )
 {
  return authDB;
 }


 public FileResourceManager getTxResourceManager()
 {
  return txResMngr;
 }


 public void setTxResourceManager(FileResourceManager txResMngr)
 {
  this.txResMngr = txResMngr;
 }


 public PermissionManager getPermissionManager()
 {
  return permissionManager;
 }


 public void setPermissionManager(PermissionManager permissionManager)
 {
  this.permissionManager = permissionManager;
 }


 public AgeTabSyntaxParser getAgeTabSyntaxParser()
 {
  return ageTabSyntaxParser;
 }


 public void setAgeTabSyntaxParser(AgeTabSyntaxParser ageTabSyntaxParser)
 {
  this.ageTabSyntaxParser = ageTabSyntaxParser;
 }


 public AgeTab2AgeConverter getAgeTab2AgeConverter()
 {
  return ageTab2AgeConverter;
 }


 public void setAgeTab2AgeConverter(AgeTab2AgeConverter ageTab2AgeConverter)
 {
  this.ageTab2AgeConverter = ageTab2AgeConverter;
 }


 public SyntaxProfile getSyntaxProfile()
 {
  return syntaxProfile;
 }


 public void setSyntaxProfile(SyntaxProfile syntaxProfile)
 {
  this.syntaxProfile = syntaxProfile;
 }


 public boolean isOnlineMode()
 {
  return onlineMode;
 }


 public String getInstanceName()
 {
  return instanceName;
 }


 public void setInstanceName(String instanceName)
 {
  this.instanceName = instanceName;
 }

 public void setOnlineMode(boolean onlineMode)
 {
  this.onlineMode = onlineMode;
 }

}


