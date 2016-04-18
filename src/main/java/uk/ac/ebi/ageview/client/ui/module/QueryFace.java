package uk.ac.ebi.ageview.client.ui.module;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.widgets.layout.VLayout;

public class QueryFace extends VLayout
{
 private ResultPane resultPane;
 private QueryPanel queryPanel;
 
 public QueryFace()
 {
  setWidth100();
  setHeight100();

  resultPane = new ResultPane();

  queryPanel = new QueryPanel(resultPane);

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
    
//    QueryService.Util.getInstance().getAllGroups(0, ResultPane.MAX_GROUPS_PER_PAGE, new AsyncCallback<Report>()
//    {
//
//     @Override
//     public void onFailure(Throwable arg0)
//     {
//      arg0.printStackTrace();
//      SC.say("Query error: " + arg0.getMessage());
//     }
//
//     @Override
//     public void onSuccess(Report resLst)
//     {
//      resultPane.showResult(resLst, null, false, false, false, false,1);
//     }
//    });
   }
  });

 }
}
