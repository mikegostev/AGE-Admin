package uk.ac.ebi.age.admin.client.model.restriction;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RestrictionImprint implements IsSerializable, Serializable
{
 public enum Type
 {
  IS,
  SOME,
  ONLY,
  MIN,
  MAX,
  EXACTLY,
  OR,
  AND,
  NOT
 }
 
 
 
 
 private Type type;
 private boolean isObligatory;
 

 public Type getType()
 {
  return type;
 }

 public void setType(Type type)
 {
  this.type = type;
 }

 public boolean isObligatory()
 {
  return isObligatory;
 }

 public void setObligatory(boolean isObligatory)
 {
  this.isObligatory = isObligatory;
 }

}
