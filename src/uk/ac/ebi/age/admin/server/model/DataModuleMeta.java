package uk.ac.ebi.age.admin.server.model;

import java.io.Serializable;

public class DataModuleMeta implements Serializable
{
 private static final long serialVersionUID = 1L;

 private String id;
 private String description;

 public void setId(String stringId)
 {
  id = stringId;
 }

 public String getDescription()
 {
  return description;
 }

 public void setDescription(String description)
 {
  this.description = description;
 }

 public String getId()
 {
  return id;
 }

}
