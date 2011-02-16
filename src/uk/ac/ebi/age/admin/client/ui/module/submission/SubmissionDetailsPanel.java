package uk.ac.ebi.age.admin.client.ui.module.submission;

import java.util.List;

import uk.ac.ebi.age.admin.shared.submission.DataModuleImprint;
import uk.ac.ebi.age.admin.shared.submission.SubmissionImprint;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class SubmissionDetailsPanel extends VLayout
{

 public SubmissionDetailsPanel(ListGridRecord record)
 {
  setMembersMargin(8);
  
  DataSource ds = SubmissionFields.createSubmissionDataSource();
  
  ds.setClientOnly(true);

  ds.addData( record );
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  
  dv.setAutoFetchData(true);

  addMember(dv);
  
  SubmissionImprint simp = (SubmissionImprint)record.getAttributeAsObject("__obj");
  
  List<DataModuleImprint> mods = simp.getModules();
  
  for( DataModuleImprint dmImp : mods )
  {
   DataSource dmds = SubmissionFields.createDataModuleDataSource();
   dmds.setClientOnly(true);
   
   ListGridRecord rec = new ListGridRecord();
   
   rec.setAttribute(SubmissionFields.MOD_ID.name(), dmImp.getId());

   rec.setAttribute(SubmissionFields.COMM.name(), dmImp.getDescription());
   rec.setAttribute(SubmissionFields.MDFR.name(), dmImp.getModifier());
   rec.setAttribute(SubmissionFields.MTIME.name(), dmImp.getMtime());
   rec.setAttribute(SubmissionFields.MOD_FILE.name(), dmImp.getId());
   
   dmds.addData(rec);
   
   dv = new DetailViewer();
   dv.setWidth("90%");
   dv.setDataSource(dmds);
   dv.setStyleName("moduleDetails");
   
   dv.setAutoFetchData(true);

   addMember(dv);

  }
  
//  ListGrid 
 }

}
