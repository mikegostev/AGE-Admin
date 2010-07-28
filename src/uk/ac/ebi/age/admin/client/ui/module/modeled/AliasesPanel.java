package uk.ac.ebi.age.admin.client.ui.module.modeled;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class AliasesPanel extends VLayout
{
 private ListGrid lst;
 
 public AliasesPanel()
 {
  super(0);
  
  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();
  

  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/add.png");
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    addAlias();
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/delete.png");
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    removeAlias();
   }
  });
  superTS.addButton(btdel);

  lst = new ListGrid();
  
  ListGridField iconField = new ListGridField("icon", "", 40);
  iconField.setAlign(Alignment.CENTER);
  iconField.setType(ListGridFieldType.IMAGE);
  iconField.setImageURLPrefix("../images/icons/");
  iconField.setImageURLSuffix(".png");

  ListGridField aliasField = new ListGridField("alias", "Alias");

  lst.setFields(iconField,aliasField);
  
  addMember(superTS);
  addMember(lst);
  
 }
 
 private void addAlias()
 {
  SC.askforValue("Please enter new alias", new ValueCallback(){

   @Override
   public void execute(String value)
   {
    lst.addData( new AliasRecord(value) );
   }});
 }
 
 private void removeAlias()
 {
  AliasRecord cr = (AliasRecord)lst.getSelectedRecord();
  
  if( cr == null )
   return;
  
  lst.removeData(cr);
 }

 class AliasRecord extends ListGridRecord
 {
  AliasRecord( String alias )
  {
   super();
   
   
   setAttribute("icon", "alias" );
   setAttribute("alias", alias );
  }
 }
}
