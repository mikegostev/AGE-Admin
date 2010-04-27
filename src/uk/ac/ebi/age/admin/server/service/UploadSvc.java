package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import uk.ac.ebi.age.admin.client.common.UploadService;
import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.server.user.Session;

import com.pri.util.stream.StreamPump;

public class UploadSvc extends ServiceServlet
{
 
 protected void service(HttpServletRequest req, HttpServletResponse resp, Session sess) throws IOException
 {
  if( ! req.getMethod().equals("POST") )
   return;
  
  if( ! sess.getUserProfile().isUploadAllowed() )
   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
  
  boolean isMultipart = ServletFileUpload.isMultipartContent(req);

  if( ! isMultipart )
   return;
  
  // Create a new file upload handler
  ServletFileUpload upload = new ServletFileUpload();
  
  UploadRequest upReq = new UploadRequest();
  
  try
  {
   // Parse the request
   FileItemIterator iter = upload.getItemIterator(req);
   
   while(iter.hasNext())
   {
    FileItemStream item = iter.next();
    String name = item.getFieldName();
    InputStream stream = item.openStream();
    
    if(item.isFormField())
    {
     if(UploadService.commandField.equals(name))
     {
      try
      {
       upReq.setCommand(Streams.asString(stream));
      }
      catch (Exception e)
      {
      }
     }
     else
     {
      upReq.addParam(name, Streams.asString(stream));
     }

//     System.out.println("Form field " + name + " with value " + Streams.asString(stream) + " detected.");
    }
    else
    {
//     System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
     InputStream uploadedStream = item.openStream();
     
     File tmpf = sess.makeTempFile();
     
     StreamPump.doPump(uploadedStream, new FileOutputStream(tmpf), true);

     upReq.addFile(tmpf);
    }
   }
  }
  catch(Exception ex)
  {
   ex.printStackTrace();
   resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
   return;
  }
  
  Configuration.getDefaultConfiguration().getUploadManager().processUpload(upReq,sess);
 }
}
