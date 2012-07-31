package uk.ac.ebi.age.admin.client.ui.module.other;

import uk.ac.ebi.age.admin.client.AgeAdminService;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;


public class OtherControlsPanel extends SectionStack
{
 public OtherControlsPanel()
 {
  SectionStackSection sec = new SectionStackSection("Online mode");
  
  sec.addItem( new OfflinePanel() );
  
  addSection( sec );
 }
 
 
 static class OfflinePanel extends VLayout
 {
  private final  Label lbl = new Label("Status: UNDEFINED");
  private final Button btn = new Button("Set");
  
  private boolean online;
  
  public OfflinePanel()
  {
   
   btn.setDisabled(true);
   
   btn.addClickHandler( new ClickHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     AgeAdminService.Util.getInstance().setOnlineMode(!online, new AsyncCallback<Boolean>()
     {
      @Override
      public void onFailure(Throwable arg0)
      {
       SC.warn("Operation error: "+arg0.getMessage());
      }

      @Override
      public void onSuccess(Boolean arg0)
      {
       online = !online;
       updateStatus();
      }
     });
     
    }
   });
   
   setMembers( lbl, btn );
   
   AgeAdminService.Util.getInstance().isOnlineMode( new AsyncCallback<Boolean>()
   {
    
    @Override
    public void onSuccess(Boolean arg0)
    {
     online = arg0;
     
     updateStatus();    
    }
    
    @Override
    public void onFailure(Throwable arg0)
    {
     SC.warn("Operation error: "+arg0.getMessage());
    }
   });

  }
  
  private void updateStatus()
  {
   if( online )
   {
    lbl.setTitle("Status: <span style='color: green'>ONLINE</span>");
    btn.setTitle("Set OFFLINE");
    btn.setDisabled(false);
   }
   else
   {
    lbl.setTitle("Status: <span style='color: red'>OFFLINE</span>");
    btn.setTitle("Set ONLINE");
    btn.setDisabled(false);
   }
  }
 }
}
