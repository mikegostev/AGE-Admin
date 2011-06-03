package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.GroupDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class UserGroupsList extends VLayout
{
 private DataSource ds;

 public UserGroupsList(final String userId)
 {
  setWidth100();  
  setHeight100();  
  setMargin(5);

  ds = DataSource.getDataSource(Constants.groupOfUserListServiceName);
  
  if( ds == null )
  {
   ds = GroupDSDef.getInstance().createDataSource();

   ds.setID(Constants.groupOfUserListServiceName);
   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
  }
  
  ds.setDefaultParams(new HashMap<String, String>()
    {{
     put(Constants.sessionKey,Session.getSessionId());
     put(Constants.userIdParam, userId);
    }});

  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final ListGrid list = new ListGrid();

  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Groups of user '"+userId+"'");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/group.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  usrTools.addButton(hdr);

  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add group");
  addBut.setSelected(true);
  addBut.setIcon( "icons/auth/group_add.png" );
  addBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new SelectGroupDialog().show();
   }
  });
  
  usrTools.addSpacer(20);
  usrTools.addButton(addBut);

  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete group");
  delBut.setSelected(true);
  delBut.setIcon( "icons/auth/group_delete.png" );
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

 
  addMember(usrTools);
  
  
  ListGridField icnField = new ListGridField("groupIcon","");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);  
  icnField.setType(ListGridFieldType.ICON);  
  icnField.setIcon("icons/auth/group.png");
  
  ListGridField idField = new ListGridField( GroupDSDef.grpIdField.getFieldId(), GroupDSDef.grpIdField.getFieldTitle());
  idField.setWidth(200);

  ListGridField nameField = new ListGridField( GroupDSDef.grpDescField.getFieldId(), GroupDSDef.grpDescField.getFieldTitle());
  
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
  
 }

}
