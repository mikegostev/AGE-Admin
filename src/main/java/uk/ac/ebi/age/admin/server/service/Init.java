package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.ebi.age.admin.server.mng.AgeAdmin;
import uk.ac.ebi.age.admin.server.mng.AgeAdminConfigManager;
import uk.ac.ebi.age.admin.server.mng.AgeAdminException;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.service.id.IdGenerator;
import uk.ac.ebi.age.service.id.impl.SeqIdGeneratorImpl;
import uk.ac.ebi.age.storage.AgeStorageAdm;
import uk.ac.ebi.age.storage.impl.ser.SerializedStorage;
import uk.ac.ebi.age.storage.impl.ser.SerializedStorageConfiguration;

public class Init implements ServletContextListener
{
 AgeAdmin adm ;
 
 @Override
 public void contextDestroyed(ServletContextEvent arg0)
 {
  if( adm != null )
   adm.shutdown();
 }

 @Override
 public void contextInitialized(ServletContextEvent arg0)
 {
  AgeAdminConfigManager cfg = new AgeAdminConfigManager(arg0.getServletContext());
  AgeStorageAdm storage;
  
  Configuration conf = Configuration.getDefaultConfiguration();
  
  conf.setBaseDir( new File(cfg.getBasePath()) );
  conf.setTmpDir( new File(cfg.getTmpPath()) );

  try
  {
   IdGenerator.setInstance( new SeqIdGeneratorImpl(cfg.getIDGenPath()) );
   
   SerializedStorageConfiguration serConf = new SerializedStorageConfiguration();
   
   serConf.setStorageBaseDir( new File( cfg.getAgeDBPath() ) );
   serConf.setMaintenanceModeTimeout(cfg.getMaintenanceModeTimeout());
   serConf.setMaster(cfg.isMaster());
   
   storage = new SerializedStorage(serConf);
  }
  catch(Exception e)
  {
   arg0.getServletContext().setAttribute(Configuration.webappErrorAttribute, "Can't instantiate AgeStorageManager");
   e.printStackTrace();
   return;
  }
  

  conf.getPublicModelDir().mkdirs();
  conf.getUserBaseDir().mkdirs();
  
  String prm = cfg.getConfigParameter(AgeAdminConfigManager.STARTUP_ONLINE_MODE_PARAM);
  
  conf.setOnlineMode( prm == null || ( ! "off".equalsIgnoreCase(prm) && ! "false".equalsIgnoreCase(prm) ));
  
  prm = cfg.getConfigParameter(AgeAdminConfigManager.INSTANCE_NAME_PARAM);
  
  if( prm == null )
  {
   try
   {
    prm = InetAddress.getLocalHost().getHostName();
   }
   catch(UnknownHostException e)
   {
    prm = "INST"+System.currentTimeMillis();
   }
  }
  
  conf.setInstanceName(prm);
  
  try
  {
   adm = new AgeAdmin(conf, storage);
  }
  catch(AgeAdminException e)
  {
   arg0.getServletContext().setAttribute(Configuration.webappErrorAttribute, "Can't instantiate AgeAdmin");
  }
 }

}
