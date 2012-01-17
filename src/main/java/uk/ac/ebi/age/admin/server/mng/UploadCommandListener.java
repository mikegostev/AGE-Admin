package uk.ac.ebi.age.admin.server.mng;

import java.io.PrintWriter;

import uk.ac.ebi.age.admin.server.service.UploadRequest;

public interface UploadCommandListener
{

 boolean processUpload(UploadRequest upReq, PrintWriter printWriter);

}
