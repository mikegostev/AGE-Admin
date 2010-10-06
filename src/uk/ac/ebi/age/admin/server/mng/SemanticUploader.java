package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.storage.AgeStorageAdm;

public class SemanticUploader implements UploadCommandListener
{
 private AgeStorageAdm storAdm;
 
 public SemanticUploader( AgeStorageAdm s )
 {
  storAdm=s;
 }
 
 @Override
 public boolean processUpload(UploadRequest upReq, Session sess, PrintWriter out)
 {
  File f = upReq.getFiles().get(0);
  
//  try
//  {
//   SemanticModel model = new SemanticModelImpl(f.toURI().toString(), ModelFactoryImpl.getInstance());
//   storAdm.updateSemanticModel(model);
//  }
//  catch(ModelException e)
//  {
//   // TODO Auto-generated catch block
//   e.printStackTrace();
//  }
//  catch(ModelStoreException e)
//  {
//   // TODO Auto-generated catch block
//   e.printStackTrace();
//  }
  
  return true;
 }

}
