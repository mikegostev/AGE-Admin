package tx;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.file.ResourceManagerSystemException;
import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.LoggerFacade;

public class Test
{
 static final Log log = LogFactory.getLog(Test.class);
 static final LoggerFacade slog = new CommonsLoggingLogger(log);
 /**
  * @param args
  */
 public static void main(String[] args)
 {
  FileResourceManager frm = new FileResourceManager("m:\\txStore", "m:\\txTmp", false, slog);

  try
  {
   frm.start();
   String txId = frm.generatedUniqueTxId();
   frm.startTransaction(txId);
   
   frm.createResource(txId, "aa/file1.txt");
   
   OutputStream outputStream = frm.writeResource(txId,"aa/file1.txt");
   PrintWriter writer = new PrintWriter(outputStream);
   writer.print("This content is for file1.txt");
   writer.flush();
   writer.close();
   
   frm.commitTransaction(txId);
   frm.stop(FileResourceManager.SHUTDOWN_MODE_NORMAL);
  }
  catch(ResourceManagerSystemException e)
  {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  catch(ResourceManagerException e)
  {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }

  
 }

}
