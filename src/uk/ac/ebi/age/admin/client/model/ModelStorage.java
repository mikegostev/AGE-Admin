package uk.ac.ebi.age.admin.client.model;

import uk.ac.ebi.age.admin.client.common.StoreNode;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ModelStorage implements IsSerializable
{
 private StoreNode publicDir;
 private StoreNode userDir;

 public StoreNode getUserDirectory()
 {
  return userDir;
 }

 public void setUserDirectory(StoreNode dir)
 {
  userDir=dir;
 }

 public StoreNode getPublicDirectory()
 {
  return publicDir;
 }

 public void setPublicDirectory(StoreNode dir)
 {
  publicDir=dir;
 }

}
