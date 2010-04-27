package uk.ac.ebi.age.admin.server.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.mng.UploadManager;
import uk.ac.ebi.age.admin.server.user.impl.SessionPoolImpl;
import uk.ac.ebi.age.admin.server.user.impl.TestUserDataBase;

public class Init implements ServletContextListener
{

 @Override
 public void contextDestroyed(ServletContextEvent arg0)
 {
  Configuration.getDefaultConfiguration().getSessionPool().shutdown();
 }

 @Override
 public void contextInitialized(ServletContextEvent arg0)
 {
  Configuration conf = Configuration.getDefaultConfiguration();
  
  conf.setSessionPool(new SessionPoolImpl() );
  conf.setUserDatabase( new TestUserDataBase() );
  conf.setUploadManager( new UploadManager() );
 }

}
