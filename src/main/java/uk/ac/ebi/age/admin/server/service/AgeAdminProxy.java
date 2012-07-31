package uk.ac.ebi.age.admin.server.service;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import uk.ac.ebi.age.admin.client.AgeAdminService;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;
import uk.ac.ebi.age.admin.server.mng.AgeAdmin;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.ext.annotation.AnnotationDBException;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.entity.Entity;
import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.HistoryEntry;
import uk.ac.ebi.age.ext.submission.SubmissionDBException;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;
import uk.ac.ebi.age.ext.user.exception.NotAuthorizedException;
import uk.ac.ebi.age.ext.user.exception.UserAuthException;

public class AgeAdminProxy extends SessionRemoteServiceServlet implements AgeAdminService
{
 private static final long serialVersionUID = 1L;

 private AgeAdmin adm;
 
 @Override
 public void init() throws ServletException
 {
  super.init();
  
  adm=AgeAdmin.getDefaultInstance();
 }
 


 @Override
 public String login(String uname, String pass) throws UserAuthException
 {
  HttpServletRequest req = getThreadLocalRequest();
  
  String sess = adm.login(uname, pass, req.getRemoteAddr()).getSessionKey();

  getThreadLocalResponse().addCookie( new Cookie(Configuration.getDefaultConfiguration().getSessionCookieName(), sess) );
  
  return sess;
 }


 @Override
 public ModelImprint getModelImprint()
 {
  return adm.getModelImprint();
 }

 @Override
 public ModelStorage getModelStorage() throws NotAuthorizedException
 {
  return adm.getModelStorage();
 }

 @Override
 public void saveModel(ModelImprint model, ModelPath storePath) throws ModelStorageException, NotAuthorizedException
 {
  adm.saveModel(model, storePath );
 }

 @Override
 public ModelImprint getModel(ModelPath path) throws ModelStorageException, NotAuthorizedException
 {
  return  adm.getModel( path );
 }

 @Override
 public LogNode installModel(ModelPath modelPath) throws NotAuthorizedException, ModelStorageException
 {
  return adm.installModel( modelPath );
 }

 @Override
 public SubmissionReport getSubmissions(SubmissionQuery q) throws NotAuthorizedException, SubmissionDBException
 {
  return adm.getSubmissions(q);
 }
 
 @Override
 public List<HistoryEntry> getSubmissionHistory( String sbmId ) throws NotAuthorizedException, SubmissionDBException
 {
  return adm.getSubmissionHistory( sbmId );
 }

 @Override
 public SimpleLogNode deleteSubmission(String id) throws NotAuthorizedException, SubmissionDBException
 {
  return adm.deleteSubmission(id);
 }
 
 @Override
 public SimpleLogNode tranklucateSubmission(String id) throws NotAuthorizedException
 {
  return adm.tranklucateSubmission(id);
 }
 
 @Override
 public SimpleLogNode restoreSubmission(String id) throws NotAuthorizedException, SubmissionDBException
 {
  return adm.restoreSubmission(id);
 }



 @Override
 public List<TagRef> getEntityTags(Entity instance)  throws NotAuthorizedException, AnnotationDBException
 {
  return adm.getEntityTags(instance);
 }



 @Override
 public void storeEntityTags(Entity instance, List<TagRef> tr)  throws NotAuthorizedException, AnnotationDBException
 {
  adm.storeEntityTags(instance,tr);
 }



 @Override
 public boolean setMaintenanceMode(boolean set, int timeout) throws NotAuthorizedException
 {
  return adm.setMaintenanceMode(set, timeout);
 }



 @Override
 public boolean setOnlineMode(boolean set) throws NotAuthorizedException
 {
  return adm.setOnlineMode(set);
 }



 @Override
 public boolean isOnlineMode()
 {
  return adm.isOnlineMode();
 }

}
