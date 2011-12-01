package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public enum HistoryFields
{
 SUBM_ID("Submission ID"),
 MOD_ID("Module ID"),
 FILE_ID("File ID"),
 SRC_FILE("Source File"),
 COMM("Description"),
 DSC_MOD("Description Modified"),
 DATA_MOD("Data Modified"),
 CTIME("Created at"),
 MTIME("Modified at"),
 CRTR("Created by"),
 MDFR("Modified by"),
 STS("Status"),
 VIS("Visibilty");

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
  
  ds.addField(new DataSourceDateField(MTIME.name(), MTIME.title()));
  ds.addField(new DataSourceTextField(MDFR.name(), MDFR.title()));
  ds.addField(new DataSourceTextField(COMM.name(), COMM.title()));

  
  return ds;
 }

 public static DataSource createSubmissionDiffDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(SUBM_ID.name(), SUBM_ID.title()));
  ds.addField(new DataSourceTextField(COMM.name(), COMM.title()));
  ds.addField(new DataSourceTextField(CRTR.name(), CRTR.title()));
  ds.addField(new DataSourceDateField(CTIME.name(), CTIME.title()));
  ds.addField(new DataSourceTextField(MDFR.name(), MDFR.title()));
  ds.addField(new DataSourceDateField(MTIME.name(), MTIME.title()));
  
  return ds;
 }

 public static DataSource createModuleDiffDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(STS.name(), STS.title()));
  ds.addField(new DataSourceTextField(MOD_ID.name(), MOD_ID.title()));
  ds.addField(new DataSourceTextField(COMM.name(), COMM.title()));
  ds.addField(new DataSourceTextField(CRTR.name(), CRTR.title()));
  ds.addField(new DataSourceDateField(CTIME.name(), CTIME.title()));
  ds.addField(new DataSourceTextField(MDFR.name(), MDFR.title()));
  ds.addField(new DataSourceDateField(MTIME.name(), MTIME.title()));
  ds.addField(new DataSourceTextField(SRC_FILE.name(), SRC_FILE.title()));
  
  return ds;
 }
 
 static DataSource createAttachmentDiffDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(STS.name(), STS.title()));
  ds.addField(new DataSourceTextField(FILE_ID.name(), FILE_ID.title()));
  ds.addField(new DataSourceTextField(COMM.name(), COMM.title()));
  ds.addField(new DataSourceTextField(VIS.name(), VIS.title()));
  ds.addField(new DataSourceTextField(CRTR.name(), CRTR.title()));
  ds.addField(new DataSourceTextField(MDFR.name(), MDFR.title()));
  ds.addField(new DataSourceDateField(CTIME.name(), CTIME.title()));
  ds.addField(new DataSourceDateField(MTIME.name(), MTIME.title()));
  ds.addField(new DataSourceTextField(SRC_FILE.name(), SRC_FILE.title()));
  
  return ds;
 }


 

};
