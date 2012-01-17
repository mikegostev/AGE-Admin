package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.ext.authz.SystemAction;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SelectPermissionDialog extends Window
{
 private DataSource ds;
 
 private CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClientEvent event)
  {
   close();
  }
 };

 
 public SelectPermissionDialog( final PermissionSelectedListener selLstnr )
 {
  setTitle("Select Permission");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
  setAutoCenter(true);
  setHeight("90%");
  setWidth("70%");
  
  
  VLayout panel = new VLayout();
  
 
  final ListGrid list = new PermissionList();
  
  panel.addMember(list);
  
  HLayout buttonPanel = new HLayout();
  buttonPanel.setMargin(5);
  buttonPanel.setMembersMargin(15);
  buttonPanel.setAlign(Alignment.CENTER);
  
  Button okBt = new Button("Allow");
  okBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    if( list.getSelection().length != 1 )
     return;
    
    selLstnr.permissionSelected( SystemAction.valueOf(list.getSelectedRecord().getAttribute("pname")), true );
    close();
   }
  });
  
  buttonPanel.addMember( okBt );
  
  okBt = new Button("Deny");
  okBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    if( list.getSelection().length != 1 )
     return;
    
    selLstnr.permissionSelected( SystemAction.valueOf(list.getSelectedRecord().getAttribute("pname")), false );
    close();
   }
  });
  
  buttonPanel.addMember( okBt );
  
  Button cancelBt = new Button("Cancel");
  cancelBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    close();
   }
  });
  
  buttonPanel.addMember( cancelBt );

  panel.addMember(buttonPanel);
    
  addItem(panel);
  
  addCloseClickHandler( clsHnd );

 }
 
 public void close()
 {
  destroy();
 }
}
