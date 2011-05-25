package uk.ac.ebi.age.admin.server.service.ds;

import java.util.HashMap;
import java.util.Map;

public class DataSourceServiceRouter
{
 private Map<String, DataSourceBackendService> svcMap = new HashMap<String, DataSourceBackendService>();
 
 public void addService(String svcName, DataSourceBackendService svc)
 {
  svcMap.put(svcName, svc);
 }
 
 public DataSourceResponse processRequest( String svcName, DataSourceRequest dsr  )
 {
  DataSourceBackendService svc = svcMap.get(svcName);
  
  if( svc == null )
   return null;
  
  return svc.processRequest( dsr );
 }

 public DataSourceBackendService getService(String dest)
 {
  return svcMap.get(dest);
 }
}
