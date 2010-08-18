package uk.ac.ebi.age.admin.client.model;

import com.pri.util.Directory;

public class ModelStorage
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
