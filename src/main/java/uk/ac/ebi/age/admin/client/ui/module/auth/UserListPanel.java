package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.shared.auth.UserDSDef;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

//criteria={"fieldName":"userid","operator":"notEqual","value":"kk"}
//criteria={"_constructor":"AdvancedCriteria","operator":"or","criteria":[{"fieldName":"username","operator":"iNotStartsWith","value":"V"}]}


public class UserListPanel extends VLayout
{
 private Layout detailsPanel;
 
 public UserListPanel(Layout detp)
 {
  detailsPanel = detp;
  
  setWidth100();  
  setHeight100();  
  setMargin(5);

  
  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final UserList list = new UserList();

  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Users");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/user.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  usrTools.addButton(hdr);


  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add user");
  addBut.setSelected(true);
  addBut.setIcon( "icons/auth/user_add.png" );
  addBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new UserAddDialog(list.getDataSource()).show();
   }
  });
  
  usrTools.addSpacer(20);
  usrTools.addButton(addBut);

  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete user");
  delBut.setSelected(true);
  delBut.setIcon( "icons/auth/user_delete.png" );
  delBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    list.removeSelectedData();
   }
  });
  
  usrTools.addSpacer(5);
  usrTools.addButton(delBut);

  ToolStripButton passBut = new ToolStripButton();
  passBut.setTitle("Change password");
  passBut.setSelected(true);
  passBut.setIcon( "icons/auth/key.png" );
  passBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    ListGridRecord[] sel = list.getSelectedRecords();
    
    if( sel == null || sel.length != 1 )
     SC.warn("Please select one user");
    else
     new UserChgPassDialog(sel[0],list.getDataSource()).show();
   }
  });
  
  usrTools.addSpacer(5);
  usrTools.addButton(passBut);

  
  addMember(usrTools);
  
  
  list.addEditFailedHandler( new EditFailedHandler()
  {
   @Override
   public void onEditFailed(EditFailedEvent event)
   {
    SC.warn( event.getDsResponse().getAttributeAsString("data") );

    list.discardAllEdits();
   }
  });
  
  
  list.addSelectionChangedHandler(new SelectionChangedHandler()
  {
   
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    clearDetailsPanel();
    
    if( event.getSelection() == null || event.getSelection().length != 1 )
     return;
    
    UserGroupsList ugl = new UserGroupsList( event.getSelectedRecord().getAttribute(UserDSDef.userIdField.getFieldId()) );
    
    detailsPanel.addMember(ugl);
   }
  });
  
  addMember( list );
 }
 
 private void clearDetailsPanel()
 {
  Canvas[] membs = detailsPanel.getMembers();
  
  detailsPanel.removeMembers(membs);
  
  for(Canvas c : membs )
   c.destroy();
 }
}
