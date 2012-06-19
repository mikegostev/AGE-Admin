package uk.ac.ebi.age.admin.server.mng;

import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.ServiceRequest;

public interface RemoteRequestListener
{

 boolean processRequest(ServiceRequest upReq, PrintWriter printWriter);

}
