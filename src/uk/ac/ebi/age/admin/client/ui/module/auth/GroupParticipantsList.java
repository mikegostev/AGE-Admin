package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.GroupDSDef;
import uk.ac.ebi.age.admin.shared.auth.GroupPartsDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class GroupParticipantsList extends VLayout
{
 private DataSource ds;

 public GroupParticipantsList(final String groupId)
 {
  setWidth100();  
  setHeight100();  
  setMargin(5);

  ds = DataSource.getDataSource(Constants.groupPartsListServiceName);
  
  if( ds == null )
  {
   ds = GroupPartsDSDef.getInstance().createDataSource();

   ds.setID(Constants.groupPartsListServiceName);
   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
  }
  
  ds.setDefaultParams(new HashMap<String, String>()
    {{
     put(Constants.sessionKey,Session.getSessionId());
     put(Constants.groupIdParam, groupId);
    }});

  ToolStrip usrTools = new ToolStrip();
  usrTools.setWidth100();

  final ListGrid list = new ListGrid();
  list.setCanAcceptDroppedRecords(true);
  
  list.addRecordDropHandler(new RecordDropHandler()
  {
   @Override
   public void onRecordDrop(RecordDropEvent event)
   {
    System.out.println(event.getSourceWidget().getClass().getName()+" | "+event.getAssociatedType());
    
    for( Record r : event.getDropRecords() )
     if( r.getAttributeAsString(GroupDSDef.grpIdField.getFieldId()) == null )
      event.cancel();
    
   }
  });
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Participants of group '"+groupId+"'");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/group.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  usrTools.addButton(hdr);

  ToolStripButton addUBut = new ToolStripButton();
  addUBut.setTitle("Add user");
  addUBut.setSelected(true);
  addUBut.setIcon( "icons/auth/user_add.png" );
  addUBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new SelectUserDialog( new RecordSelectedListener()
    {
     @Override
     public void recordSelected(Record r)
     {
      list.addData(r);
     }
    }).show();
   }
  });
  
  usrTools.addSpacer(20);
  usrTools.addButton(addUBut);

  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add group");
  addBut.setSelected(true);
  addBut.setIcon( "icons/auth/group_add.png" );
  addBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new SelectGroupDialog( new RecordSelectedListener()
    {
     @Override
     public void recordSelected(Record r)
     {
      list.addData(r);
     }
    }).show();
   }
  });
  
  usrTools.addSpacer(5);
  usrTools.addButton(addBut);

 
  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete participant");
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
  
  
  ListGridField icnField = new ListGridField(GroupPartsDSDef.partTypeField.getFieldId(),"U/G");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);  
  icnField.setType(ListGridFieldType.IMAGE);
  icnField.setImageURLPrefix("icons/auth/");
  icnField.setImageURLSuffix(".png");
  
  ListGridField idField = new ListGridField( GroupPartsDSDef.partIdField.getFieldId(), GroupPartsDSDef.partIdField.getFieldTitle());
  idField.setWidth(200);

  ListGridField nameField = new ListGridField( GroupPartsDSDef.partDescField.getFieldId(), GroupPartsDSDef.partDescField.getFieldTitle());
  
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
  
  
  addMember( list );
  
 }

}
