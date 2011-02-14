package uk.ac.ebi.age.admin.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StoreNode implements IsSerializable
{
 private String name;
 private List<StoreNode> subDirs;
 private List<StoreNode> files;
 private boolean isDir;
 
 private StoreNode parent;

 public StoreNode()
 {}
 
 public StoreNode(String nm)
 {
  name=nm;
 }
 
 public String getName()
 {
  return name;
 }
 
 public List<StoreNode> getFiles()
 {
  return files;
 }
 
 public List<StoreNode> getSubnodes()
 {
  return subDirs;
 }
 
 public void addSubNode(StoreNode sd)
 {
  if( subDirs == null )
   subDirs = new ArrayList<StoreNode>(5);
  
  sd.setDirectory(true);
  subDirs.add(sd);
  
  sd.parent = this;
 }
 
 public StoreNode addFile(String nm)
 {
  if( files == null )
   files = new ArrayList<StoreNode>(5);
  
  StoreNode d = new StoreNode(nm);
  d.setDirectory(false);
  
  files.add(d);
  d.parent = this;
  
  return d;
 }

 public void setName(String string)
 {
  name=string;
 }
 
 public StoreNode getParent()
 {
  return parent;
 }

 public void setDirectory(boolean isDir)
 {
  this.isDir = isDir;
 }

 public boolean isDirectory()
 {
  return isDir;
 }
}
