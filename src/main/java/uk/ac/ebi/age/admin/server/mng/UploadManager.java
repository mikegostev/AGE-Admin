package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import uk.ac.ebi.age.admin.server.service.UploadRequest;

public class UploadManager
{
 private Map<String, UploadCommandListener> lsnrs = new TreeMap<String, UploadCommandListener>();

 public void processUpload(UploadRequest upReq, PrintWriter printWriter)
 {
  try
  {
   if(upReq.getCommand() == null)
    return;

   UploadCommandListener lsnr = lsnrs.get(upReq.getCommand());

   if(lsnr != null)
   {
    lsnr.processUpload(upReq, printWriter);

    // if( lsnr.processUpload(upReq,sess,printWriter) && upReq.getFiles() !=
    // null )
    // {
    // for( File f : upReq.getFiles().values() )
    // f.delete();
    // }
   }

  }
  finally
  {
   for(File f : upReq.getFiles().values())
   {
    if( f.exists() )
     f.delete();
   }
  }
 }

 public void addUploadCommandListener( String cmd, UploadCommandListener ls )
 {
  lsnrs.put(cmd, ls);
 }
 
}
