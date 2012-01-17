package uk.ac.ebi.age.admin.server.mng;

import java.io.File;
import java.util.Map;

public interface FileSource
{
 File getFile( Map<String,String> params );
}
