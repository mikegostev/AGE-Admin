package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
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


public class UserList extends VLayout
{
 private Layout detailsPanel;
 
 public UserList(Layout detp)
 {
  detailsPanel = detp;
  
  setWidth100();  
  setHeight100();  
  setMargin(5);

  final DataSource ds = UserDSDef.getInstance().createDataSource();
  
  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final ListGrid list = new ListGrid();
  list.setCanDragRecordsOut(true);
  list.setDragDataAction(DragDataAction.COPY);

  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Users");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/user.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  usrTools.addButton(hdr);

//  StaticTextItem hdr = new StaticTextItem("hdr", Canvas.imgHTML("icons/auth/user.png") + " Users" );
//  usrTools.addFormItem(hdr);

  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add user");
  addBut.setSelected(true);
  addBut.setIcon( "icons/auth/user_add.png" );
  addBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new UserAddDialog(ds).show();
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
    ListGridRecord[] sel = list.getSelection();
    
    if( sel == null || sel.length != 1 )
     SC.warn("Please select one user");
    else
     new UserChgPassDialog(sel[0],ds).show();
   }
  });
  
  usrTools.addSpacer(5);
  usrTools.addButton(passBut);

  
  addMember(usrTools);
  
  
  ds.setDataFormat(DSDataFormat.JSON);
  ds.setDataURL(Constants.dsServiceUrl);
  ds.setDataProtocol(DSProtocol.POSTPARAMS);
  ds.setDefaultParams(new HashMap<String, String>(){{ put(Constants.sessionKey,Session.getSessionId());}});
  
  
  ListGridField icnField = new ListGridField("userIcon","");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);  
  icnField.setType(ListGridFieldType.ICON);  
  icnField.setIcon("icons/auth/user.png");
  
  ListGridField idField = new ListGridField( UserDSDef.userIdField.getFieldId(), UserDSDef.userIdField.getFieldTitle());
  idField.setWidth(200);

  ListGridField nameField = new ListGridField( UserDSDef.userNameField.getFieldId(), UserDSDef.userNameField.getFieldTitle());
  
  list.setFields(icnField,idField,nameField);
  
  list.setWidth100();
  list.setHeight100();
  list.setAutoFetchData(true);
  list.setDataSource(ds);
  
  list.setShowFilterEditor(true);  
  list.setFilterOnKeypress(true);  
  
  list.setShowAllRecords(false);
  list.setDrawAheadRatio(1.5F);
  list.setScrollRedrawDelay(0);
  
  list.addEditFailedHandler( new EditFailedHandler()
  {
   @Override
   public void onEditFailed(EditFailedEvent event)
   {
    SC.warn( event.getDsResponse().getAttributeAsString("data") );

    list.discardAllEdits();
   }
  });
  
  addMember( list );
  
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
  
 }
 
 private void clearDetailsPanel()
 {
  Canvas[] membs = detailsPanel.getMembers();
  
  detailsPanel.removeMembers(membs);
  
  for(Canvas c : membs )
   c.destroy();
 }
}
