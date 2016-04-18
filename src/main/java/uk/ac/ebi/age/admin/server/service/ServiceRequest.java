package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ServiceRequest
{
 public static class FileInfo
 {
  private File file;
  private String fileName;
  
  public File getFile()
  {
   return file;
  }

  public void setFile(File file)
  {
   this.file = file;
  }

  public String getFileName()
  {
   return fileName;
  }

  public void setFileName(String fileName)
  {
   this.fileName = fileName;
  }
 }
 
 private String command;
 private Map<String,String> params = new TreeMap<String,String>();
 private Map<String, FileInfo> files  = new HashMap<String, FileInfo>();
 
 public String getHandlerName()
 {
  return command;
 }
 
 public void setHandlerName(String command)
 {
  this.command = command;
 }
 
 public Map<String, String> getParams()
 {
  return params;
 }
 
 public Map<String, FileInfo> getFiles()
 {
  return files;
 }
 
 public void addFile(String name, String string, File file)
 {
  FileInfo fi = new FileInfo();
  
  fi.setFile(file);
  fi.setFileName(string);
  
  files.put(name, fi);
 }
 
 public void clearFiles()
 {
  files.clear();
 }
 
 public void addParam( String name, String val )
 {
  params.put(name, val);
 }


}
