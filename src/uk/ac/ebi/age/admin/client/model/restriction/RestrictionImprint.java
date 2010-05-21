package uk.ac.ebi.age.admin.client.model.restriction;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RestrictionImprint implements IsSerializable
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
 

 public Type getType()
 {
  return type;
 }

 public void setType(Type type)
 {
  this.type = type;
 }

}
