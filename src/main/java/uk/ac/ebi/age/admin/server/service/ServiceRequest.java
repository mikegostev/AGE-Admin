package uk.ac.ebi.age.admin.server.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ServiceRequest
{
 private String command;
 private Map<String,String> params = new TreeMap<String,String>();
 private Map<String, File> files  = new HashMap<String, File>();
 
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
 
 public Map<String, File> getFiles()
 {
  return files;
 }
 
 public void addFile(String name, File file)
 {
  files.put(name, file);
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
