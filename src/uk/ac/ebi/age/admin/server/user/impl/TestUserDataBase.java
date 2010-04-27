package uk.ac.ebi.age.admin.server.user.impl;

import java.util.Map;
import java.util.TreeMap;

import uk.ac.ebi.age.admin.server.user.UserDatabase;
import uk.ac.ebi.age.admin.server.user.UserProfile;

public class TestUserDataBase implements UserDatabase
{
 private Map<String, UserProfile> profileMap = new TreeMap<String, UserProfile>();
 
 public TestUserDataBase()
 {
  UserProfile testProf = new UserProfile();
  
  testProf.setUserName("test");
  testProf.setPassword("test");
  testProf.setUploadAllowed(true);
  
  profileMap.put("test", testProf );
 }

 @Override
 public UserProfile getAnonymousProfile()
 {
  UserProfile testProf = new UserProfile();
  
  testProf.setUserName("anonymout");
  testProf.setUploadAllowed(false);
  
  return testProf;
 }

 @Override
 public UserProfile getUserProfile(String userName)
 {
  return profileMap.get(userName);
 }

}
