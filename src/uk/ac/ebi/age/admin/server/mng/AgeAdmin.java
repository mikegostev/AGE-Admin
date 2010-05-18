package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import uk.ac.ebi.age.admin.client.common.user.exception.UserAuthException;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeObjectRestrictionImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.AgeObjectRestrictionImprint.Type;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;
import uk.ac.ebi.age.admin.server.user.UserProfile;
import uk.ac.ebi.age.admin.server.user.impl.SessionPoolImpl;
import uk.ac.ebi.age.admin.server.user.impl.TestUserDataBase;
import uk.ac.ebi.age.model.AgeAbstractClass;
import uk.ac.ebi.age.model.AgeAttributeClass;
import uk.ac.ebi.age.model.AgeClass;
import uk.ac.ebi.age.model.AgeRelationClass;
import uk.ac.ebi.age.model.AgeRestriction;
import uk.ac.ebi.age.model.AllValuesFromRestriction;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.model.SomeValuesFromRestriction;
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
  
  Map<AgeAbstractClass,Object> clMap = new HashMap<AgeAbstractClass, Object>();
  
  AgeClass ageRoot = sm.getRootAgeClass();
  
  AgeClassImprint rImp = new AgeClassImprint();
  
  for( AgeClass acls : ageRoot.getSubClasses() )
  {
   AgeClassImprint simp = convertAgeToImprint(acls,clMap, new Creator<AgeClassImprint>()
   {

    @Override
    public void addSubclass(AgeClassImprint bclass, AgeClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeClassImprint create(String id, String name)
    {
     AgeClassImprint cImp = new AgeClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   rImp.addSubClass(simp);
   simp.addSuperClass(rImp);
  }
  
  mimp.setRootClass(rImp);
  
  
  
  AgeAttributeClass attrRoot = sm.getRootAgeAttributeClass();
  
  AgeAttributeClassImprint atImp = new AgeAttributeClassImprint();
  
  for( AgeAttributeClass acls : attrRoot.getSubClasses() )
  {
   AgeAttributeClassImprint simp = convertAgeToImprint(acls,clMap, new Creator<AgeAttributeClassImprint>()
   {

    @Override
    public void addSubclass(AgeAttributeClassImprint bclass, AgeAttributeClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeAttributeClassImprint create(String id, String name)
    {
     AgeAttributeClassImprint cImp = new AgeAttributeClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   atImp.addSubClass(simp);
   simp.addSuperClass(atImp);
  }
  
  mimp.setRootAttributeClass(atImp);
  
  
  
  AgeRelationClass relRoot = sm.getRootAgeRelationClass();
  
  AgeRelationClassImprint relImp = new AgeRelationClassImprint();
  
  for( AgeRelationClass rcls : relRoot.getSubClasses() )
  {
   AgeRelationClassImprint simp = convertAgeToImprint(rcls,clMap, new Creator<AgeRelationClassImprint>()
   {

    @Override
    public void addSubclass(AgeRelationClassImprint bclass, AgeRelationClassImprint derClass)
    {
     bclass.addSubClass(derClass);
     derClass.addSuperClass(bclass);
    }

    @Override
    public AgeRelationClassImprint create(String id, String name)
    {
     AgeRelationClassImprint cImp = new AgeRelationClassImprint();
     
     cImp.setName(name);
     cImp.setId(id);
     
     return cImp;
    }
   });
   
   relImp.addSubClass(simp);
   simp.addSuperClass(relImp);
  }
  
  mimp.setRootRelationClass(relImp);

  convertObjectRestrictions(sm.getRootAgeClass(), clMap );
  
  return mimp;
 }

 
 private void convertObjectRestrictions(AgeClass rootAgeClass, Map<AgeAbstractClass, Object> clMap)
 {
  if(rootAgeClass.getSubClasses() == null)
   return;

  for(AgeClass cls : rootAgeClass.getSubClasses())
  {
   if(cls.getObjectRestrictions() == null)
    continue;

   AgeClassImprint clImp = (AgeClassImprint) clMap.get(cls);

   for(AgeRestriction rstr : cls.getObjectRestrictions())
    clImp.addRestriction(convertRestriction(rstr, clMap));
  }

 }

 private AgeObjectRestrictionImprint convertRestriction( AgeRestriction rstr, Map<AgeAbstractClass, Object> clMap )
 {
  AgeObjectRestrictionImprint rstimp = new AgeObjectRestrictionImprint();
  
  if( rstr instanceof SomeValuesFromRestriction )
  {
   rstimp.setType(Type.SOME);
   rstimp.setRelation((AgeRelationClassImprint)clMap.get(((SomeValuesFromRestriction) rstr).getAgeRelationClass()));
   rstimp.setFiller( convertRestriction(((SomeValuesFromRestriction) rstr).getFiller(), clMap));
  }
  else if ( rstr instanceof AllValuesFromRestriction )
  {
   rstimp.setType(Type.ONLY);
   rstimp.setRelation((AgeRelationClassImprint)clMap.get(((SomeValuesFromRestriction) rstr).getAgeRelationClass()));
   rstimp.setFiller( convertRestriction(((SomeValuesFromRestriction) rstr).getFiller(), clMap));
  }

  return rstimp;
 }

 private interface Creator<ImpC>
 {
  ImpC create( String id, String name );
  void addSubclass(ImpC bclass, ImpC derClass);
 }
 
 private <ImpC> ImpC convertAgeToImprint(AgeAbstractClass acls, Map<AgeAbstractClass, Object> clMap, Creator<ImpC> cr)
 {
  ImpC cImp = cr.create(acls.getId(),acls.getName());
  

  if( acls.getSubClasses() != null )
  {
   for( AgeAbstractClass scls : acls.getSubClasses() )
   {
    ImpC subImp = (ImpC)clMap.get(scls);
    
    if( subImp == null )
    {
     subImp = convertAgeToImprint(scls, clMap, cr);
     clMap.put(scls, subImp);
    }
    
    cr.addSubclass(cImp,subImp);
   }
  }
  
  return cImp;
 }
 
}
