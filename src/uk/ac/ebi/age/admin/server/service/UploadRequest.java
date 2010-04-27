package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UploadRequest
{
 private String command;
 private Map<String,String> params = new TreeMap<String,String>();
 private List<File> files  = new ArrayList<File>(2);
 
 public String getCommand()
 {
  return command;
 }
 
 public void setCommand(String command)
 {
  this.command = command;
 }
 
 public Map<String, String> getParams()
 {
  return params;
 }
 
 public List<File> getFiles()
 {
  return files;
 }
 
 public void addFile(File file)
 {
  files.add(file);
 }
 
 public void addParam( String name, String val )
 {
  params.put(name, val);
 }
}
