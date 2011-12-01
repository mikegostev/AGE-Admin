package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
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


public class ProfileListPanel extends VLayout
{
 private Layout detailsPanel;
 
 public ProfileListPanel(Layout detp)
 {
  setWidth100();
  setHeight100();
  setMargin(5);

  detailsPanel = detp;
  
 
  ToolStrip profTools = new ToolStrip();
  profTools.setWidth100();

  final ListGrid list = new ProfileList();
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Profiles");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/profile.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  profTools.addButton(hdr);

  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add profile");
  addBut.setSelected(true);
  addBut.setIcon("icons/auth/profile_add.png");
  addBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new ProfileAddDialog(list.getDataSource()).show();
   }
  });

  profTools.addSpacer(20);
  profTools.addButton(addBut);

  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete Profile");
  delBut.setSelected(true);
  delBut.setIcon("icons/auth/profile_delete.png");
  delBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    list.removeSelectedData();
   }
  });

  profTools.addSpacer(5);
  profTools.addButton(delBut);

  addMember(profTools);

  list.addEditFailedHandler(new EditFailedHandler()
  {
   @Override
   public void onEditFailed(EditFailedEvent event)
   {
    SC.warn(event.getDsResponse().getAttributeAsString("data"));

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
    
    ProfilePermissionsList gpl = new ProfilePermissionsList( event.getSelectedRecord().getAttribute(ProfileDSDef.profIdField.getFieldId()) );
    
    detailsPanel.addMember(gpl);
   }
  });

  addMember(list);
 }
 
 private void clearDetailsPanel()
 {
  Canvas[] membs = detailsPanel.getMembers();
  
  detailsPanel.removeMembers(membs);
  
  for(Canvas c : membs )
   c.destroy();
 }

}
