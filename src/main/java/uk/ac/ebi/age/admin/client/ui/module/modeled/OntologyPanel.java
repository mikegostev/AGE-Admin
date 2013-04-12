package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OntologyPanel extends HLayout
{
 private final ListGrid ontologyList;


 public OntologyPanel()
 {
  setWidth100();
  setHeight100();
  setMargin(5);
  
  ToolStrip grpTools = new ToolStrip();
  grpTools.setWidth100();
  
  ToolStripButton hdr = new ToolStripButton();
  hdr.setTitle("Ontologies");
  hdr.setSelected(false);
  hdr.setIcon( "icons/ontology/ontology.png" );
  hdr.setShowDisabled(false);
  hdr.setDisabled(true);
  
  grpTools.addButton(hdr);
  
  ToolStripButton addBut = new ToolStripButton();
  addBut.setTitle("Add ontology");
  addBut.setSelected(true);
  addBut.setIcon("icons/ontology/ontology_add.png");
  addBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
   }
  });
  
  grpTools.addSpacer(20);
  grpTools.addButton(addBut);
  
  ToolStripButton delBut = new ToolStripButton();
  delBut.setTitle("Delete ontology");
  delBut.setSelected(true);
  delBut.setIcon("icons/ontology/ontology_delete.png");
  delBut.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    ontologyList.removeSelectedData();
   }
  });
  
  grpTools.addSpacer(5);
  grpTools.addButton(delBut);
  
  ontologyList = new ListGrid();

  ListGridField icnField = new ListGridField("clsIcon", "");
  icnField.setWidth(30);
  icnField.setAlign(Alignment.CENTER);
  icnField.setType(ListGridFieldType.ICON);
  icnField.setIcon("icons/ontology/ontology.png");

  ListGridField nameField = new ListGridField("name", "Name");
  nameField.setWidth(200);

  ListGridField descField = new ListGridField("description", "Description");
  
  ontologyList.setFields(icnField, nameField, descField);

  ontologyList.setWidth100();
  ontologyList.setHeight100();

  ontologyList.setShowAllRecords(true);


  
  addMember(grpTools);
  addMember(ontologyList);

 }
 
 public void setModel(ModelImprint mod)
 {
  // TODO Auto-generated method stub
  
 }

}
