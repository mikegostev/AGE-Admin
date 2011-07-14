package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerSystemException;
import org.apache.commons.transaction.util.CommonsLoggingLogger;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;
import uk.ac.ebi.age.admin.server.model.Age2ImprintConverter;
import uk.ac.ebi.age.admin.server.service.auth.ClassifierDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.GroupDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.GroupOfUserDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.GroupPartsDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.ProfileDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.ProfilePermissionsDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.SystemACLDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.TagACLDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.TagDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.auth.UserDBDataSourceService;
import uk.ac.ebi.age.admin.server.service.ds.DataSourceServiceRouter;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.StoreNode;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.admin.shared.user.exception.UserAuthException;
import uk.ac.ebi.age.annotation.AnnotationManager;
import uk.ac.ebi.age.annotation.Topic;
import uk.ac.ebi.age.annotation.impl.InMemoryAnnotationStorage;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.SecurityChangedListener;
import uk.ac.ebi.age.authz.Session;
import uk.ac.ebi.age.authz.SessionManager;
import uk.ac.ebi.age.authz.exception.AuthDBException;
import uk.ac.ebi.age.authz.exception.DBInitException;
import uk.ac.ebi.age.authz.impl.PermissionManagerImpl;
import uk.ac.ebi.age.authz.impl.SerializedAuthDBImpl;
import uk.ac.ebi.age.authz.impl.SessionManagerImpl;
import uk.ac.ebi.age.entity.CommonID;
import uk.ac.ebi.age.entity.EntityDomain;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.log.SimpleLogNode;
import uk.ac.ebi.age.ext.submission.HistoryEntry;
import uk.ac.ebi.age.ext.submission.SubmissionDBException;
import uk.ac.ebi.age.ext.submission.SubmissionQuery;
import uk.ac.ebi.age.ext.submission.SubmissionReport;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.submission.SubmissionManager;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.service.submission.SubmissionDB;
import uk.ac.ebi.age.service.submission.impl.H2SubmissionDB;
import uk.ac.ebi.age.storage.AgeStorageAdm;
import uk.ac.ebi.age.transaction.ReadLock;

import com.pri.util.M2codec;


public class AgeAdmin implements SecurityChangedListener
{
 private static AgeAdmin instance;

 public static AgeAdmin getDefaultInstance()
 {
  return instance;
 }

 private SessionManager   spool;
 private AgeStorageAdm storage;
 private SubmissionDB submissionDB;
 private AnnotationManager annotationMngr;
 
 private Configuration configuration;

