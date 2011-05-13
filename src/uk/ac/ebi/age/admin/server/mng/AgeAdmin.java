package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.ModelStorage;
import uk.ac.ebi.age.admin.client.model.ModelStorageException;
import uk.ac.ebi.age.admin.server.model.Age2ImprintConverter;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.server.user.SessionPool;
import uk.ac.ebi.age.admin.server.user.UserDatabase;
import uk.ac.ebi.age.admin.server.user.UserProfile;
import uk.ac.ebi.age.admin.server.user.impl.SessionPoolImpl;
import uk.ac.ebi.age.admin.server.user.impl.TestUserDataBase;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ModelPath;
import uk.ac.ebi.age.admin.shared.StoreNode;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.admin.shared.user.exception.UserAuthException;
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

import com.pri.util.M2codec;


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
 private SubmissionDB submissionDB;
 
 private Configuration configuration;

 public AgeAdmin(Configuration conf, AgeStorageAdm storage)
 {
  configuration=conf;
  
  this.storage = storage;

  if(conf.getTmpDir() == null)
   conf.setTmpDir(new File("var/tmp"));

  if(conf.getSessionPool() == null)
   conf.setSessionPool(spool = new SessionPoolImpl());
  else
   spool = conf.getSessionPool();

  if(conf.getUserDatabase() == null)
   conf.setUserDatabase(udb = new TestUserDataBase());
  else
   udb = conf.getUserDatabase();

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
  
  if( conf.getSubmissionManager() == null )
   conf.setSubmissionManager( new SubmissionManager(storage, submissionDB ) );

  if( conf.getFileSourceManager() == null )
   conf.setFileSourceManager( new FileSourceManager() );

  
  conf.getUploadManager().addUploadCommandListener("SetModel", new SemanticUploader(storage));
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

  if(udb != null)
   udb.shutdown();

//  if(submissionDB != null)
//   submissionDB.shutdown();

 }

 public Session login(String userName, String password, String clientAddr) throws UserAuthException
 {
//  UserDatabase udb = Configuration.getDefaultConfiguration().getUserDatabase();

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

  Session sess = spool.createSession(prof, new String[] { userName, clientAddr });

  return sess;
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

 public ModelStorage getModelStorage(Session userSession)
 {
  ModelStorage stor = new ModelStorage();
  
  stor.setPublicDirectory( imprintDirectory( configuration.getPublicModelDir() ) );
  stor.setUserDirectory( imprintDirectory( new File( configuration.getUserBaseDir(),
    String.valueOf(userSession.getUserProfile().getUserId())+File.separatorChar+configuration.getModelRelPath()) ) );
  
  return stor;
 }

 private File pathToFile( ModelPath storePath, int uid ) throws ModelStorageException
 {
  StringBuilder sb = new StringBuilder(400);
  
  File baseDir;
  
  if( storePath.isPublic() )
   baseDir = configuration.getPublicModelDir();
  else
   baseDir = new File(configuration.getUserBaseDir(),
     String.valueOf(uid)+File.separatorChar+configuration.getModelRelPath());

  
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
 
 public void saveModel(ModelImprint model, ModelPath storePath, Session userSession) throws ModelStorageException
 {
  File modelFile = pathToFile(storePath, userSession.getUserProfile().getUserId());
  
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

 public ModelImprint getModel(ModelPath path, Session userSession) throws ModelStorageException
 {
  File modelFile = pathToFile(path, userSession.getUserProfile().getUserId());

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

 public void installModel(ModelPath modelPath, Session userSession) throws ModelStorageException
 {
  ModelImprint modImp = getModel(modelPath, userSession);

  SemanticModel sm = Age2ImprintConverter.convertImprintToModel(modImp);
  
  BufferLogger bLog = new BufferLogger();
  
  if( ! storage.updateSemanticModel(sm, bLog.getRootNode()) )
   throw new ModelStorageException("Model installation failed");
 }



 public AgeStorageAdm getStorageAdmin()
 {
  return storage;
 }



 public SubmissionReport getSubmissions(SubmissionQuery q, Session session) throws SubmissionDBException
 {
  // TODO check permission to list all submissions
  
  return submissionDB.getSubmissions(q);
 }



 public List<HistoryEntry> getSubmissionHistory(String sbmId, Session session) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  return submissionDB.getHistory(sbmId);
 }



 public SimpleLogNode deleteSubmission(String id, Session userSession) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  BufferLogger log = new BufferLogger();
  
  configuration.getSubmissionManager().removeSubmission(id, userSession.getUserProfile(), log.getRootNode());
  
  return log.getRootNode();
 }



 public SimpleLogNode restoreSubmission(String id, Session userSession) throws SubmissionDBException
 {
  // TODO check permission to list all submissions

  BufferLogger log = new BufferLogger();

  configuration.getSubmissionManager().restoreSubmission(id, userSession.getUserProfile(), log.getRootNode() );
  
  return log.getRootNode();
 }

// public void storeSubmission(SubmissionMeta sMeta)
// {
//  submissionDB.storeSubmission(sMeta);
// }


// public List<SubmissionImprint> getSubmissions(SubmissionQuery q)
// {
//  return submissionDB.getSubmissions(q);
// }
 
}
