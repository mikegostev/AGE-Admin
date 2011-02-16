package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public enum SubmissionFields
{
 SUBM_ID("Submission ID"),
 MOD_ID("Module ID"),
 MOD_FILE("Module File"),
 CTIME("Created"),
 MTIME("Modified"),
 CRTR("Created by"),
 MDFR("Modified by"),
 COMM("Description");
 
 SubmissionFields(String s)
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
  
  ds.addField(new DataSourceTextField(SubmissionFields.SUBM_ID.name(), SubmissionFields.SUBM_ID.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.COMM.name(), SubmissionFields.COMM.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.CRTR.name(), SubmissionFields.CRTR.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.MDFR.name(), SubmissionFields.MDFR.title()));
  ds.addField(new DataSourceDateField(SubmissionFields.CTIME.name(), SubmissionFields.CTIME.title()));
  ds.addField(new DataSourceDateField(SubmissionFields.MTIME.name(), SubmissionFields.MTIME.title()));

  
  return ds;
 }
 
 static DataSource createDataModuleDataSource()
 {
  DataSource ds = new DataSource();
  
  ds.addField(new DataSourceTextField(SubmissionFields.MOD_ID.name(), SubmissionFields.MOD_ID.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.COMM.name(), SubmissionFields.COMM.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.MDFR.name(), SubmissionFields.MDFR.title()));
  ds.addField(new DataSourceDateField(SubmissionFields.MTIME.name(), SubmissionFields.MTIME.title()));
  ds.addField(new DataSourceTextField(SubmissionFields.MOD_FILE.name(), SubmissionFields.MOD_FILE.title()));
  
  return ds;
 }

};
