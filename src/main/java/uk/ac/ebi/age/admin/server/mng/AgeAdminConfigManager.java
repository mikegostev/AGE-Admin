package uk.ac.ebi.age.admin.server.mng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;


public class AgeAdminConfigManager
{
 public static final String BASE_PATH_PARAM="basePath";
 public static final String INDEX_PATH_PARAM="indexPath";
 public static final String AGEDB_PATH_PARAM="dbPath";
 public static final String TMP_PATH_PARAM="tmpPath";
 public static final String SERVICES_PATH_PARAM="servicesPath";
 public static final String IDGEN_PATH_PARAM="IDGenPath";
 public static final String IS_MASTER_PARAM="isMaster";
 public static final String MAINTENANCE_MODE_TIMEOUT_PARAM="maintenanceModeTimeout";
 public static final String AUTO_MMODE_TIMEOUT_PARAM="autoMModeTimeout";
 
 private static final String defaultIndexPath = "index";
 
 private static final long defaultMaintenanceModeTimeout = 30000;
 private static final long defaultAutoMModeTimeout = 2000;
 
 @SuppressWarnings("serial")
 private Map<String,String> configMap = new HashMap<String,String>(){{
  put(BASE_PATH_PARAM,      "var/biosd/");
  put(AGEDB_PATH_PARAM,     "agedb");
  put(TMP_PATH_PARAM,       "tmp");
  put(SERVICES_PATH_PARAM,  "services");
  put(IDGEN_PATH_PARAM,     "services/SeqIdGen");
 }};
 
// private static AgeAdminConfigManager instance = null;
 
 public AgeAdminConfigManager(ServletContext servletContext)
 {
  InputStream propis = getClass().getResourceAsStream("/default.properties");
  
  if( propis != null )
  {
   Properties prop = new Properties();
   try
   {
    prop.load(propis);
    
    for( Map.Entry<Object, Object> me : prop.entrySet() )
     configMap.put(me.getKey().toString(), me.getValue().toString() );
   }
   catch(IOException e)
   {
    e.printStackTrace();
   }
  }
  
  Enumeration<?> pNames = servletContext.getInitParameterNames();
  
  while( pNames.hasMoreElements() )
  {
   String key = pNames.nextElement().toString();
   configMap.put(key, servletContext.getInitParameter(key) );
  }
 }
 
// public static void setInstance( AgeAdminConfigManager inst )
// {
//  instance=inst;
// }
//
// 
// public static AgeAdminConfigManager instance()
// {
//  return instance;
// }


 public String getIndexDir()
 {
  String path = getConfigParameter(INDEX_PATH_PARAM);
    
  if( path != null )
   return path;
  
  String basePath = getBasePath();
  
  return basePath.endsWith("/")?basePath+defaultIndexPath:basePath+"/"+defaultIndexPath;
 }
 
 public String getBasePath()
 {
  return getConfigParameter(BASE_PATH_PARAM);
 }

 
 public String getAgeDBPath()
 {
  return getPathParam(AGEDB_PATH_PARAM);
 }

 public String getTmpPath()
 {
  return getPathParam(TMP_PATH_PARAM);
 }

 public String getServicesPath()
 {
  return getPathParam(SERVICES_PATH_PARAM);
 }

 public String getIDGenPath()
 {
  return getPathParam(IDGEN_PATH_PARAM);
 }

 
 public String getPathParam( String key )
 {
  String basePath = getBasePath();
  String path = getConfigParameter(key);
  
  if( path == null )
   path=key;
  
  if( basePath.endsWith("/") || path.startsWith("/") )
   return basePath+path;

  return basePath+"/"+path;
 }

 
 public String getConfigParameter( String key )
 {
  return configMap.get(key);
 }

 public String setConfigParameter( String key, String value )
 {
  String old = configMap.get(key);
  configMap.put(key, value);
  return old;
 }

 public boolean isMaster()
 {
  String mval = configMap.get(IS_MASTER_PARAM);
 
  return mval != null && ( "true".equalsIgnoreCase(mval) || "yes".equalsIgnoreCase(mval) || "1".equalsIgnoreCase(mval) ); 
 }
 
 public long getMaintenanceModeTimeout()
 {
  String mmTO = configMap.get(MAINTENANCE_MODE_TIMEOUT_PARAM);
  
  if( mmTO == null )
   return defaultMaintenanceModeTimeout;
  
  try
  {
   return Long.parseLong( mmTO );
  }
  catch (Exception e)
  {
   return defaultMaintenanceModeTimeout;
  }
 }
 
 public long getAutoMModeTimeout()
 {
  String mmTO = configMap.get(AUTO_MMODE_TIMEOUT_PARAM);
  
  if( mmTO == null )
   return defaultAutoMModeTimeout;
  
  try
  {
   return Long.parseLong( mmTO );
  }
  catch (Exception e)
  {
   return defaultAutoMModeTimeout;
  }
 }
}
