package uk.ac.ebi.age.admin.client.model;

import uk.ac.ebi.age.admin.client.common.Directory;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ModelStorage implements IsSerializable
{
 private Directory publicDir;
 private Directory userDir;

 public Directory getUserDirectory()
 {
  return userDir;
 }

 public void setUserDirectory(Directory dir)
 {
  userDir=dir;
 }

 public Directory getPublicDirectory()
 {
  return publicDir;
 }

 public void setPublicDirectory(Directory dir)
 {
  publicDir=dir;
 }

}
