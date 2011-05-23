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
 
 public boolean processRequest( String svcName, DataSourceRequest dsr  )
 {
  DataSourceBackendService svc = svcMap.get(svcName);
  
  if( svc == null )
   return false;
  
  return svc.processRequest( dsr );
 }
}
