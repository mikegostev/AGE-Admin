package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class InverseRelationPanel extends VLayout
{
 private ListGrid lst;
 private AgeRelationClassImprint classImp;
 
 public InverseRelationPanel(AgeRelationClassImprint cls)
 {
  super(0);
  
  setWidth(150);
  
  classImp = cls;
  
  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();
  
  Label lbl = new Label("Inverse relation");
  lbl.setMargin(5);
  superTS.addMember( lbl );
  superTS.addFill();
  
  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon(ModeledIcons.get.inverseRelationAdd());
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    setRelation();
   }
  });
  superTS.addButton(btadd);

  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon(ModeledIcons.get.inverseRelationDelete());
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    removeRelation();
   }
  });
  superTS.addButton(btdel);

  lst = new ListGrid();
  lst.setShowHeader(false);
  lst.setWidth100();
  lst.setHeight(30);
  lst.setBodyOverflow(Overflow.VISIBLE);
  lst.setOverflow(Overflow.VISIBLE);

  ListGridField iconField = new ListGridField("icon", "", 20);
  iconField.setAlign(Alignment.CENTER);
  iconField.setType(ListGridFieldType.IMAGE);

  ListGridField aliasField = new ListGridField("rel", "Relation");

  lst.setFields(iconField,aliasField);
  
  addMember(superTS);
  addMember(lst);
  
  if( cls.getInverseRelation() != null )
  {
   lst.addData( new RelRecord(cls.getInverseRelation()) );
  }
 }
 
 private void setRelation()
 {
  new XSelectDialog<AgeRelationClassImprint>(classImp.getModel().getRootRelationClass(), RelationMetaClassDef.getInstance(), new ClassSelectedAdapter()
  {
   @Override
   public void classSelected(AgeAbstractClassImprint cls)
   {
    ListGridRecord[] recs = lst.getRecords();
    
    if( recs == null || recs.length == 0 )
     lst.addData( new RelRecord(cls) );
    else
    {
     ((RelRecord)recs[0]).setName(cls.getName());
     lst.refreshCell(0, 1);
    }
    
    AgeRelationClassImprint relCls = (AgeRelationClassImprint)cls;
    
    classImp.setInverseRelation(relCls);
    relCls.setInverseRelation(classImp);
    
   }
  }).show();

 }
 
 private void removeRelation()
 {
  ListGridRecord[] recs = lst.getRecords();
  
  if( recs == null || recs.length == 0 )
   return;
  
  classImp.getInverseRelation().setInverseRelation(null);
  classImp.setInverseRelation(null);
  
  lst.removeData(recs[0]);
 }

 class RelRecord extends ListGridRecord
 {
  RelRecord( AgeAbstractClassImprint ageRelationClassImprint )
  {
   super();
   
   setAttribute("icon", RelationMetaClassDef.getIcon(ageRelationClassImprint) );
   setAttribute("rel", ageRelationClassImprint.getName() );
  }

  public void setName(String name)
  {
   setAttribute("rel", name );
  }
 }
}
