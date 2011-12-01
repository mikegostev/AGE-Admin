package uk.ac.ebi.age.admin.server.mng;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileSourceManager
{
 private Map<String, FileSource> srcMap = Collections.synchronizedMap( new HashMap<String, FileSource>() );

 public FileSource getFileSource(String handler)
 {
  return srcMap.get(handler);
 }
 
 public void addFileSource( String srcName, FileSource fc )
 {
  srcMap.put(srcName, fc);
 }
 
 public FileSource removeFileSource( String srcName )
 {
  return srcMap.remove(srcName);
 }

}
