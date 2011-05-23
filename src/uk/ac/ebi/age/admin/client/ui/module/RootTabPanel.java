package uk.ac.ebi.age.admin.client.ui.module;

import uk.ac.ebi.age.admin.client.ui.PlacingManager;
import uk.ac.ebi.age.admin.client.ui.module.auth.AuthAdminPanel;
import uk.ac.ebi.age.admin.client.ui.module.modeled.ModelPanel;
import uk.ac.ebi.age.admin.client.ui.module.submission.SubmissionPreparePanelGWT;
import uk.ac.ebi.age.admin.client.ui.module.submission.SubmissionQueryFace;

import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class RootTabPanel extends TabSet
{
 public RootTabPanel()
 {
  setTabBarPosition(Side.TOP);  
  setWidth100();  
  setHeight100();  

  
  Tab submTab = new Tab("Login");
  submTab.setPane( new TestEntryPanel() );
  
  addTab(submTab);

  Tab modelTab = new Tab("Model");
  modelTab.setPane( new ModelPanel() );
  
  addTab(modelTab);

  Tab submitTab = new Tab("Submit");
  submitTab.setPane( new SubmissionPreparePanelGWT() );
  
  addTab(submitTab);

  Tab submittionListTab = new Tab("Submissions");
  submittionListTab.setPane( new SubmissionQueryFace() );
  
  addTab(submittionListTab);

  Tab secTab = new Tab("Security");
  secTab.setPane( new AuthAdminPanel() );
  
  addTab(secTab);

  
  PlacingManager.setManager( new PlacingManager() {
   
   protected void _placeWidget(Canvas pnl, String title)
   {
    Tab pTab = new Tab(title);
    pTab.setPane( pnl );
    pTab.setCanClose(true);
    
    addTab(pTab);
    selectTab(pTab);
   }

  });
  
 }
}
