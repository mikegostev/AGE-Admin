package uk.ac.ebi.age.admin.server.mng;

import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.shared.MaintenanceModeConstants;

public class MaintenanceModeManager implements UploadCommandListener
{
 private AgeAdmin admin; 

 public MaintenanceModeManager(AgeAdmin ageAdmin)
 {
  admin = ageAdmin;
 }

 @Override
 public boolean processUpload(UploadRequest upReq, PrintWriter out)
 {
  String val = upReq.getParams().get(MaintenanceModeConstants.modeParam);
  
  boolean setMM = false;
  
  if( val != null && val.equalsIgnoreCase("true") )
   setMM=true;
  
  try
  {
   if( admin.setMaintenanceMode(setMM) )
    out.println("OK:SET");
   else
    out.println("OK:WAS");
   
   return true;
  }
  catch (Exception e)
  {
   out.println("ERROR:"+e.getMessage());
  }
  
  return false;
 }

}
