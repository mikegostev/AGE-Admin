package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;

public class UploadManager
{
 private Map<String, UploadCommandListener> lsnrs = new TreeMap<String, UploadCommandListener>();

 public void processUpload(UploadRequest upReq, Session sess)
 {
  UploadCommandListener lsnr = lsnrs.get(upReq.getCommand());
  
  if( lsnr != null )
  {
   if( lsnr.processUpload(upReq,sess) && upReq.getFiles() != null )
   {
    for( File f : upReq.getFiles() )
     f.delete();
   }
  }
 }

 public void addUploadCommandListener( String cmd, UploadCommandListener ls )
 {
  lsnrs.put(cmd, ls);
 }
 
}
