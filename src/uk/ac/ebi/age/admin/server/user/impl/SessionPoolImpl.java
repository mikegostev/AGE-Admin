package uk.ac.ebi.age.admin.server.user.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserProfile;

public class SessionPoolImpl implements SessionPool, Runnable
{
 private static final int CHECK_INTERVAL = 30000;
 private static final int MAX_SESSION_IDLE_TIME = 300000;
 
 private Thread controlThread = new Thread( this );
 private boolean shutdown = false;

 private Map<String, Session> sessionMap = new TreeMap<String, Session>();
 private Lock lock = new ReentrantLock(); 
 
 public SessionPoolImpl()
 {
  controlThread.start();
 }
 
 @Override
 public Session createSession(UserProfile prof, String[] strings)
 {
  Session sess = new Session();
  
  String key = generateSessionKey(strings);
  
  sess.setSessionKey(key);
  sess.setUserProfile(prof);
  sess.setLastAccessTime( System.currentTimeMillis() );

  try
  {
   lock.lock();

   sessionMap.put(key,sess);
  }
  finally
  {
   lock.unlock();
  }
  
  return sess;
 }

 @Override
 public Session getSession(String sessID)
 {
  try
  {
   lock.lock();

   Session sess = sessionMap.get(sessID);
   
   if( sess == null )
    return null;
   
   sess.setLastAccessTime( System.currentTimeMillis() );
   
   return sessionMap.get(sessID);
  }
  finally
  {
   lock.unlock();
  }
 }

 @Override
 public void shutdown()
 {
  shutdown = true;
  controlThread.interrupt();
 }

 @Override
 public void run()
 {
  while( ! shutdown )
  {
   try
   {
    Thread.sleep(CHECK_INTERVAL);
   }
   catch(InterruptedException e)
   {
   }

   
   try
   {
    lock.lock();
    
    long time = System.currentTimeMillis();

    Iterator<Session> sitr = sessionMap.values().iterator();
    
    while( sitr.hasNext() )
    {
     Session sess = sitr.next();
     
     if( ( time - sess.getLastAccessTime() ) > MAX_SESSION_IDLE_TIME || shutdown )
     {
      sitr.remove();
      sess.destroy();
     }
      
    }
   }
   finally
   {
    lock.unlock();
   }
  }
 }

 final static String algorithm="MD5";
 
 private String generateSessionKey( String[] strs )
 {

  StringBuffer message = new StringBuffer(100);

  for(int i = 0; i < strs.length; i++)
   message.append(strs[i]);

  message.append(System.currentTimeMillis());

  try
  {
   MessageDigest md5d = MessageDigest.getInstance(algorithm);

   byte[] digest = md5d.digest(message.toString().getBytes());

   message.setLength(0);

   for(int i = 0; i < digest.length; i++)
   {
    String byteHex = Integer.toHexString(digest[i]);
    
    if( byteHex .length() < 2 )
     message.append('0').append(byteHex.charAt(0));
    else
     message.append(byteHex.substring(byteHex.length()-2));
   }
   
   return "K" + message;
  }
  catch(NoSuchAlgorithmException ex)
  {
   ex.printStackTrace();
   return String.valueOf(System.currentTimeMillis());
  }

 }

}
