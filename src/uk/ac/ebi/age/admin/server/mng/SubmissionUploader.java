package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Map;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.LogNode.Level;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.SubmissionManager;

import com.pri.util.stream.StreamPump;

public class SubmissionUploader implements UploadCommandListener
{
 private SubmissionManager sbmManager;

 public SubmissionUploader(SubmissionManager sm)
 {
  sbmManager=sm;
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
    
    String userName = sess.getUserProfile().getUserName();
    
    SubmissionMeta sMeta = new SubmissionMeta();
    
    sMeta.setForUpdate( "true".equals(upReq.getParams().get(SubmissionConstants.SUBMISSON_UPDATE)) );
    
    sMeta.setId(upReq.getParams().get(SubmissionConstants.SUBMISSON_ID));
    
    sMeta.setDescription( upReq.getParams().get(SubmissionConstants.SUBMISSON_DESCR) );
    sMeta.setSubmitter( userName );
    sMeta.setModifier( userName );
    
    long time = System.currentTimeMillis();
    
    sMeta.setSubmissionTime(time);
    sMeta.setModificationTime(time);
    
    int atN=0;
    
    for(Map.Entry<String, File> me : upReq.getFiles().entrySet())
    {
     String fName = me.getKey();
     
     if(  fName.startsWith(SubmissionConstants.MODULE_FILE) )
     {
      // log.getRootNode().log(Level.ERROR,
      // "Invalid name of module file field: '"+fName+"'. Must start with '"+SubmissionConstants.MODULE_FILE+"'");
      // return false;

      String dmRId = fName.substring(SubmissionConstants.MODULE_FILE.length());

      String dmDesc = upReq.getParams().get(SubmissionConstants.MODULE_NAME + dmRId);

      DataModuleMeta dmMeta = new DataModuleMeta();

      dmMeta.setId(upReq.getParams().get(SubmissionConstants.MODULE_ID + dmRId));
      dmMeta.setForUpdate("true".equals(upReq.getParams().get(SubmissionConstants.MODULE_UPDATE + dmRId)));
      
      dmMeta.setDescription(dmDesc);

      dmMeta.setSubmissionTime(time);
      dmMeta.setModificationTime(time);
      dmMeta.setSubmitter(userName);
      dmMeta.setModifier(userName);

      sMeta.addDataModule(dmMeta);

      ByteArrayOutputStream bais = new ByteArrayOutputStream();

      FileInputStream fis = new FileInputStream(me.getValue());
      StreamPump.doPump(fis, bais, false);
      fis.close();

      bais.close();

      byte[] barr = bais.toByteArray();
      String enc = "UTF-8";

      if(barr.length >= 2 && (barr[0] == -1 && barr[1] == -2) || (barr[0] == -2 && barr[1] == -1))
       enc = "UTF-16";

      dmMeta.setText(new String(bais.toByteArray(), enc));
     }
     else if( fName.startsWith(SubmissionConstants.ATTACHMENT_FILE) )
     {
      atN++;
      
      String atRId = fName.substring(SubmissionConstants.ATTACHMENT_FILE.length());
      
      String atID = upReq.getParams().get(SubmissionConstants.ATTACHMENT_ID+atRId);
      
      if( atID == null )
      {
       log.getRootNode().log(Level.ERROR,
        "Attachment file "+atN+" has no ID so it can't be referenced");
       return false;
      }
      
      FileAttachmentMeta fAtMeta = new FileAttachmentMeta();
      
      fAtMeta.setOriginalId(atID);
      fAtMeta.setDescription(upReq.getParams().get(SubmissionConstants.ATTACHMENT_DESC+atRId));
      
      fAtMeta.setSubmissionTime(time);
      fAtMeta.setModificationTime(time);
      
      fAtMeta.setSubmitter( userName );
      fAtMeta.setModifier( userName );
      
      String glbPrm = upReq.getParams().get(SubmissionConstants.ATTACHMENT_GLOBAL+atRId);
      fAtMeta.setGlobal( glbPrm != null && "on".equals(glbPrm) );
      fAtMeta.setAux(me.getValue());
      
      sMeta.addAttachment( fAtMeta );
     }

    }

    sbmManager.storeSubmission(sMeta, sess.getUserProfile(), log.getRootNode());
    
//    if(  SubmissionManager.getInstance()
//      .storeSubmission(sMeta, sess.getUserProfile(), admin.getStorageAdmin(), log.getRootNode()) )
//    {
//     sMeta.setId(SubmissionConstants.submissionIDPrefix+IdGenerator.getInstance().getStringId(SubmissionConstants.submissionIDDomain));
//    
//     admin.storeSubmission( sMeta );
//    }
     
     // BufferLogger.printBranch(log.getRootNode());

   }
   catch(Exception e)
   {
    String msg = e.getMessage();
    
    if( msg == null )
     msg = "Exception: "+e.getClass().getName();
    
    log.getRootNode().log(Level.ERROR, msg);

    e.printStackTrace();
   }
  }
  finally
  {
   out.print("<html><body>OK-"+upReq.getParams().get(SubmissionConstants.SUBMISSON_KEY)+"<pre>(");
   out.print(Log2JSON.convert(log.getRootNode()));
   out.print(")</pre></body></html>");
  }

  return true;
 }

}
