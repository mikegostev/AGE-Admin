package uk.ac.ebi.age.admin.client.ui.module.auth;

import uk.ac.ebi.age.admin.client.ClientConfig;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.grid.ListGrid;

public class UserList extends ListGrid
{
 public UserList()
 {
  DataSource ds = new RestDataSource();
  
  ds.setDataFormat(DSDataFormat.JSON);
//  ds.setDataURL(ClientConfig.dsServiceUrl+"?sess="+Session.getSessionId()+"&"+ClientConfig.dsServiceParam+"="+ClientConfig.userListServiceName);
  ds.setDataURL(ClientConfig.dsServiceUrl);
  ds.setDataProtocol(DSProtocol.POSTPARAMS);

  
  DataSourceField iserIdField = new DataSourceField("userid", FieldType.TEXT, "User ID", 150);
  DataSourceField iserNameField = new DataSourceField("username", FieldType.TEXT, "User Name");
  
  ds.setFields(iserIdField,iserNameField);

  setWidth100();
  setHeight100();
  setAutoFetchData(true);
  setDataSource(ds);
 }
}
