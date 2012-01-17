package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import uk.ac.ebi.age.admin.server.mng.Configuration;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.authz.Session;

import com.pri.util.stream.StreamPump;

public class UploadSvc extends ServiceServlet
{

 private static final long serialVersionUID = -4888846889606953616L;

 protected void service(HttpServletRequest req, HttpServletResponse resp, Session sess) throws IOException
 {
  //TODO permission control
//  if( ! sess.getUserProfile().isUploadAllowed() )
//  {
//   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
//   return;
//  }
  
  if( sess == null )
  {
   resp.sendError(HttpServletResponse.SC_FORBIDDEN);
   return;
  }
  
  if( ! req.getMethod().equals("POST") )
  {
   resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
   return;
  }
  
  UploadRequest upReq = new UploadRequest();
  
  boolean isMultipart = ServletFileUpload.isMultipartContent(req);

  if( ! isMultipart )
  {
   
   for(Enumeration<?> pnames = req.getParameterNames(); pnames.hasMoreElements();)
   {
    String pname = (String)pnames.nextElement();
    
    if(Constants.uploadHandlerParameter.equals(pname))
     upReq.setCommand(req.getParameter(pname));
    else
     upReq.addParam(pname, req.getParameter(pname));
   }
    
   if( upReq.getCommand() == null )
    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
   else
    Configuration.getDefaultConfiguration().getUploadManager().processUpload(upReq, resp.getWriter());

   return;
  }

  
  // Create a new file upload handler
  ServletFileUpload upload = new ServletFileUpload();
  
  
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
     if(Constants.uploadHandlerParameter.equals(name))
     {
      try
      {
       upReq.setCommand(Streams.asString(stream));
       stream.close();
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

     upReq.addFile(name, tmpf);
    }
   }
  }
  catch(Exception ex)
  {
   ex.printStackTrace();
   resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
   return;
  }
  
  Configuration.getDefaultConfiguration().getUploadManager().processUpload(upReq, resp.getWriter());
 }
}
