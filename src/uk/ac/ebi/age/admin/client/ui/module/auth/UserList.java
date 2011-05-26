package uk.ac.ebi.age.admin.client.ui.module.auth;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.auth.UserDSDef;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditFailedEvent;
import com.smartgwt.client.widgets.grid.events.EditFailedHandler;

//criteria={"fieldName":"userid","operator":"notEqual","value":"kk"}
//criteria={"_constructor":"AdvancedCriteria","operator":"or","criteria":[{"fieldName":"username","operator":"iNotStartsWith","value":"V"}]}


public class UserList extends ListGrid
{
 public UserList()
 {
  DataSource ds = UserDSDef.getInstance().createDataSource();
  
 
  ds.setDataFormat(DSDataFormat.JSON);
  ds.setDataURL(Constants.dsServiceUrl);
  ds.setDataProtocol(DSProtocol.POSTPARAMS);
  ds.setDefaultParams(new HashMap<String, String>(){{ put("_$sess",Session.getSessionId());}});
  

  setWidth100();
  setHeight100();
  setAutoFetchData(true);
  setDataSource(ds);
  
  setShowFilterEditor(true);  
  setFilterOnKeypress(true);  
  
  setShowAllRecords(false);
  setDrawAheadRatio(1.5F);
  setScrollRedrawDelay(0);
  
  addEditFailedHandler( new EditFailedHandler()
  {
   
   @Override
   public void onEditFailed(EditFailedEvent event)
   {
    SC.warn( event.getDsResponse().getAttributeAsString("data") );

    discardAllEdits();
   }
  });
  
 }
}
