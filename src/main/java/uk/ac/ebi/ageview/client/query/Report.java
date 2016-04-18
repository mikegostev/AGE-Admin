package uk.ac.ebi.ageview.client.query;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Report implements IsSerializable
{
 private List<AttributedImprint> objects;
 private int totalRecords;

 
 public List<AttributedImprint> getObjects()
 {
  return objects;
 }
 
 public void setObjects(List<AttributedImprint> objects)
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
