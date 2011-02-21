package uk.ac.ebi.age.admin.shared.submission;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubmissionImprint implements IsSerializable
{
 private String id;
 private String description;
 private String submitter;
 private String modifier;
 private long   ctime;
 private long   mtime;

 private List<DataModuleImprint> modules;
 
 public String getId()
 {
  return id;
 }

 public void setId(String id)
 {
  this.id = id;
 }

 public String getDescription()
 {
  return description;
 }

 public void setDescription(String description)
 {
  this.description = description;
 }

 public String getSubmitter()
 {
  return submitter;
 }

 public void setSubmitter(String submitter)
 {
  this.submitter = submitter;
 }

 public String getModifier()
 {
  return modifier;
 }

 public void setModifier(String modifier)
 {
  this.modifier = modifier;
 }

 public long getCtime()
 {
  return ctime;
 }

 public void setCtime(long ctime)
 {
  this.ctime = ctime;
 }

 public long getMtime()
 {
  return mtime;
 }

 public void setMtime(long mtime)
 {
  this.mtime = mtime;
 }

 public List<DataModuleImprint> getModules()
 {
  return modules;
 }

 public void getModules( List<DataModuleImprint> mds )
 {
  modules=mds;
 }

 public void addDataModule(DataModuleImprint dimp)
 {
  if( modules == null )
   modules = new ArrayList<DataModuleImprint>(5);
  
  modules.add(dimp);
 }

}
