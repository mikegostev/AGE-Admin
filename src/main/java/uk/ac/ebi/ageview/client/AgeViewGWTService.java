package uk.ac.ebi.ageview.client;

import uk.ac.ebi.age.ext.user.exception.NotAuthorizedException;
import uk.ac.ebi.age.ui.shared.imprint.ObjectId;
import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;
import uk.ac.ebi.ageview.client.query.Report;
import uk.ac.ebi.ageview.client.shared.MaintenanceModeException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("ageQueryGWT")
public interface AgeViewGWTService extends RemoteService
{
 public static class Util
 {
  private static AgeViewGWTServiceAsync instance;
  
  public static AgeViewGWTServiceAsync getInstance()
  {
   if( instance != null )
    return instance;
   
   
   instance = (AgeViewGWTServiceAsync) GWT.create(AgeViewGWTService.class);
   return instance;
  }
 }
 
 
 Report selectRootObjects(String value, boolean searchAttrNm, boolean searchAttrVl, int offs, int cnt)
   throws MaintenanceModeException;


 ObjectImprint getObjectImprint(ObjectId id) throws MaintenanceModeException, NotAuthorizedException;



}
