package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.Factory;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.Status;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.LogNode.Level;
import uk.ac.ebi.age.log.impl.BufferLogger;
import uk.ac.ebi.age.mng.submission.AttachmentAux;
import uk.ac.ebi.age.mng.submission.ModuleAux;
import uk.ac.ebi.age.mng.submission.SubmissionManager;

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
    
    String userName = sess.getUserProfile().getUserName();
    
    SubmissionMeta sMeta = Factory.createSubmissionMeta();
    
    String val = upReq.getParams().get(SubmissionConstants.SUBMISSON_STATUS);
    
    if( val == null )
    {
     log.getRootNode().log(Level.ERROR, "The '"+SubmissionConstants.SUBMISSON_STATUS+"' parameter should be defined");
     return false;
    }
    
    Status blkSts = null;

    try
    {
     blkSts = Status.valueOf(val);
    }
    catch (Exception e) 
    {
     log.getRootNode().log(Level.ERROR, "Invalid status of submission: '"+val+"'");
     
     return false;
    }

    sMeta.setStatus( blkSts );
    
    val = upReq.getParams().get(SubmissionConstants.SUBMISSON_ID);
    
    if( val == null && blkSts != Status.NEW )
    {
     log.getRootNode().log(Level.ERROR, "Submission ID is not provided");
     
     return false;
    }
    
    String updateDescr = upReq.getParams().get(SubmissionConstants.THE_UPDATE_DESCR);
    
    sMeta.setId(upReq.getParams().get(val) );
    
    sMeta.setDescription( upReq.getParams().get(SubmissionConstants.SUBMISSON_DESCR) );
    
    sMeta.setSubmitter( userName );
    sMeta.setModifier( userName );
    
    long time = System.currentTimeMillis();
    
    sMeta.setSubmissionTime(time);
    sMeta.setModificationTime(time);
    
    int nPrms = upReq.getParams().size();
    
    for( int partNo = 1; partNo <= nPrms; partNo++ )
    {
     String param = SubmissionConstants.MODULE_STATUS+partNo;
     
     val = upReq.getParams().get(param);
     
     if( val != null )
     {
      blkSts = null;
      
      try
      {
       blkSts = Status.valueOf(val);
      }
      catch (Exception e) 
      {
       log.getRootNode().log(Level.ERROR, "Invalid status for module "+partNo+": '"+val+"'");
        return false;
      }
      
      if( blkSts == Status.KEEP )
       continue;
      
      DataModuleMeta dmMeta = Factory.createDataModuleMeta();
      dmMeta.setSubmissionTime(time);
      dmMeta.setModificationTime(time);
      dmMeta.setSubmitter(userName);
      dmMeta.setModifier(userName);
      
      ModuleAux mAux = new ModuleAux();
      dmMeta.setAux(mAux);

      mAux.setStatus( blkSts );
      mAux.setOrder(partNo);
      
      sMeta.addDataModule(dmMeta);
 
      val = upReq.getParams().get(SubmissionConstants.MODULE_ID+partNo);
      
      if( val == null && blkSts != Status.NEW )
      {
       log.getRootNode().log(Level.ERROR, "Module "+partNo+". ID is not provided");
       
       return false;
      }
      
      dmMeta.setId( val );
      
      if( dmMeta.getId() == null || "on".equals(upReq.getParams().get(SubmissionConstants.MODULE_DESCRIPTION_UPDATE+partNo)) )
       dmMeta.setDescription(upReq.getParams().get(SubmissionConstants.MODULE_DESCRIPTION+partNo));

      if( dmMeta.getId() == null || "on".equals(upReq.getParams().get(SubmissionConstants.MODULE_FILE_UPDATE+partNo)) )
      {
       File modFile = upReq.getFiles().get(SubmissionConstants.MODULE_FILE+partNo);
       
       if( modFile == null )
       {
        log.getRootNode().log(Level.ERROR,
         "File for module "+partNo+" is not found");
        return false;
       }
       
       ByteArrayOutputStream bais = new ByteArrayOutputStream();

       FileInputStream fis = new FileInputStream(modFile);
       StreamPump.doPump(fis, bais, false);
       fis.close();

       bais.close();

       byte[] barr = bais.toByteArray();
       String enc = "UTF-8";

       if(barr.length >= 2 && (barr[0] == -1 && barr[1] == -2) || (barr[0] == -2 && barr[1] == -1))
        enc = "UTF-16";

       dmMeta.setText(new String(bais.toByteArray(), enc));
      }

      continue;
     }
     
     param = SubmissionConstants.ATTACHMENT_STATUS+partNo;
     
     val = upReq.getParams().get(param);
  
     
     if( val != null )
     {
      blkSts = null;
      
      try
      {
       blkSts = Status.valueOf(val);
      }
      catch (Exception e) 
      {
       log.getRootNode().log(Level.ERROR,
         "Invalid status for attachment "+partNo+": '"+val+"'");
        return false;
      }

      if( blkSts == Status.KEEP )
       continue;

      
      FileAttachmentMeta fatt = Factory.createFileAttachmentMeta();
      fatt.setSubmissionTime(time);
      fatt.setModificationTime(time);
      
      fatt.setSubmitter( userName );
      fatt.setModifier( userName );

      sMeta.addAttachment(fatt);
      
      AttachmentAux atAux = new AttachmentAux();
      fatt.setAux(atAux);
      atAux.setStatus(blkSts);
      
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_ID+partNo);
      
      if( val == null && blkSts != Status.NEW )
      {
       log.getRootNode().log(Level.ERROR, "Attachment "+partNo+". ID is not provided");
       
       return false;
      }
      
      fatt.setId(val);
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_GLOBAL+partNo);
      
      fatt.setGlobal(val != null && "on".equals(val));
      
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_ID_UPDATE+partNo);
      
      if( val != null && "on".equals(val) )
       atAux.setNewId( upReq.getParams().get(SubmissionConstants.ATTACHMENT_NEW_ID+partNo) );
      
      
      if( fatt.getId() == null || (val != null && "on".equals( upReq.getParams().get(SubmissionConstants.ATTACHMENT_DESC_UPDATE+partNo)) ) )
       fatt.setDescription( upReq.getParams().get(SubmissionConstants.ATTACHMENT_DESC+partNo) );

      if( fatt.getId() == null || (val != null && "on".equals( upReq.getParams().get(SubmissionConstants.ATTACHMENT_FILE_UPDATE+partNo)) ) )
      {
       File attFile = upReq.getFiles().get(SubmissionConstants.ATTACHMENT_FILE+partNo);
       
       if( attFile == null )
       {
        log.getRootNode().log(Level.ERROR,  "File for attachment "+partNo+" is not found");
        return false;
       }

       atAux.setFile(attFile);
      }
     }
    }

    sbmManager.storeSubmission(sMeta, updateDescr, sess.getUserProfile(), log.getRootNode());
    
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
