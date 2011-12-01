package uk.ac.ebi.age.admin.shared.submission;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DataModuleImprint implements IsSerializable
{
 private String id;
 private String description;
 private String modifier;
 private long   mtime;

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

 public String getModifier()
 {
  return modifier;
 }

 public void setModifier(String modifier)
 {
  this.modifier = modifier;
 }

 public long getMtime()
 {
  return mtime;
 }

 public void setMtime(long mtime)
 {
  this.mtime = mtime;
 }

}
