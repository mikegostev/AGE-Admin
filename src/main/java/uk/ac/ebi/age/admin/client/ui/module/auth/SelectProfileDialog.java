package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class SelectProfileDialog extends Window
{
 private DataSource ds;
 
 private final CloseClickHandler clsHnd = new CloseClickHandler()
 {
  @Override
  public void onCloseClick(CloseClickEvent event)
  {
   close();
  }
 };

 
 public SelectProfileDialog( final RecordSelectedListener selLstnr )
 {
  setTitle("Select Profile");
  setShowMinimizeButton(false);
  setIsModal(true);
  setShowModalMask(true);
//  setAutoSize(true);
  setAutoCenter(true);
  setHeight("90%");
  setWidth("70%");
  
  
  VLayout panel = new VLayout();
  
  ds = DataSource.getDataSource(Constants.profileListServiceName);
  
  if( ds == null )
  {
   ds = ProfileDSDef.getInstance().createDataSource();

   ds.setID(Constants.groupListServiceName);
   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
   ds.setDefaultParams(new HashMap<String, String>()
     {{
      put(Constants.sessionKey,Session.getSessionId());
     }});
  }
  
  final ListGrid list = new ListGrid();
  list.setSelectionType(SelectionStyle.SINGLE);
  
  ListGridField icnField = new ListGridField("grpIcon", "");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);
  icnField.setType(ListGridFieldType.ICON);
  icnField.setIcon("icons/auth/profile.png");

  ListGridField idField = new ListGridField(ProfileDSDef.profIdField.getFieldId(), ProfileDSDef.profIdField.getFieldTitle());
  idField.setWidth(200);

  ListGridField nameField = new ListGridField(ProfileDSDef.profDescField.getFieldId(), ProfileDSDef.profDescField.getFieldTitle());
  nameField.setCanEdit(false);

  list.setFields(icnField, idField, nameField);

  list.setWidth100();
  list.setHeight100();
  list.setAutoFetchData(true);
  list.setDataSource(ds);

  list.setShowFilterEditor(true);
  list.setFilterOnKeypress(true);

  list.setShowAllRecords(false);
  list.setDrawAheadRatio(1.5F);
  list.setScrollRedrawDelay(0);

  panel.addMember(list);
  
  HLayout buttonPanel = new HLayout();
//  buttonPanel.setWidth100();
  buttonPanel.setMargin(5);
  buttonPanel.setMembersMargin(15);
  buttonPanel.setAlign(Alignment.CENTER);
  
  Button okBt = new Button("OK");
  okBt.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    if( list.getSelection().length != 1 )
     return;
    
    selLstnr.recordSelected( list.getSelectedRecord() );
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
