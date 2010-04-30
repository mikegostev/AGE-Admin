package uk.ac.ebi.age.admin.server.user;

import java.io.File;

import uk.ac.ebi.age.admin.server.mng.Configuration;

public class Session
{
 private UserProfile userProfile;
 private long lastAccessTime;
 private String sessionKey;
 
 private File sessionDir;
 
 private int tmpFileCounter = 0;
 
 public UserProfile getUserProfile()
 {
  return userProfile;
 }

 public File makeTempFile()
 {
  if( sessionDir == null )
  {
   sessionDir = new File(Configuration.getDefaultConfiguration().getTmpDir(),sessionKey);
   sessionDir.mkdirs();
  }
   
   
  return new File( sessionDir, String.valueOf(++tmpFileCounter));
 }

 public long getLastAccessTime()
 {
  return lastAccessTime;
 }

 public void setLastAccessTime(long currentTimeMillis)
 {
  lastAccessTime = currentTimeMillis;
 }

 public void destroy()
 {
  if( sessionDir != null )
  {
   for( File f : sessionDir.listFiles() )
    f.delete();
   
   sessionDir.delete();
  }
 }

 public void setSessionKey(String key)
 {
  sessionKey = key;
 }

 public void setUserProfile(UserProfile prof)
 {
  userProfile = prof;
 }

 public String getSessionKey()
 {
  return sessionKey;
 }

}
