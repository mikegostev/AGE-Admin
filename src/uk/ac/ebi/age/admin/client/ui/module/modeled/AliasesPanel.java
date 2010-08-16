package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.ui.MetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Label;
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
 private AgeAbstractClassImprint classImp;
 
 public AliasesPanel(AgeAbstractClassImprint cls)
 {
  super(0);
  
  setWidth(200);
  
  classImp = cls;
  
  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();
  
  Label lbl = new Label("Aliases");
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();
  
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
  lst.setShowHeader(false);
  lst.setWidth100();
  lst.setHeight(30);
  lst.setBodyOverflow(Overflow.VISIBLE);
  lst.setOverflow(Overflow.VISIBLE);

  ListGridField iconField = new ListGridField("icon", "", 40);
  iconField.setAlign(Alignment.CENTER);
  iconField.setType(ListGridFieldType.IMAGE);
  iconField.setImageURLPrefix("../images/icons/");
  iconField.setImageURLSuffix(".png");

  ListGridField aliasField = new ListGridField("alias", "Alias");

  lst.setFields(iconField,aliasField);
  
  addMember(superTS);
  addMember(lst);
  
  if( cls.getAliases() != null )
  {
   for(String s : cls.getAliases() )
    lst.addData( new AliasRecord(s) );
  }
 }
 
 private void addAlias()
 {
  SC.askforValue("Please enter new alias", new ValueCallback(){

   @Override
   public void execute(String value)
   {
    if( value == null || (value=value.trim()).length() == 0 )
     return;
    
    MetaClassDef meta = MetaClassDef.getMetaClass(classImp);
    
    Collection<? extends AgeAbstractClassImprint> clsz = meta.getXClasses( classImp.getModel() );
    
    for( AgeAbstractClassImprint acls : clsz )
    {
     if( acls.getName().equals(value) )
     {
      SC.warn("Class with name '"+value+"' already exists");
      return;
     }
     
     if( acls.getAliases() != null )
     {
      for( String ali : acls.getAliases() )
      {
       if( ali.equals(value) )
       {
        SC.warn("Name '"+value+"' is already taken by class '"+acls.getName()+"' as alias");
        return;
       }
      }
     }
    }
    
    lst.addData( new AliasRecord(value) );
    classImp.addAlias(value);
   }});
 }
 
 private void removeAlias()
 {
  AliasRecord cr = (AliasRecord)lst.getSelectedRecord();
  
  if( cr == null )
   return;
  
  lst.removeData(cr);
  classImp.removeAlias(cr.getAlias());
 }

 class AliasRecord extends ListGridRecord
 {
  AliasRecord( String alias )
  {
   super();
   
   
   setAttribute("icon", "alias" );
   setAttribute("alias", alias );
  }

  public String getAlias()
  {
   return getAttribute("alias");
  }
 }
}
