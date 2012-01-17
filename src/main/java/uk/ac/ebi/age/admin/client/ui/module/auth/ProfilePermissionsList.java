package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;
import uk.ac.ebi.age.admin.shared.auth.ProfilePermDSDef;
import uk.ac.ebi.age.ext.authz.SystemAction;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ProfilePermissionsList extends VLayout
{
 private DataSource ds;

 public ProfilePermissionsList(final String profId)
 {
  setWidth100();  
  setHeight100();  
  setMargin(5);

//  DataSource.
  
  ds = DataSource.getDataSource(Constants.profilePermissionsListServiceName);
  
  if( ds != null )
  {
   ds.destroy();
   ds = null;
  }
  
  if( ds == null )
  {
   ds = ProfilePermDSDef.getInstance().createDataSource();

   ds.setID(Constants.profilePermissionsListServiceName);
   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
  }
  
  ds.setDefaultParams(new HashMap<String, String>()
    {{
     put(Constants.sessionKey,Session.getSessionId());
     put(Constants.profileIdParam, profId);
    }});
  
  

  ToolStrip permTools = new ToolStrip();
  permTools.setWidth100();

  final ListGrid list = new ListGrid();

//  list.setCanAcceptDroppedRecords(true);
//  
//  list.addRecordDropHandler(new RecordDropHandler()
//  {
//   @Override
//   public void onRecordDrop(RecordDropEvent event)
//   {
//    System.out.println(event.getSourceWidget().getClass().getName()+" | "+event.getAssociatedType());
//    
//    for( Record r : event.getDropRecords() )
//    {
//     if( r.getAttributeAsString(UserDSDef.userIdField.getFieldId()) == null )
//     {
//      event.cancel();
//      return;
//     }
//     
//     Record nr = new ListGridRecord();
//     
//     nr.setAttribute(GroupPartsDSDef.partIdField.getFieldId(), r.getAttribute(UserDSDef.userIdField.getFieldId()) );
//     nr.setAttribute(GroupPartsDSDef.partDescField.getFieldId(), r.getAttribute(UserDSDef.userNameField.getFieldId()) );
//     nr.setAttribute(GroupPartsDSDef.partTypeField.getFieldId(), "user" );
//     
//     list.addData(nr);
//
//    }
//    
//    event.cancel();
//   }
//  });
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Permissions of profile '"+profId+"'");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/profile.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  permTools.addButton(hdr);

  ToolStripButton addUBut = new ToolStripButton();
  addUBut.setTitle("Add permission");
  addUBut.setSelected(true);
  addUBut.setIcon( "icons/auth/permission_add.png" );
  addUBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new SelectPermissionDialog( new PermissionSelectedListener()
    {
     
     public void permissionSelected( SystemAction act, boolean allow )
     {
      Record nr = new ListGridRecord();
      
      String typ = allow?"allow":"deny";
      
      nr.setAttribute(ProfilePermDSDef.keyField.getFieldId(), act.name()+typ );
      nr.setAttribute(ProfilePermDSDef.idField.getFieldId(), act.name() );
      nr.setAttribute(ProfilePermDSDef.descField.getFieldId(), act.getDescription() );
      nr.setAttribute(ProfilePermDSDef.typeField.getFieldId(), typ );
      
      ds.addData(nr);
     }

    }).show();
   }
  });
  
  permTools.addSpacer(20);
  permTools.addButton(addUBut);

  
  ToolStripButton addProfBut = new ToolStripButton();
  addProfBut.setTitle("Add profile");
  addProfBut.setSelected(true);
  addProfBut.setIcon( "icons/auth/profile_add.png" );
  addProfBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new SelectProfileDialog( new RecordSelectedListener()
    {
     
     public void recordSelected( Record r )
     {
      Record nr = new ListGridRecord();
      
      String prof = r.getAttribute(ProfileDSDef.profIdField.getFieldId());
      
      nr.setAttribute(ProfilePermDSDef.keyField.getFieldId(), prof+"prof" );
      nr.setAttribute(ProfilePermDSDef.idField.getFieldId(), prof );
      nr.setAttribute(ProfilePermDSDef.descField.getFieldId(), r.getAttribute(ProfileDSDef.profDescField.getFieldId()) );
      nr.setAttribute(ProfilePermDSDef.typeField.getFieldId(), "profile" );
      
      ds.addData(nr);
     }

    }).show();
   }
  });
  
  permTools.addSpacer(5);
  permTools.addButton(addProfBut);

 
  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete");
  delBut.setSelected(true);
  delBut.setIcon( "icons/auth/permission_delete.png" );
  delBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    list.removeSelectedData();
   }
  });
  
  permTools.addSpacer(5);
  permTools.addButton(delBut);

 
  addMember(permTools);
  
  
  ListGridField typeField = new ListGridField( ProfilePermDSDef.typeField.getFieldId(), "Typ" );
  typeField.setWidth(30);
  typeField.setAlign(Alignment.CENTER);  
  typeField.setType(ListGridFieldType.IMAGE);
  typeField.setImageURLPrefix("icons/auth/");
  typeField.setImageURLSuffix(".png");

  ListGridField idField = new ListGridField( ProfilePermDSDef.idField.getFieldId(), ProfilePermDSDef.idField.getFieldTitle() );
  idField.setWidth(200);

  ListGridField nameField = new ListGridField( ProfilePermDSDef.descField.getFieldId(), ProfilePermDSDef.descField.getFieldTitle() );
  
  list.setFields(typeField,idField,nameField);
  
  list.setWidth100();
  list.setHeight100();
  list.setAutoFetchData(true);
  list.setDataSource(ds);
  
  list.setShowFilterEditor(true);  
  list.setFilterOnKeypress(true);  
  
  list.setShowAllRecords(false);
  list.setDrawAheadRatio(1.5F);
  list.setScrollRedrawDelay(0);
  
  list.setGroupByField(ProfilePermDSDef.typeField.getFieldId());
  list.setGroupStartOpen(GroupStartOpen.ALL);
  
  addMember( list );
  
 }

}
