package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OntologyImprint implements Serializable, IsSerializable
{
 private static final long serialVersionUID = 1L;

 private String name;
 private String description;
 private String locationURL;

 public String getName()
 {
  return name;
 }

 public void setName(String name)
 {
  this.name = name;
 }

 public String getDescription()
 {
  return description;
 }

 public void setDescription(String description)
 {
  this.description = description;
 }

 public String getLocationURL()
 {
  return locationURL;
 }

 public void setLocationURL(String locationURL)
 {
  this.locationURL = locationURL;
 }
}
