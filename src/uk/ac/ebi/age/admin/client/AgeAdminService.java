package uk.ac.ebi.age.admin.client;

import java.util.Collection;
import java.util.List;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.user.exception.UserAuthException;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.HistoryEntry;
import uk.ac.ebi.age.ext.submission.SubmissionDBException;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;

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

 LogNode installModel(ModelPath modelPath) throws UserAuthException, ModelStorageException;

 SubmissionReport getSubmissions(SubmissionQuery q) throws UserAuthException, SubmissionDBException;

 List<HistoryEntry> getSubmissionHistory(String sbmId) throws UserAuthException, SubmissionDBException;

 SimpleLogNode deleteSubmission(String id) throws UserAuthException, SubmissionDBException;

 SimpleLogNode restoreSubmission(String id) throws UserAuthException, SubmissionDBException;

 Collection<TagRef> getSubmissionTags(String param) throws UserAuthException, SubmissionDBException;

 void storeSubmissionTags(String param, Collection<TagRef> result) throws SubmissionDBException;

 SimpleLogNode tranklucateSubmission(String id) throws SubmissionDBException;
}
