package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public enum HistoryFields
{
 DSC_MOD("Description Modified"),
 DATA_MOD("Data Modified"),
 MTIME("Modified"),
 MDFR("Modified by"),
 COMM("Description");
 
 HistoryFields(String s)
 {
  title=s;
 }
 
 private String title;
 
 String title()
 {
  return title;
 }
 
 static DataSource createHistoryDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceDateField(HistoryFields.MTIME.name(), HistoryFields.MTIME.title()));
  ds.addField(new DataSourceTextField(HistoryFields.MDFR.name(), HistoryFields.MDFR.title()));
  ds.addField(new DataSourceTextField(HistoryFields.COMM.name(), HistoryFields.COMM.title()));

  
  return ds;
 }

 public static DataSource createSubmissionDiffDataSource()
 {
  // TODO Auto-generated method stub
  throw new dev.NotImplementedYetException();
  //return null;
 }
 

};
