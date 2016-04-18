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
import uk.ac.ebi.age.model.IdScope;
import uk.ac.ebi.age.parser.SyntaxProfile;
import uk.ac.ebi.age.parser.impl.ClassSpecificSyntaxProfileDefinitionImpl;
import uk.ac.ebi.age.parser.impl.SyntaxProfileDefinitionImpl;
import uk.ac.ebi.age.service.id.IdGenerator;
import uk.ac.ebi.age.service.id.impl.SeqIdGeneratorImpl;
import uk.ac.ebi.age.storage.AgeStorageAdm;
import uk.ac.ebi.age.storage.impl.ser.SerializedStorage;
import uk.ac.ebi.age.storage.impl.ser.SerializedStorageConfiguration;
import uk.ac.ebi.ageview.server.service.AgeViewInitException;
import uk.ac.ebi.ageview.server.service.AgeViewService;
import uk.ac.ebi.ageview.server.service.AgeViewServiceImpl;
import uk.ac.ebi.ageview.server.service.AgeViewTagController;
import uk.ac.ebi.ageview.shared.Constants;
import uk.ac.ebi.mg.assertlog.Log;
import uk.ac.ebi.mg.assertlog.LogFactory;
import uk.ac.ebi.mg.executor.DefaultExecutorService;

import com.pri.util.StringUtils;

public class Init implements ServletContextListener
{
 private static Log log = LogFactory.getLog(Init.class);

 AgeAdmin adm ;
 
 @Override
 public void contextDestroyed(ServletContextEvent arg0)
 {
  if( adm != null )
   adm.shutdown();
  
  DefaultExecutorService.shutdown();

 }

 @Override
 public void contextInitialized(ServletContextEvent arg0)
 {
  long startTime=0;
  
  assert ( startTime = System.currentTimeMillis() ) != 0;

  AgeAdminConfigManager cfg = new AgeAdminConfigManager(arg0.getServletContext());
  AgeStorageAdm storage;
  
  Configuration conf = Configuration.getDefaultConfiguration();
  
  conf.setBaseDir( new File(cfg.getBasePath()) );
  conf.setTmpDir( new File(cfg.getTmpPath()) );

  conf.setSubmissionConnURI( cfg.getSubmissionURI() );
  
  DefaultExecutorService.init();
  
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
  
  
  SyntaxProfile sp = new SyntaxProfile();
  SyntaxProfileDefinitionImpl commSP = new SyntaxProfileDefinitionImpl();
  commSP.setClusterIdPrefix("$C:");
  commSP.setModuleIdPrefix("$M:");
  commSP.setGlobalIdPrefix("$G:");
  commSP.setDefaultIdScope(IdScope.GLOBAL);
  commSP.setPrototypeObjectId("<<ALL>>");
  commSP.setResetPrototype(true);
 
  ClassSpecificSyntaxProfileDefinitionImpl vertClasses = new ClassSpecificSyntaxProfileDefinitionImpl(commSP);
  vertClasses.setHorizontalBlockDefault(false);

  ClassSpecificSyntaxProfileDefinitionImpl vertModClasses = new ClassSpecificSyntaxProfileDefinitionImpl(commSP);
  vertModClasses.setHorizontalBlockDefault(false);
  vertModClasses.setDefaultIdScope(IdScope.MODULE);
  
  sp.setCommonSyntaxProfile(commSP);
  
  sp.addClassSpecificSyntaxProfile("Submission", vertClasses);

  sp.addClassSpecificSyntaxProfile("Person", vertModClasses);
  sp.addClassSpecificSyntaxProfile("Publication", vertModClasses);
  sp.addClassSpecificSyntaxProfile("Organization", vertModClasses);

  
  conf.setSyntaxProfile(sp);
  
  
  
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

  AgeViewServiceImpl ageViewSvc=null;
  try
  {
   ageViewSvc = new AgeViewServiceImpl( storage );
  }
  catch(AgeViewInitException e)
  {
   // TODO Auto-generated catch block
   e.printStackTrace();
   return;
  }
  
  conf.getRemoteRequestManager().addRemoteRequestListener(Constants.AGEVIEW_TAG_CONTROL_COMMAND, new AgeViewTagController(adm));
  
  Configuration.getDefaultConfiguration().getAuthDB().addSecurityChangedListener(ageViewSvc);
  
  AgeViewService.setDefaultInstance( ageViewSvc );

  assert log.info("System startup time: "+StringUtils.millisToString(System.currentTimeMillis()-startTime) );

 
 }

}