 public AgeAdmin(Configuration conf, AgeStorageAdm storage) throws AgeAdminException
 {
  configuration=conf;
  
  this.storage = storage;

  if(conf.getTmpDir() == null)
   conf.setTmpDir(new File("var/tmp"));

  conf.getTmpDir().mkdirs();
  
  for( File tf : conf.getTmpDir().listFiles() )
  {
   if( tf.isDirectory() )
   {
    try
    {
     FileUtils.deleteDirectory(tf);
    }
    catch(IOException e1)
    {
     throw new AgeAdminException("Can't clean temporary dir: "+conf.getTmpDir().getAbsolutePath(),e1);
    }
   }
   else
    tf.delete();
  }
  
  if(conf.getSessionManager() == null)
   conf.setSessionManager(spool = new SessionManagerImpl( conf.getTmpDir() ));
  else
   spool = conf.getSessionManager();


  if(conf.getUploadManager() == null)
   conf.setUploadManager(new UploadManager());
  
  if(conf.getSubmissionDB() == null)
  {
   if( conf.getSubmissionManager() != null )
    conf.setSubmissionDB( conf.getSubmissionManager().getSubmissionDB() );
   else
    conf.setSubmissionDB( submissionDB = new H2SubmissionDB(conf.getSubmissionDbDir()) );
  }
  else
   submissionDB=conf.getSubmissionDB();
  
  if( conf.getTxResourceManager() == null )
  {
   conf.setTxResourceManager( new FileResourceManager(conf.getBaseDir().getAbsolutePath(),
     new File(conf.getTmpDir(),"tx").getAbsolutePath() , false, new CommonsLoggingLogger(LogFactory.getLog(FileResourceManager.class))) );
   
   try
   {
    conf.getTxResourceManager().start();
   }
   catch(ResourceManagerSystemException e)
   {
    e.printStackTrace();
    throw new AgeAdminException(e);
   }
  }

  if(conf.getAnnotationManager() == null)
  {
   try
   {
    conf.setAnnotationManager( annotationMngr = new InMemoryAnnotationStorage(conf.getTxResourceManager(),Configuration.annotationRelPath) );
   }
   catch(uk.ac.ebi.age.annotation.DBInitException e)
   {
    e.printStackTrace();
    throw new AgeAdminException(e);
   }
  }
  else
   annotationMngr=conf.getAnnotationManager();

  if( conf.getAuthDB() == null )
  {
   try
   {
    conf.setAuthDB( new SerializedAuthDBImpl(conf.getTxResourceManager(),Configuration.authRelPath) );
   }
   catch(DBInitException e)
   {
    e.printStackTrace();
    throw new AgeAdminException(e);
   }
  }
  
  conf.getAuthDB().addSecurityChangedListener(this);
  
  if( conf.getPermissionManager() == null )
   conf.setPermissionManager( new PermissionManagerImpl(spool, conf.getAuthDB(), annotationMngr) );
  
  if( conf.getSubmissionManager() == null )
   conf.setSubmissionManager( new SubmissionManager(storage, submissionDB, conf.getPermissionManager() ) );

//  if( conf.getClassifierDB() == null )
//  {
//   if( conf.getAuthDB() instanceof ClassifierDB )
//    conf.setClassifierDB( (ClassifierDB) conf.getAuthDB() );
//   else
//    conf.setClassifierDB( new SerializedAuthDBImpl(conf.getTxResourceManager(),Configuration.authRelPath) );
//  }
  
  
  if( conf.getDataSourceServiceRouter() == null )
   conf.setDataSourceServiceRouter( new DataSourceServiceRouter() );

  conf.getDataSourceServiceRouter().addService(Constants.userListServiceName, new UserDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.groupListServiceName, new GroupDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.groupOfUserListServiceName, new GroupOfUserDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.groupPartsListServiceName, new GroupPartsDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.profileListServiceName, new ProfileDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.profilePermissionsListServiceName, new ProfilePermissionsDBDataSourceService( conf.getAuthDB() ) );
 
  conf.getDataSourceServiceRouter().addService(Constants.classifierListServiceName, new ClassifierDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.tagTreeServiceName, new TagDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.tagACLServiceName, new TagACLDBDataSourceService( conf.getAuthDB() ) );
  conf.getDataSourceServiceRouter().addService(Constants.sysACLServiceName, new SystemACLDBDataSourceService( conf.getAuthDB() ) );

  if( conf.getFileSourceManager() == null )
   conf.setFileSourceManager( new FileSourceManager() );

  
//  conf.getUploadManager().addUploadCommandListener("SetModel", new SemanticUploader(storage));
  conf.getUploadManager().addUploadCommandListener(SubmissionConstants.SUBMISSON_COMMAND, new SubmissionUploader(conf.getSubmissionManager()));

  conf.getFileSourceManager().addFileSource(Constants.attachmentRequestSubject, new AttachmentFileSource(conf.getSubmissionDB()) );
  conf.getFileSourceManager().addFileSource(Constants.documentRequestSubject, new DocumentFileSource(conf.getSubmissionDB()) );
  
  if(instance == null)
   instance = this;
 }



 public void shutdown()
 {
  if(spool != null)
   spool.shutdown();

  try
  {
   Configuration.getDefaultConfiguration().getTxResourceManager().stop(FileResourceManager.SHUTDOWN_MODE_NORMAL);
  }
  catch(ResourceManagerSystemException e)
  {
   e.printStackTrace();
  }

//  if(submissionDB != null)
//   submissionDB.shutdown();

 }

 public Session login(String userName, String password, String clientAddr) throws UserAuthException
 {
  if( userName == null )
   throw new UserAuthException("Invalid user name");
  
  AuthDB authDB = configuration.getAuthDB();
  
  ReadLock lck = authDB.getReadLock();
  try
  {
//   User u = configuration.getAuthDB().getUser(lck, userName);
//   
//   if( u == null )
//    throw new UserAuthException("Invalid user name");
   
   try
   {
    if( ! authDB.checkUserPassword(lck, userName, password) )
     throw new UserAuthException("Invalid user password");
   }
   catch(AuthDBException e)
   {
    throw new UserAuthException("Invalid user name");
   }
  }
  finally
  {
   lck.release();
  }
  
  return spool.createSession(userName);
  

 }

 public ModelImprint getModelImprint( )
 {
  SemanticModel sm = storage.getSemanticModel();
  
  return Age2ImprintConverter.convertModelToImprint(sm);
 }

 public Session getSession(String value)
 {
  return spool.getSession(value);
 }

 public ModelStorage getModelStorage()
 {
  ModelStorage stor = new ModelStorage();
  
  stor.setPublicDirectory( imprintDirectory( configuration.getPublicModelDir() ) );
  
  Session sess = Configuration.getDefaultConfiguration().getSessionManager().getSession();
  
  if( sess != null )
    stor.setUserDirectory( imprintDirectory( new File( configuration.getUserBaseDir(),
      M2codec.encode(sess.getUser())+File.separatorChar+configuration.getModelRelPath()) ) );
  
  return stor;
 }

