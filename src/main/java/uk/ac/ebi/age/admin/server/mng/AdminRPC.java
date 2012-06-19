package uk.ac.ebi.age.admin.server.mng;

import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.ServiceRequest;

public class AdminRPC implements RemoteRequestListener
{
 private AgeAdmin admin;

 public AdminRPC(AgeAdmin ageAdmin)
 {
  admin=ageAdmin;
 }

 @Override
 public boolean processRequest(ServiceRequest upReq, PrintWriter printWriter)
 {
  // TODO Auto-generated method stub
  throw new dev.NotImplementedYetException();
  //return false;
 }

}
