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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;

public class UserList extends ListGrid
{
 private DataSource ds;
 
 @SuppressWarnings("serial")
 public UserList()
 {
  ds = DataSource.getDataSource(Constants.userListServiceName);
  
  if( ds == null )
  {
   ds = UserDSDef.getInstance().createDataSource();

   ds.setID(Constants.userListServiceName);
   ds.setDataFormat(DSDataFormat.JSON);
   ds.setDataURL(Constants.dsServiceUrl);
   ds.setDataProtocol(DSProtocol.POSTPARAMS);
   ds.setDefaultParams(new HashMap<String, String>()
     {{
      put(Constants.sessionKey,Session.getSessionId());
     }});
  }
  
  final ListGrid list = this;
  list.setCanDragRecordsOut(true);
  list.setDragDataAction(DragDataAction.COPY);

  
  ListGridField icnField = new ListGridField("userIcon","");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);  
  icnField.setType(ListGridFieldType.ICON);  
  icnField.setIcon("icons/auth/user.png");
  
  ListGridField idField = new ListGridField( UserDSDef.userIdField.getFieldId(), UserDSDef.userIdField.getFieldTitle());
  idField.setWidth(200);
  idField.setCanEdit(false);
  
  ListGridField nameField = new ListGridField( UserDSDef.userNameField.getFieldId(), UserDSDef.userNameField.getFieldTitle());

  ListGridField emailField = new ListGridField( UserDSDef.userEmailField.getFieldId(), UserDSDef.userEmailField.getFieldTitle());
  
  list.setFields(icnField,idField,nameField,emailField);
  
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


 }

}
