package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.ProfileDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class ProfileList extends ListGrid
{
 public ProfileList()
 {
  
  DataSource ds = DataSource.getDataSource(Constants.profileListServiceName);
  
  if( ds == null )
  {
   ds = ProfileDSDef.getInstance().createDataSource();

   ds.setID(Constants.profileListServiceName);
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
  

  ListGridField icnField = new ListGridField("profIcon", "");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);
  icnField.setType(ListGridFieldType.ICON);
  icnField.setIcon("icons/auth/profile.png");

  ListGridField idField = new ListGridField(ProfileDSDef.profIdField.getFieldId(), ProfileDSDef.profIdField.getFieldTitle());
  idField.setWidth(200);

  ListGridField nameField = new ListGridField(ProfileDSDef.profDescField.getFieldId(), ProfileDSDef.profDescField.getFieldTitle());

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

 }
}
