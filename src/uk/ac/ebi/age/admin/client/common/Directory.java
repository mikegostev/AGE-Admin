package uk.ac.ebi.age.admin.client.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Directory implements Serializable, IsSerializable
{
 private String name;
 private List<Directory> subDirs;
 private List<String> files;

 public Directory()
 {}
 
 public Directory(String nm)
 {
  name=nm;
 }
 
 public String getName()
 {
  return name;
 }
 
 public List<String> getFiles()
 {
  return files;
 }
 
 public List<Directory> getSubdirectories()
 {
  return subDirs;
 }
 
 public void addSubdirectory(Directory sd)
 {
  if( subDirs == null )
   subDirs = new ArrayList<Directory>(5);
  
  subDirs.add(sd);
 }
 
 public void addFile(String nm)
 {
  if( files == null )
   files = new ArrayList<String>(5);
  
  files.add(nm);
 }
}
