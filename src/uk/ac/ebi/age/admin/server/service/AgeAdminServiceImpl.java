package uk.ac.ebi.age.admin.server.service;

import java.util.Collection;
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
import uk.ac.ebi.age.admin.shared.user.exception.UserAuthException;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.HistoryEntry;
import uk.ac.ebi.age.ext.submission.SubmissionDBException;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;

public class AgeAdminServiceImpl extends SessionRemoteServiceServlet implements AgeAdminService
{
 private static final long serialVersionUID = 1L;

 private AgeAdmin adm;
 
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
 public ModelStorage getModelStorage() throws UserAuthException
 {
  return adm.getModelStorage();
 }

 @Override
 public void saveModel(ModelImprint model, ModelPath storePath) throws ModelStorageException, UserAuthException
 {
  adm.saveModel(model, storePath );
 }

 @Override
 public ModelImprint getModel(ModelPath path) throws ModelStorageException, UserAuthException
 {
  return  adm.getModel( path );
 }

 @Override
 public LogNode installModel(ModelPath modelPath) throws UserAuthException, ModelStorageException
 {
  return adm.installModel( modelPath );
 }

 @Override
 public SubmissionReport getSubmissions(SubmissionQuery q) throws UserAuthException, SubmissionDBException
 {
  return adm.getSubmissions(q);
 }
 
 @Override
 public List<HistoryEntry> getSubmissionHistory( String sbmId ) throws UserAuthException, SubmissionDBException
 {
  return adm.getSubmissionHistory( sbmId );
 }

 @Override
 public SimpleLogNode deleteSubmission(String id) throws UserAuthException, SubmissionDBException
 {
  return adm.deleteSubmission(id);
 }
 
 @Override
 public SimpleLogNode tranklucateSubmission(String id)
 {
  return adm.tranklucateSubmission(id);
 }
 
 @Override
 public SimpleLogNode restoreSubmission(String id) throws UserAuthException, SubmissionDBException
 {
  return adm.restoreSubmission(id);
 }



 @Override
 public Collection<TagRef> getSubmissionTags(String param) throws UserAuthException, SubmissionDBException
 {
  return adm.getSubmissionTags(param);
 }



 @Override
 public void storeSubmissionTags(String param, Collection<TagRef> result) throws SubmissionDBException
 {
  adm.storeSubmissionTags(param,result);
 }



}
