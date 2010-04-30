package uk.ac.ebi.age.admin.server.mng;

import uk.ac.ebi.age.admin.server.service.UploadRequest;
import uk.ac.ebi.age.admin.server.user.Session;

public interface UploadCommandListener
{

 boolean processUpload(UploadRequest upReq, Session sess);

}
