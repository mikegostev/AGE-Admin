package uk.ac.ebi.age.admin.client.common;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelPath implements IsSerializable
{
 private boolean publ;
 private String modelName;
 private List<String> pathEls;
 
 public boolean isPublic()
 {
  return publ;
 }
 
 public void setPublic(boolean publ)
 {
  this.publ = publ;
 }
 
 public String getModelName()
 {
  return modelName;
 }
 
 public void setModelName(String modelName)
 {
  this.modelName = modelName;
 }
 
 public List<String> getPathElements()
 {
  return pathEls;
 }
 
 public void setPathElements(List<String> pathEls)
 {
  this.pathEls = pathEls;
 }
 
}
