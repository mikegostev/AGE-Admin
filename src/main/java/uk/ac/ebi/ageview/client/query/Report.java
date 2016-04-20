package uk.ac.ebi.ageview.client.query;

import java.util.List;

import uk.ac.ebi.age.ui.shared.imprint.ObjectImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Report implements IsSerializable
{
 private List<ObjectImprint> objects;
 private int totalRecords;

 
 public List<ObjectImprint> getObjects()
 {
  return objects;
 }
 
 public void setObjects(List<ObjectImprint> objects)
 {
  this.objects = objects;
 }
 
 public int getTotalObjects()
 {
  return totalRecords;
 }
 
 public void setTotalObjects(int totalRecords)
 {
  this.totalRecords = totalRecords;
 }
 
}
