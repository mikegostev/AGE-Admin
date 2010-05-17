package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeObjectRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;
import uk.ac.ebi.age.admin.server.user.UserProfile;
import uk.ac.ebi.age.admin.server.user.impl.SessionPoolImpl;
import uk.ac.ebi.age.admin.server.user.impl.TestUserDataBase;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeRestriction;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.storage.AgeStorageAdm;

public class AgeAdmin
{
 private static AgeAdmin instance;

 public static AgeAdmin getDefaultInstance()
 {
  return instance;
 }

 private SessionPool   spool;
 private UserDatabase  udb;
 private AgeStorageAdm storage;

 public AgeAdmin(Configuration conf, AgeStorageAdm storage)
 {
  this.storage = storage;

  if(conf.getTmpDir() == null)
   conf.setTmpDir(new File("var/tmp"));

  if(conf.getSessionPool() == null)
   conf.setSessionPool(spool = new SessionPoolImpl());

  if(conf.getUserDatabase() == null)
   conf.setUserDatabase(udb = new TestUserDataBase());

  if(conf.getUploadManager() == null)
   conf.setUploadManager(new UploadManager());

  conf.getUploadManager().addUploadCommandListener("SetModel", new SemanticUploader(storage));
  conf.getUploadManager().addUploadCommandListener("Submission", new SubmissionUploader(storage));

  if(instance == null)
   instance = this;
 }

 public void shutdown()
 {
  if(spool != null)
   spool.shutdown();

  if(udb != null)
   udb.shutdown();
 }

 public Session login(String userName, String password, String clientAddr) throws UserAuthException
 {
  UserDatabase udb = Configuration.getDefaultConfiguration().getUserDatabase();

  UserProfile prof = null;

  if(userName == null || userName.length() == 0)
   prof = udb.getAnonymousProfile();
  else
  {
   prof = udb.getUserProfile(userName);

   if(prof == null)
    throw new UserAuthException("Invalid user name");

   if(!prof.checkPassword(password))
    throw new UserAuthException("Invalid user password");

  }

  Session sess = Configuration.getDefaultConfiguration().getSessionPool().createSession(prof, new String[] { userName, clientAddr });

  return sess;
 }

 public ModelImprint getModelImprint()
 {
  SemanticModel sm = storage.getSemanticModel();
  
  ModelImprint mimp = new ModelImprint();
  
  Map<AgeClass,AgeClassImprint> clMap = new HashMap<AgeClass, AgeClassImprint>();
  
  AgeClass ageRoot = sm.getRootAgeClass();
  
  AgeClassImprint rImp = new AgeClassImprint();
  
  for( AgeClass acls : ageRoot.getSubClasses() )
  {
   AgeClassImprint simp = convertClassToImprint(acls,clMap);
   
   rImp.addSubClass(simp);
   simp.addSuperClass(rImp);
  }
  
  return mimp;
 }

 private AgeClassImprint convertClassToImprint(AgeClass acls, Map<AgeClass, AgeClassImprint> clMap)
 {
  AgeClassImprint cImp = new AgeClassImprint();
  
  cImp.setName(acls.getName());
  cImp.setId(acls.getId());
  
  if( acls.getSubClasses() != null )
  {
   for( AgeClass scls : acls.getSubClasses() )
   {
    AgeClassImprint subImp = clMap.get(scls);
    
    if( subImp == null )
    {
     subImp = convertClassToImprint(scls, clMap);
     clMap.put(scls, subImp);
    }
    
    cImp.addSubClass(subImp);
    subImp.addSuperClass(cImp);
   }
  }
  
  if( acls.getObjectRestrictions()  != null )
  {
   for( AgeRestriction rstr : acls.getObjectRestrictions() )
    cImp.addRestriction( convertRestrictionToImprint( rstr ) );
  }
  
  return cImp;
 }

 private AgeObjectRestrictionImprint convertRestrictionToImprint(AgeRestriction rstr)
 {
  rstr.g
  throw new dev.NotImplementedYetException();
 }
}
