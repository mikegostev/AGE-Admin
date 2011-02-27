package uk.ac.ebi.age.admin.server.service;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import uk.ac.ebi.age.admin.server.mng.AgeAdmin;
import uk.ac.ebi.age.admin.server.mng.AgeAdminConfigManager;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.mng.AgeStorageManager;
import uk.ac.ebi.age.mng.AgeStorageManager.DB_TYPE;
import uk.ac.ebi.age.service.id.IdGenerator;
import uk.ac.ebi.age.service.id.impl.SeqIdGeneratorImpl;
import uk.ac.ebi.age.storage.AgeStorageAdm;

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
   boolean master = cfg.isMaster();
   
   IdGenerator.setInstance( new SeqIdGeneratorImpl(cfg.getIDGenPath()) );
   storage = AgeStorageManager.createInstance( DB_TYPE.AgeDB, cfg.getAgeDBPath(), master );
  }
  catch(Exception e)
  {
   e.printStackTrace();
   return;
  }
  

  conf.getPublicModelDir().mkdirs();
  conf.getUserBaseDir().mkdirs();
  
  adm = new AgeAdmin(conf, storage);
 }

}
