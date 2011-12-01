package uk.ac.ebi.age.admin.client;

public class Session
{
 private static String sessionId;

 public static String getSessionId()
 {
  return sessionId;
 }

 public static void setSessionId(String sessId)
 {
  sessionId = sessId;
 }
 
 
}
