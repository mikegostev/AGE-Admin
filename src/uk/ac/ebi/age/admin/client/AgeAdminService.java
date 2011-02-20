package uk.ac.ebi.age.admin.client;

import java.util.List;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.submission.SubmissionImprint;
import uk.ac.ebi.age.admin.shared.submission.SubmissionQuery;
import uk.ac.ebi.age.admin.shared.user.exception.UserAuthException;

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

 void saveModel(ModelImprint model, ModelPath storePath) throws ModelStorageException, UserAuthException;

 ModelImprint getModel(ModelPath path) throws ModelStorageException, UserAuthException;

 void installModel(ModelPath modelPath) throws UserAuthException, ModelStorageException;

 List<SubmissionImprint> getSubmissions(SubmissionQuery q);
}
