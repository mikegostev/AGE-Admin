package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.HashMap;

import uk.ac.ebi.age.admin.client.Session;
import uk.ac.ebi.age.admin.shared.Constants;
import uk.ac.ebi.age.admin.shared.ontology.OntologyDSDef;

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

public class OntologyList extends ListGrid
{
 private DataSource ds;
 
 @SuppressWarnings("serial")
 public OntologyList()
 {
  ds = DataSource.getDataSource(Constants.ontologyListServiceName);
  
  if( ds == null )
  {
   ds = OntologyDSDef.getInstance().createDataSource();

   ds.setID(Constants.ontologyListServiceName);
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

  
  ListGridField icnField = new ListGridField("ontoIcon","");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);  
  icnField.setType(ListGridFieldType.ICON);  
  icnField.setIcon("icons/ontology/ontology.png");
  
  ListGridField idField = new ListGridField( OntologyDSDef.ontologyNameField.getFieldId(), OntologyDSDef.ontologyNameField.getFieldTitle());
  idField.setWidth(200);
  idField.setCanEdit(false);
  
  ListGridField descField = new ListGridField( OntologyDSDef.ontologyDescriptionField.getFieldId(), OntologyDSDef.ontologyDescriptionField.getFieldTitle());

  ListGridField urlField = new ListGridField( OntologyDSDef.ontologyURLField.getFieldId(), OntologyDSDef.ontologyURLField.getFieldTitle());
  
  list.setFields(icnField,idField,descField,urlField);
  
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
