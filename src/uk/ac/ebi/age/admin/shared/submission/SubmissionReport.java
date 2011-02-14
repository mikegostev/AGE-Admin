package uk.ac.ebi.age.admin.shared.submission;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubmissionReport implements IsSerializable
{
 private List<SubmissionImprint> objects;
 private int totalRecords;
 private int totalSamples;
 private int totalMatchedSamples=-1;
 
 public List<SubmissionImprint> getObjects()
 {
  return objects;
 }
 
 public void setObjects(List<SubmissionImprint> objects)
 {
  this.objects = objects;
 }
 
 public int getTotalSubmissions()
 {
  return totalRecords;
 }
 
 public void setTotalSubmissions(int totalRecords)
 {
  this.totalRecords = totalRecords;
 }

 public int getTotalModules()
 {
  return totalSamples;
 }

 public void setTotalModules(int totalSamples)
 {
  this.totalSamples = totalSamples;
 }

 public int getTotalMatchedModules()
 {
  return totalMatchedSamples;
 }

 public void setTotalMatchedModules(int totalMatchedSamples)
 {
  this.totalMatchedSamples = totalMatchedSamples;
 }
}
