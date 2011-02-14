package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Map;

import uk.ac.ebi.age.admin.server.model.SubmissionMeta;
import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.LogNode.Level;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.SubmissionManager;
import uk.ac.ebi.age.model.DataModuleMeta;
import uk.ac.ebi.age.service.IdGenerator;

import com.pri.util.stream.StreamPump;

public class SubmissionUploader implements UploadCommandListener
{
 private AgeAdmin admin;

 public SubmissionUploader(AgeAdmin storage)
 {
  admin=storage;
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
     
     dmMeta.setDescription( dmDesc );
     
     dmMeta.setModificationTime(time);
     
     sMeta.addDataModule( dmMeta );
     
     ByteArrayOutputStream bais = new ByteArrayOutputStream();
     
     FileInputStream fis = new FileInputStream(me.getValue());
     StreamPump.doPump(fis, bais, false);
     fis.close();

     bais.write('\n');
     bais.close();
     
     byte[] barr = bais.toByteArray();
     String enc = "UTF-8";
     
     if( barr.length >= 2 && (barr[0] == -1 && barr[1] == -2) || (barr[0] == -2 && barr[1] == -1) )  
      enc="UTF-16";

     dmMeta.setText(new String(bais.toByteArray(),enc));
    }

    if(  SubmissionManager.getInstance()
      .storeSubmission(sMeta.getDataModules(), false, sess.getUserProfile(), admin.getStorageAdmin(), log.getRootNode()) )
    {
     sMeta.setId(SubmissionConstants.submissionIDPrefix+IdGenerator.getInstance().getStringId(SubmissionConstants.submissionIDDomain));
    
     admin.storeSubmission( sMeta );
    }
     
     // BufferLogger.printBranch(log.getRootNode());

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
