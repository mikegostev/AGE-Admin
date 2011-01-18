package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Map;

import uk.ac.ebi.age.admin.client.common.SubmissionConstants;
import uk.ac.ebi.age.admin.server.model.DataModuleMeta;
import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.LogNode.Level;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.SubmissionManager;
import uk.ac.ebi.age.model.writable.DataModuleWritable;
import uk.ac.ebi.age.service.IdGenerator;
import uk.ac.ebi.age.storage.AgeStorageAdm;

import com.pri.util.stream.StreamPump;

public class SubmissionUploader implements UploadCommandListener
{
 private AgeStorageAdm storAdm;

 public SubmissionUploader(AgeStorageAdm storage)
 {
  storAdm=storage;
 }

 @Override
 public boolean processUpload(UploadRequest upReq, Session sess, PrintWriter out)
 {
  BufferLogger log = new BufferLogger();

  try
  {
   if(!sess.getUserProfile().isUploadAllowed())
   {
    log.getRootNode().log(Level.ERROR, "User '" + sess.getUserProfile().getUserName() + "' is not allowed to load data");
    return false;
   }

   try
   {
    if(upReq.getFiles() == null)
    {
     log.getRootNode().log(Level.ERROR, "No files found");
     return false;
    }
    
    SubmissionMeta sMeta = new SubmissionMeta();
    
    sMeta.setId( IdGenerator.getInstance().getStringId("submission") );
    
    sMeta.setDescription( upReq.getParams().get(SubmissionConstants.SUBMISSON_DESCR) );
    sMeta.setSubmitter( sess.getUserProfile().getUserName() );
    
    long time = System.currentTimeMillis();
    
    sMeta.setSubmissionTime(time);
    sMeta.setModificationTime(time);
    
    for(Map.Entry<String, File> me : upReq.getFiles().entrySet())
    {
     String fName = me.getKey();
     
     if( ! fName.startsWith(SubmissionConstants.MODULE_FILE) )
     {
      log.getRootNode().log(Level.ERROR, "Invalid name of module file field: '"+fName+"'. Must start with '"+SubmissionConstants.MODULE_FILE+"'");
      return false;
     }
     
     String dmRId = fName.substring(SubmissionConstants.MODULE_FILE.length());
     
     String dmDesc = upReq.getParams().get(SubmissionConstants.MODULE_NAME+dmRId);
     
     DataModuleMeta dmMeta = new DataModuleMeta();
     
     dmMeta.setId( IdGenerator.getInstance().getStringId("datamodule") );
     dmMeta.setDescription( dmDesc );
     
     sMeta.addDataModule( dmMeta );
    }
    
    String text = null;

    ByteArrayOutputStream bais = new ByteArrayOutputStream();

    for(File f : upReq.getFiles().values())
    {
     FileInputStream fis = new FileInputStream(f);
     StreamPump.doPump(fis, bais, false);
     fis.close();

     bais.write('\n');
     bais.write('\n');
    }

    bais.close();

    text = new String(bais.toByteArray());

    // AgeTabSubmission sbm = AgeTabSyntaxParser.getInstance().parse(text);
    //
    // SubmissionWritable submission =
    // AgeTabSemanticValidator.getInstance().parse(sbm,
    // SemanticManager.getInstance().getContextModel(sess.getUserProfile()));

    DataModuleWritable submission = SubmissionManager.getInstance()
      .prepareSubmission(text, null, false, sess.getUserProfile(), storAdm, log.getRootNode());

    // BufferLogger.printBranch(log.getRootNode());

    try
    {
     if(submission != null)
      storAdm.storeDataModule(submission);
    }
    catch(Exception e)
    {
     log.getRootNode().log(Level.ERROR, e.getMessage());
    }

   }
   catch(Exception e)
   {
    log.getRootNode().log(Level.ERROR, e.getMessage());

    e.printStackTrace();
   }
  }
  finally
  {
   out.print("<html><body><pre>(");
   out.print(Log2JSON.convert(log.getRootNode()));
   out.print(")</pre></body></html>");
  }

  return true;
 }

}
