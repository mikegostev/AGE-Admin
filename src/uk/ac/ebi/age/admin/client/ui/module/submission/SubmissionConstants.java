package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public enum SubmissionConstants
{
 SUBM_ID("Submission ID"),
 MOD_ID("Module ID"),
 FILE_ID("File ID"),
 SRC_FILE("Source File"),
 CTIME("Created"),
 MTIME("Modified"),
 CRTR("Created by"),
 MDFR("Modified by"),
 COMM("Description"), 
 VIS("Visibilty"),
 STS("Status"),
 RMVD("Removed"),
 ACTV("Active"),
 TAGS("Tags");

 
 SubmissionConstants(String s)
 {
  title=s;
 }
 
 private String title;
 
 String title()
 {
  return title;
 }
 
 static DataSource createSubmissionDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(SubmissionConstants.SUBM_ID.name(), SubmissionConstants.SUBM_ID.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.TAGS.name(), SubmissionConstants.TAGS.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.STS.name(), SubmissionConstants.STS.title()));

  
  return ds;
 }
 
 static DataSource createDataModuleDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(SubmissionConstants.MOD_ID.name(), SubmissionConstants.MOD_ID.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.SRC_FILE.name(), SubmissionConstants.SRC_FILE.title()));
  
  return ds;
 }
 
 static DataSource createAttachmentDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(SubmissionConstants.FILE_ID.name(), SubmissionConstants.FILE_ID.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.COMM.name(), SubmissionConstants.COMM.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.VIS.name(), SubmissionConstants.VIS.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.CRTR.name(), SubmissionConstants.CRTR.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.MDFR.name(), SubmissionConstants.MDFR.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.CTIME.name(), SubmissionConstants.CTIME.title()));
  ds.addField(new DataSourceDateField(SubmissionConstants.MTIME.name(), SubmissionConstants.MTIME.title()));
  ds.addField(new DataSourceTextField(SubmissionConstants.SRC_FILE.name(), SubmissionConstants.SRC_FILE.title()));
  
  return ds;
 }



};
