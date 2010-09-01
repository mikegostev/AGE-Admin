package uk.ac.ebi.age.admin.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelStorageException extends Exception implements IsSerializable
{

 public ModelStorageException()
 {}
 
 public ModelStorageException(String msg)
 {
  super(msg);
 }

}
