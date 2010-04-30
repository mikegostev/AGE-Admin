package uk.ac.ebi.age.admin.server.user;

public interface SessionPool
{

 public Session createSession(UserProfile prof, String[] strings);
 public Session getSession(String sessID);
 public void shutdown();

}
