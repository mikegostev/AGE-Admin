package uk.ac.ebi.age.admin.client;

import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ageAdminGWT")
public interface AgeAdminService extends RemoteService
{
 public static class Util
 {
  private static AgeAdminServiceAsync instance;
  
  public static AgeAdminServiceAsync getInstance()
  {
   if( instance != null )
    return instance;
   
   
   instance = (AgeAdminServiceAsync) GWT.create(AgeAdminService.class);
   return instance;
  }
 }
 
 String login(String uname, String pass) throws UserAuthException;
 
 ModelImprint getModelImprint();

 ModelStorage getModelStorage() throws UserAuthException;

 void saveModel(ModelImprint model, String storePath) throws ModelStorageException, UserAuthException;
}
