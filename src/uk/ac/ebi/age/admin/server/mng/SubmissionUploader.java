package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.LogNode.Level;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.SubmissionManager;
import uk.ac.ebi.age.model.writable.SubmissionWritable;
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
   if(upReq.getFiles() == null)
    return false;

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

//   AgeTabSubmission sbm = AgeTabSyntaxParser.getInstance().parse(text);
//   
//   SubmissionWritable submission = AgeTabSemanticValidator.getInstance().parse(sbm,
//     SemanticManager.getInstance().getContextModel(sess.getUserProfile()));

   SubmissionWritable submission = SubmissionManager.getInstance().prepareSubmission(text, null, false, sess.getUserProfile(), storAdm, log.getRootNode());
   
//   BufferLogger.printBranch(log.getRootNode());
   
   try
   {
    if( submission != null )
     storAdm.storeSubmission(submission);
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

  out.print("<html><body><pre>(");
  out.print(Log2JSON.convert(log.getRootNode()));
  out.print(")</pre></body></html>");
  
  return true;
 }

}
