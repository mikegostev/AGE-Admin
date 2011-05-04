package uk.ac.ebi.age.admin.client.ui.module.submission;

import uk.ac.ebi.age.ext.submission.HistoryEntry;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;

public class HistoryDetailsPanel extends VLayout
{

 public HistoryDetailsPanel(HistoryEntry hEnt)
 {
  setMembersMargin(8);
  setPadding(8);
  
  DataSource ds = HistoryFields.createSubmissionDiffDataSource();
  
  ds.setClientOnly(true);

//  ds.addData( record );
  
  DetailViewer dv = new DetailViewer();
  dv.setWidth("90%");
  dv.setDataSource(ds);
  dv.setStyleName("groupDetails");
  
  dv.setAutoFetchData(true);

  addMember(dv);
 }

}
