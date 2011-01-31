package uk.ac.ebi.age.admin.server.mng;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;


public class AgeAdminConfigManager
{
 public static final String ISMASTER_WEBAPP_PARAM="isMaster";
 
 public static final String BASE_PATH_PARAM="basePath";
 public static final String DB_PATH_PARAM="dbPath";
 public static final String TMP_PATH_PARAM="tmpPath";
 public static final String SERVICES_PATH_PARAM="servicesPath";
 
 @SuppressWarnings("serial")
 private Map<String,String> configMap = new HashMap<String,String>(){{
  put(BASE_PATH_PARAM,      "var/biosd/");
  put(DB_PATH_PARAM,        "agedb");
  put(TMP_PATH_PARAM,       "tmp");
  put(SERVICES_PATH_PARAM,  "services");
 }};
 
// private static AgeAdminConfigManager instance = null;
 
 public AgeAdminConfigManager(ServletContext servletContext)
 {
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

 public String getBasePath()
 {
  return getConfigParameter(BASE_PATH_PARAM);
 }

 
 public String getDBPath()
 {
  return getPathParam(DB_PATH_PARAM);
 }

 public String getTmpPath()
 {
  return getPathParam(TMP_PATH_PARAM);
 }

 public String getServicesPath()
 {
  return getPathParam(SERVICES_PATH_PARAM);
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
  String mval = configMap.get(ISMASTER_WEBAPP_PARAM);
 
  return mval != null && ( "true".equalsIgnoreCase(mval) || "yes".equalsIgnoreCase(mval) || "1".equalsIgnoreCase(mval) ); 
 }
}
