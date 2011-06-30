package uk.ac.ebi.age.admin.client.ui.module.auth;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class TagPermissionsPanel extends HLayout
{
 private DataSource grpDs;
 
 public TagPermissionsPanel()
 {

  setMembersMargin(3);
  
  VLayout ugPanel = new VLayout();
  
  ugPanel.setHeight100();
  ugPanel.setWidth("30%");
  
  VLayout userPanel = new VLayout();
  

  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final UserList usrList = new UserList();
  usrList.setWidth100();
  usrList.setHeight100();
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Users");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/user.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  usrTools.addButton(hdr);

  userPanel.addMember(usrTools);

  
  userPanel.addMember( usrList );
  
  ugPanel.addMember(userPanel);
  
  
  VLayout groupPanel = new VLayout();
  groupPanel.setWidth100();
  groupPanel.setHeight("50%");
  
 
  ToolStrip grpTools = new ToolStrip();
  grpTools.setWidth100();

  final ListGrid grpList = new GroupList();
  
  hdr = new ToolStripButton();
  hdr.setTitle("Groups");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/group.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  grpTools.addButton(hdr);

  groupPanel.addMember(grpTools);
  groupPanel.addMember(grpList);

  grpList.addSelectionChangedHandler(new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    if( event.getState() )
     usrList.deselectAllRecords();
   }
  });

  usrList.addSelectionChangedHandler(new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    if( event.getState() )
     grpList.deselectAllRecords();
   }
  });

  ugPanel.addMember(groupPanel);
  
  addMember(ugPanel);
  
  VLayout permProfPanel = new VLayout();
  permProfPanel.setHeight100();
  permProfPanel.setWidth("30%");
  
  VLayout permPanel = new VLayout();
  permPanel.setWidth100();
  permPanel.setHeight("50%");
  
  ToolStrip permTools = new ToolStrip();
  grpTools.setWidth100();

  final ListGrid permList = new PermissionList();
  
  hdr = new ToolStripButton();
  hdr.setTitle("Permission");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/permission.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  permTools.addButton(hdr);

  permPanel.addMember(permTools);
  permPanel.addMember(permList);
  
  permProfPanel.addMember(permPanel);
  
  VLayout profPanel = new VLayout();
  profPanel.setWidth100();
  profPanel.setHeight("50%");
  
  ToolStrip profTools = new ToolStrip();
  grpTools.setWidth100();

  final ListGrid profList = new ProfileList();
  
  hdr = new ToolStripButton();
  hdr.setTitle("Profile");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/profile.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  profTools.addButton(hdr);

  profPanel.addMember(profTools);
  profPanel.addMember(profList);
  
  permProfPanel.addMember(profPanel);

  addMember(permProfPanel);
  
  permList.addSelectionChangedHandler(new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    if( event.getState() )
     profList.deselectAllRecords();
   }
  });

  
  profList.addSelectionChangedHandler(new SelectionChangedHandler()
  {
   @Override
   public void onSelectionChanged(SelectionEvent event)
   {
    if( event.getState() )
     permList.deselectAllRecords();
   }
  });

 }
}
