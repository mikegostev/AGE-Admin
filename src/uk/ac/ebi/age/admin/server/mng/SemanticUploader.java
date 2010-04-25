package uk.ac.ebi.age.admin.server.mng;

import java.io.File;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;
import uk.ac.ebi.age.model.ModelException;
import uk.ac.ebi.age.model.SemanticModel;
import uk.ac.ebi.age.model.impl.ModelFactoryImpl;
import uk.ac.ebi.age.model.impl.SemanticModelImpl;
import uk.ac.ebi.age.storage.AgeStorageAdm;

public class SemanticUploader implements UploadCommandListener
{
 private AgeStorageAdm storAdm;
 
 public SemanticUploader( AgeStorageAdm s )
 {
  storAdm=s;
 }
 
 @Override
 public void processUpload(UploadRequest upReq, Session sess)
 {
  File f = upReq.getFiles().get(0);
  
  try
  {
   SemanticModel model = new SemanticModelImpl(f.toURI().toString(), ModelFactoryImpl.getInstance());
   storAdm.updateSemanticModel(model);
  }
  catch(ModelException e)
  {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }

}
