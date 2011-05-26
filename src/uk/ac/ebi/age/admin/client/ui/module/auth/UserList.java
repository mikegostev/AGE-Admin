package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;

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

//criteria={"fieldName":"userid","operator":"notEqual","value":"kk"}
//criteria={"_constructor":"AdvancedCriteria","operator":"or","criteria":[{"fieldName":"username","operator":"iNotStartsWith","value":"V"}]}


public class UserList extends VLayout
{
 public UserList()
 {
  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final ListGrid list = new ListGrid();


  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add user");
  addBut.setSelected(true);
  addBut.setIcon( "icons/auth/user_add.png" );
  addBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
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
   }
  });
  
  usrTools.addSpacer(5);
  usrTools.addButton(passBut);

  
  addMember(usrTools);
  
  
  DataSource ds = UserDSDef.getInstance().createDataSource();
  
 
  ds.setDataFormat(DSDataFormat.JSON);
  ds.setDataURL(Constants.dsServiceUrl);
  ds.setDataProtocol(DSProtocol.POSTPARAMS);
  ds.setDefaultParams(new HashMap<String, String>(){{ put("_$sess",Session.getSessionId());}});
  
  
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
  
 }
}
