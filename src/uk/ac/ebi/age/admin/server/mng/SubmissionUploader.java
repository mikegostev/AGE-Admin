package uk.ac.ebi.age.admin.server.mng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.SubmissionConstants;
import uk.ac.ebi.age.authz.ACR.Permit;
import uk.ac.ebi.age.authz.AuthDB;
import uk.ac.ebi.age.authz.BuiltInUsers;
import uk.ac.ebi.age.authz.Session;
import uk.ac.ebi.age.ext.authz.SystemAction;
import uk.ac.ebi.age.ext.authz.TagRef;
import uk.ac.ebi.age.ext.log.LogNode;
import uk.ac.ebi.age.ext.log.LogNode.Level;
import uk.ac.ebi.age.ext.submission.DataModuleMeta;
import uk.ac.ebi.age.ext.submission.Factory;
import uk.ac.ebi.age.ext.submission.FileAttachmentMeta;
import uk.ac.ebi.age.ext.submission.Status;
import uk.ac.ebi.age.ext.submission.SubmissionMeta;
import uk.ac.ebi.age.log.BufferLogger;
import uk.ac.ebi.age.log.Log2JSON;
import uk.ac.ebi.age.log.TooManyErrorsException;
import uk.ac.ebi.age.mng.submission.AttachmentAux;
import uk.ac.ebi.age.mng.submission.ModuleAux;
import uk.ac.ebi.age.mng.submission.SubmissionManager;
import uk.ac.ebi.age.transaction.ReadLock;
import uk.ac.ebi.age.util.StringUtil;
import uk.ac.ebi.mg.time.UniqTime;

import com.pri.util.stream.StreamPump;

public class SubmissionUploader implements UploadCommandListener
{
 private SubmissionManager sbmManager;
 private AuthDB authDB;

 public SubmissionUploader(SubmissionManager sm,  AuthDB adb)
 {
  sbmManager=sm;
  authDB = adb;
 }

