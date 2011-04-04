package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.util.Map;

import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.service.submission.SubmissionDB;

public class DocumentFileSource implements FileSource
{

 private SubmissionDB submDb;

 public DocumentFileSource(SubmissionDB sdb)
 {
  submDb = sdb;
 }

 @Override
 public File getFile(Map<String, String> params)
 {
  String clustId = params.get(Constants.clusterIdParameter);
  
  if( clustId == null )
   return null;
  
  String fileId = params.get(Constants.documentIdParameter);
 
  if( fileId == null )
   return null;
  
  String verStr = params.get(Constants.versionParameter);
  
  if( verStr == null )
   return null;

  long ts;
  
  try
  {
   ts = Long.parseLong(verStr);
  }
  catch (Exception e) 
  {
   return null;
  }
  

  return submDb.getDocument(clustId,fileId,ts);
 }

}