 private File pathToFile( ModelPath storePath, String uid ) throws ModelStorageException
 {
  StringBuilder sb = new StringBuilder(400);
  
  File baseDir;
  
  if( storePath.isPublic() )
   baseDir = configuration.getPublicModelDir();
  else if( uid != null )
   baseDir = new File(configuration.getUserBaseDir(),
     M2codec.encode(uid)+File.separatorChar+configuration.getModelRelPath());
  else
   throw new ModelStorageException("User not logged in");
  
  if( storePath.getPathElements() != null )
  {
   for( String pel : storePath.getPathElements() )
    sb.append( M2codec.encode(pel) ).append(File.separatorChar);
  }

  File stDir = new File( baseDir, sb.toString() );
  
  if( !stDir.exists() || !stDir.isDirectory() )
   throw new ModelStorageException("Selected path doesn't exist");
  
  return  new File( stDir, M2codec.encode(storePath.getModelName()) );
 }
 
 public void saveModel(ModelImprint model, ModelPath storePath) throws ModelStorageException
 {
  Session sess = Configuration.getDefaultConfiguration().getSessionManager().getSession();
  
  File modelFile = pathToFile(storePath, sess!=null?sess.getUser():null);
  
  ObjectOutputStream oos=null;
  try
  {
   FileOutputStream fos = null;
   fos = new FileOutputStream(modelFile);
   oos = new ObjectOutputStream( fos );
   
   oos.writeObject(model);
   
   oos.close();
   oos=null;
  }
  catch(IOException e)
  {
   if( oos != null )
   {
    try
    {
     oos.close();
    }
    catch(IOException ioe)
    {
     ioe.printStackTrace();
    }
   }
   
   throw new ModelStorageException("IO Error: "+e.getMessage());
  }
 }

 public static StoreNode imprintDirectory( File d )
 {
  if( ! d.exists() )
   d.mkdirs();
  else if( ! d.isDirectory() )
  {
   System.out.println("Model directory file is not directory: "+d.getAbsolutePath());
   return null;
  }
  
  StoreNode dir = new StoreNode( M2codec.decode(d.getName()) );
  dir.setDirectory(true);
  
  for( File f : d.listFiles() )
   if( f.isDirectory() )
    dir.addSubNode( imprintDirectory(f) );
   else
    dir.addFile( M2codec.decode(f.getName()) );
  
  return dir;
 }

 public ModelImprint getModel(ModelPath path) throws ModelStorageException
 {
  Session sess = Configuration.getDefaultConfiguration().getSessionManager().getSession();

  
  File modelFile = pathToFile(path, sess!=null?sess.getUser():null );

  if( ! modelFile.exists() )
   throw new ModelStorageException("Model doesn't exist");
  
  FileInputStream fis=null;
  
  try
  {
   fis = new FileInputStream(modelFile);
   ObjectInputStream ois = new ObjectInputStream( fis );
   
   ModelImprint mod = (ModelImprint)ois.readObject();

   return mod;
  }
  catch(Exception e)
  {
   throw new ModelStorageException(e.getMessage());
  }
  finally
  {
   if( fis != null )
   {
    try
    {
     fis.close();
    }
    catch(IOException e)
    {
     System.out.println("Can't close model file: "+modelFile.getAbsolutePath());
    }
   }
  }
 }

 public void installModel(ModelPath modelPath) throws ModelStorageException
 {
  ModelImprint modImp = getModel(modelPath);

  SemanticModel sm = Age2ImprintConverter.convertImprintToModel(modImp);
  
  BufferLogger bLog = new BufferLogger();
  
  if( ! storage.updateSemanticModel(sm, bLog.getRootNode()) )
   throw new ModelStorageException("Model installation failed");
 }



 public AgeStorageAdm getStorageAdmin()
 {
  return storage;
 }



 public SubmissionReport getSubmissions(SubmissionQuery q) throws SubmissionDBException
 {
  // TODO check permission to list all submissions
  
  return submissionDB.getSubmissions(q);
 }



 public List<HistoryEntry> getSubmissionHistory(String sbmId) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  return submissionDB.getHistory(sbmId);
 }



 public SimpleLogNode deleteSubmission(String id) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  BufferLogger log = new BufferLogger();
  
  configuration.getSubmissionManager().removeSubmission(id, log.getRootNode());
  
  return log.getRootNode();
 }



 public SimpleLogNode restoreSubmission(String id) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  BufferLogger log = new BufferLogger();

  configuration.getSubmissionManager().restoreSubmission(id, log.getRootNode() );
  
  return log.getRootNode();
 }



 @SuppressWarnings("unchecked")
 public Collection<TagRef> getSubmissionTags(String param)
 {
  // TODO check permission
  
  CommonID id = new CommonID();
  
  id.setDomain(EntityDomain.CLUSTER);
  id.setId(param);
  
  return (Collection<TagRef>) annotationMngr.getAnnotation(Topic.TAG, id);
 }



 public void storeSubmissionTags(String param, Collection<TagRef> result)
 {
  CommonID id = new CommonID();
  
  id.setDomain(EntityDomain.CLUSTER);
  id.setId(param);
  
  if( ! ( result instanceof ArrayList ) )
   result = new ArrayList<TagRef>( result );
  
  annotationMngr.addAnnotation(Topic.TAG, id, (Serializable)result);
 }



 @Override
 public void securityChanged()
 {
  storage.rebuildIndices();
 }


 
}
