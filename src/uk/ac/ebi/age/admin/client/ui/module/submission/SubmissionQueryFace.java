package uk.ac.ebi.age.admin.client.ui.module.submission;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.widgets.layout.VLayout;

public class SubmissionQueryFace extends VLayout
{
 private SubmissionsPane resultPane;
 private SubmissionsQueryPanel queryPanel;
 
 public SubmissionQueryFace()
 {
  setWidth100();
  setHeight100();

  resultPane = new SubmissionsPane();

  queryPanel = new SubmissionsQueryPanel(resultPane);

  // qp.setHeight(200);
  queryPanel.setWidth("100%");

  resultPane.setWidth100();
  resultPane.setHeight100();

  addMember(queryPanel);
  addMember(resultPane);

  
  Scheduler.get().scheduleDeferred(new ScheduledCommand()
  {
   @Override
   public void execute()
   {
    queryPanel.executeQuery();
   }
  });

 }
}
