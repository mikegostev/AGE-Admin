package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.GroupDSDef;
import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;
import uk.ac.ebi.age.admin.shared.cassif.ACLDSDef;

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
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ACLPanel extends HLayout
{
 
 public ACLPanel( final String classfId, final String tgId, String svcName )
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
  permTools.setWidth100();

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
  profTools.setWidth100();

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

  
  VLayout aclPanel = new VLayout(); 
  aclPanel.setHeight100();
  aclPanel.setWidth("30%");
  
  ToolStrip aclTools = new ToolStrip();
  aclTools.setWidth100();

 
  hdr = new ToolStripButton();
  hdr.setTitle("ACL");
  hdr.setSelected(false);
  hdr.setIcon( "icons/auth/permission.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  aclTools.addButton(hdr);

  abstract class AddARCHandler implements ClickHandler
  {
   ListGridRecord makeRecord( String ptype )
   {
    ListGridRecord acrr = new ListGridRecord();

    Record r = usrList.getSelectedRecord();

    if(r != null)
    {
     acrr.setAttribute(ACLDSDef.sTypeField.getFieldId(), "user");
     acrr.setAttribute(ACLDSDef.sIdField.getFieldId(), r.getAttribute(UserDSDef.userIdField.getFieldId()));
    }
    else
    {
     r = grpList.getSelectedRecord();
     if(r == null)
      return null;

     acrr.setAttribute(ACLDSDef.sTypeField.getFieldId(), "group");
     acrr.setAttribute(ACLDSDef.sIdField.getFieldId(), r.getAttribute(GroupDSDef.grpIdField.getFieldId()));
    }

    r = permList.getSelectedRecord();

    if(r != null)
    {
     acrr.setAttribute(ACLDSDef.pTypeField.getFieldId(), ptype);
     acrr.setAttribute(ACLDSDef.pIdField.getFieldId(), r.getAttribute("pname"));
    }
    else
    {
     r = profList.getSelectedRecord();
     if(r == null)
      return null;

     acrr.setAttribute(ACLDSDef.pTypeField.getFieldId(), "profile");
     acrr.setAttribute(ACLDSDef.pIdField.getFieldId(), r.getAttribute(ProfileDSDef.profIdField.getFieldId()));
    }
    
    return acrr;
   }
  }
  
  final ListGrid acl = new ListGrid();
  DataSource aclds = DataSource.getDataSource(svcName);
  
  if( aclds != null )
  {
   aclds.destroy();
   aclds = null;
  }
  
  if( aclds == null )
  {
   aclds = ACLDSDef.getInstance().createDataSource();

   aclds.setID(svcName);
   aclds.setDataFormat(DSDataFormat.JSON);
   aclds.setDataURL(Constants.dsServiceUrl);
   aclds.setDataProtocol(DSProtocol.POSTPARAMS);
  }
  
  aclds.setDefaultParams(new HashMap<String, String>()
    {{
     put(Constants.sessionKey,Session.getSessionId());
     put(Constants.classifIdParam, classfId);
     put(Constants.tagIdParam, tgId);
    }});
  
  
  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Allow");
  addBut.setSelected(true);
  addBut.setIcon("icons/auth/allow.png");
  addBut.addClickHandler(new AddARCHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    ListGridRecord acrr = makeRecord("allow");
    
    if( acrr != null )
     acl.getDataSource().addData(acrr);
   }
  });

  aclTools.addSpacer(20);
  aclTools.addButton(addBut);

  {
   ToolStripButton denyBut = new ToolStripButton();
   denyBut.setTitle("Deny");
   denyBut.setSelected(true);
   denyBut.setIcon("icons/auth/deny.png");
   denyBut.addClickHandler(new AddARCHandler()
   {
    @Override
    public void onClick(ClickEvent event)
    {
     ListGridRecord acrr = makeRecord("deny");

     if(acrr != null)
      acl.getDataSource().addData(acrr);
    }
   });

   aclTools.addSpacer(5);
   aclTools.addButton(denyBut);
  }

  ToolStripButton remBut = new ToolStripButton();
  remBut.setTitle("Remove");
  remBut.setSelected(true);
  remBut.setIcon("icons/auth/permission_delete.png");
  remBut.addClickHandler(new AddARCHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    acl.removeSelectedData();
   }
  });

  aclTools.addSpacer(5);
  aclTools.addButton(remBut);
  
  
  aclPanel.addMember(aclTools);

  ListGridField idField = new ListGridField( ACLDSDef.keyField.getFieldId(), "ID" );
  idField.setWidth(30);

  ListGridField sTypeField = new ListGridField( ACLDSDef.sTypeField.getFieldId(), "U/G" );
  sTypeField.setWidth(30);
  sTypeField.setAlign(Alignment.CENTER);  
  sTypeField.setType(ListGridFieldType.IMAGE);
  sTypeField.setImageURLPrefix("icons/auth/");
  sTypeField.setImageURLSuffix(".png");

  ListGridField sidField = new ListGridField( ACLDSDef.sIdField.getFieldId(), ACLDSDef.sIdField.getFieldTitle() );
  sidField.setWidth(200);

  ListGridField pTypeField = new ListGridField( ACLDSDef.pTypeField.getFieldId(), "A/D" );
  pTypeField.setWidth(30);
  pTypeField.setAlign(Alignment.CENTER);  
  pTypeField.setType(ListGridFieldType.IMAGE);
  pTypeField.setImageURLPrefix("icons/auth/");
  pTypeField.setImageURLSuffix(".png");

  ListGridField permField = new ListGridField( ACLDSDef.pIdField.getFieldId(), ACLDSDef.pIdField.getFieldTitle() );
  
  
  acl.setFields(idField, sTypeField,sidField,pTypeField,permField);
  
  acl.setWidth100();
  acl.setHeight100();
  acl.setAutoFetchData(true);
 
  
  acl.setDataSource(aclds);
  
  aclPanel.addMember(acl);
  
  addMember(aclPanel);
 }
}