 @Override
 public boolean processUpload(UploadRequest upReq, PrintWriter out)
 {
  BufferLogger log = new BufferLogger( uk.ac.ebi.age.conf.Constants.MAX_ERRORS );

  try
  {
   Session sess = Configuration.getDefaultConfiguration().getSessionManager().getSession();
   
   String userName = sess!=null?sess.getUser():BuiltInUsers.ANONYMOUS.getName();
   
   if( Configuration.getDefaultConfiguration().getPermissionManager().checkSystemPermission(SystemAction.CREATESUBM, userName) != Permit.ALLOW )
   {
    log.getRootNode().log(Level.ERROR, "User '" + sess.getUser() + "' is not allowed to load data");
    return false;
   }

   try
   {
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
    
    if( val != null )
    {
     val = val.trim();
     if( val.length() == 0 )
      val=null;
    }
    
    
    String tagsStr = upReq.getParams().get(SubmissionConstants.SUBMISSON_TAGS);
    
    if( tagsStr != null )
    {
     LogNode prsNode = log.getRootNode().branch("Parsing submission tags");
     List<TagRef> tags = parseTags( tagsStr, prsNode );
     
     if( tags == null )
      return false;
     
     
     ReadLock authLock = authDB.getReadLock();
     
     try
     {
      boolean error = false;
      
      for( TagRef tr : tags )
      {
       if( authDB.getTag(authLock, tr.getClassiferName(), tr.getTagName())  == null )
       {
        prsNode.log(Level.ERROR, "Invalid tag: "+tr.getClassiferName()+":"+tr.getTagName());
        error = true;
       }
      }
      
      if( error )
       return false;
     }
     finally
     {
      authDB.releaseLock(authLock);
     }
     
     prsNode.success();
     
     sMeta.setTags(tags);
    }
    
    
    if( val == null && blkSts != Status.NEW && blkSts != Status.UPDATEORNEW )
    {
     log.getRootNode().log(Level.ERROR, "Submission ID is not provided");
     
     return false;
    }
    
    String updateDescr = upReq.getParams().get(SubmissionConstants.THE_UPDATE_DESCR);
    
    sMeta.setId(val);
    
    sMeta.setDescription( upReq.getParams().get(SubmissionConstants.SUBMISSON_DESCR) );
    
    sMeta.setSubmitter( userName );
    sMeta.setModifier( userName );
    
    long time = UniqTime.getTime();
    
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
      
      if( val == null && blkSts != Status.NEW && blkSts != Status.UPDATEORNEW )
      {
       log.getRootNode().log(Level.ERROR, "Module "+partNo+". ID is not provided");
       
       return false;
      }
      
      dmMeta.setId( val );
      
      if( blkSts == Status.NEW || blkSts == Status.UPDATEORNEW || "on".equals(upReq.getParams().get(SubmissionConstants.MODULE_DESCRIPTION_UPDATE+partNo)) )
       dmMeta.setDescription(upReq.getParams().get(SubmissionConstants.MODULE_DESCRIPTION+partNo));

      if( blkSts == Status.NEW || blkSts == Status.UPDATEORNEW || "on".equals(upReq.getParams().get(SubmissionConstants.MODULE_FILE_UPDATE+partNo)) )
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

      tagsStr = upReq.getParams().get(SubmissionConstants.MODULE_TAGS+partNo);
      
      if( tagsStr != null )
      {
       LogNode prsNode = log.getRootNode().branch("Parsing module "+partNo+" tags");
       List<TagRef> tags = parseTags( tagsStr, prsNode );
       
       if( tags == null )
        return false;
       
       ReadLock authLock = authDB.getReadLock();
       
       try
       {
        boolean error = false;
        
        for( TagRef tr : tags )
        {
         if( authDB.getTag(authLock, tr.getClassiferName(), tr.getTagName())  == null )
         {
          prsNode.log(Level.ERROR, "Invalid tag: "+tr.getClassiferName()+":"+tr.getTagName());
          error = true;
         }
        }
        
        if( error )
         return false;
       }
       finally
       {
        authDB.releaseLock(authLock);
       }
       
       prsNode.success();
       
       dmMeta.setTags(tags);
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
      
      atAux.setOrder(partNo);
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_ID+partNo);
      
      if( val == null && blkSts != Status.NEW && blkSts != Status.UPDATEORNEW  )
      {
       log.getRootNode().log(Level.ERROR, "Attachment "+partNo+". ID is not provided");
       
       return false;
      }
      
      fatt.setId(val);
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_GLOBAL+partNo);
      
      fatt.setGlobal(val != null && "on".equals(val));
      
      
      val = upReq.getParams().get(SubmissionConstants.ATTACHMENT_ID_UPDATE+partNo);
      
      if( "on".equals(val) )
       atAux.setNewId( upReq.getParams().get(SubmissionConstants.ATTACHMENT_NEW_ID+partNo) );
      
      
      if( blkSts == Status.NEW || blkSts == Status.UPDATEORNEW || "on".equals( upReq.getParams().get(SubmissionConstants.ATTACHMENT_DESC_UPDATE+partNo) ) )
       fatt.setDescription( upReq.getParams().get(SubmissionConstants.ATTACHMENT_DESC+partNo) );

      if( blkSts == Status.NEW || blkSts == Status.UPDATEORNEW || "on".equals( upReq.getParams().get(SubmissionConstants.ATTACHMENT_FILE_UPDATE+partNo) ) )
      {
       File attFile = upReq.getFiles().get(SubmissionConstants.ATTACHMENT_FILE+partNo);
       
       if( attFile == null )
       {
        log.getRootNode().log(Level.ERROR,  "File for attachment "+partNo+" is not found");
        return false;
       }

       atAux.setFile(attFile);
      }
      
      
      tagsStr = upReq.getParams().get(SubmissionConstants.ATTACHMENT_TAGS+partNo);
      
      if( tagsStr != null )
      {
       LogNode prsNode = log.getRootNode().branch("Parsing attachment "+partNo+" tags");
       List<TagRef> tags = parseTags( tagsStr, prsNode );
       
       if( tags == null )
        return false;
       ReadLock authLock = authDB.getReadLock();
       
       try
       {
        boolean error = false;
        
        for( TagRef tr : tags )
        {
         if( authDB.getTag(authLock, tr.getClassiferName(), tr.getTagName())  == null )
         {
          prsNode.log(Level.ERROR, "Invalid tag: "+tr.getClassiferName()+":"+tr.getTagName());
          error = true;
         }
        }
        
        if( error )
         return false;
       }
       finally
       {
        authDB.releaseLock(authLock);
       }
       
       prsNode.success();
       
       fatt.setTags(tags);
      }
     }
    }

    boolean verifyOnly = "on".equals(upReq.getParams().get(SubmissionConstants.VERIFY_ONLY));
    
    sbmManager.storeSubmission(sMeta, updateDescr, log.getRootNode(), verifyOnly );
    
   }
   catch( TooManyErrorsException e )
   {
    int errs = e.getErrorCount();
    
    log.resetErrorCounter();
    
    log.getRootNode().log(Level.ERROR, "Too many errors: "+errs);
    return false;
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
   String logBody = Log2JSON.convert(log.getRootNode());
   
   out.print("<html><body>OK-"+upReq.getParams().get(SubmissionConstants.SUBMISSON_KEY)+":["+log.getRootNode().getLevel().name()+"]<pre>\n"+Constants.beginJSONSign+"\n(");
   out.print(logBody);
   out.print(")\n"+Constants.endJSONSign+"\n</pre></body></html>");
  }

  return true;
 }

 private List<TagRef> parseTags(String tagsStr, LogNode logn )
 {
  tagsStr = tagsStr.trim();

  List<TagRef> tags = new ArrayList<TagRef>();
  
  for( String ts : StringUtil.splitString(tagsStr, ",") )
  {
   List<String> parts = StringUtil.splitString(ts, ":");
   
   if( parts.size() != 2 )
   {
    logn.log(Level.ERROR, "Invalid tag string: '"+tagsStr+"'");
    return null;
   }
   
   TagRef tr = new TagRef();
   tr.setTagName(parts.get(0).trim());
   
   String p2 = parts.get(1);
   
   int colPos = p2.indexOf('=');
   
   if( colPos != -1 )
   {
    tr.setTagName(p2.substring(0,colPos).trim() );
    tr.setTagValue(p2.substring(colPos+1).trim() );
   }
   else
    tr.setTagName(p2);
   
   tags.add(tr);
  }
  
  return tags;
 }

}
