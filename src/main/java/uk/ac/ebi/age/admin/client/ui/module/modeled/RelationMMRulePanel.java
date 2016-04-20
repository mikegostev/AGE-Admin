package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.Cardinality;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.QualifiersRecord;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class RelationMMRulePanel extends RelationRulePanel
{
 private RelationRuleImprint rule;
 private AgeRelationClassImprint relationClass;
 private AgeClassImprint targetClass;
 private RadioGroupItem cardType;
 private final StaticTextItem relClassItm;
 private final StaticTextItem tagClassItm;
 private ListGrid qTbl;
 private TextItem cardVal;
 private CheckboxItem relSubclCb;
 private CheckboxItem tagSubclCb;
 
// private CheckboxItem qualUniq;
 
 RelationMMRulePanel( final RelationRuleImprint rl )
 {
  setWidth100();
  setHeight100();
  setPadding(10);
  setMembersMargin(10);
 
  {
   DynamicForm relForm = new DynamicForm();
   relForm.setGroupTitle("Relation");
   relForm.setIsGroup(true);
   relForm.setPadding(5);
   relForm.setNumCols(3);
   
   relClassItm = new StaticTextItem();
   relClassItm.setTitle("Relation class");
   relClassItm.setWidth(50);
   relClassItm.setAlign(Alignment.CENTER);
   relClassItm.setEndRow(false);
   
   ButtonItem  selBtnItm = new ButtonItem();
   selBtnItm.setTitle("Select class");
   selBtnItm.setIcon(ModeledIcons.get.relation());
   selBtnItm.setStartRow(false);
   selBtnItm.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
   {
    @Override
    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
    {
     new XSelectDialog<AgeRelationClassImprint>(rule.getModel().getRootRelationClass(), RelationMetaClassDef.getInstance(), new ClassSelectedAdapter()
     {
      
      @Override
      public void classSelected(AgeAbstractClassImprint cls)
      {
       relClassItm.setValue("<span class='relRef'>"+cls.getName()+"</span>");
       relationClass = (AgeRelationClassImprint)cls;
      }
     }).show();

     
    }
   });
   
   
   relSubclCb = new CheckboxItem();
   relSubclCb.setTitle("Including subclasses");
   
   relForm.setItems(relClassItm, selBtnItm, relSubclCb);

   addMember(relForm);
  }
  
  {
   DynamicForm targetForm = new DynamicForm();
   targetForm.setGroupTitle("Target class");
   targetForm.setIsGroup(true);
   targetForm.setPadding(5);
   targetForm.setNumCols(3);
   
   tagClassItm = new StaticTextItem();
   tagClassItm.setTitle("Target class");
   tagClassItm.setWidth(50);
   tagClassItm.setAlign(Alignment.CENTER);
   
   tagClassItm.setEndRow(false);
   
   ButtonItem  selBtnItm = new ButtonItem();
   selBtnItm.setTitle("Select class");
   selBtnItm.setIcon(ModeledIcons.get.ageClass());
   selBtnItm.setStartRow(false);
   selBtnItm.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
   {
    @Override
    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
    {
     new XSelectDialog<AgeClassImprint>(rule.getModel().getRootClass(), ClassMetaClassDef.getInstance(), new ClassSelectedAdapter()
     {
      
      @Override
      public void classSelected(AgeAbstractClassImprint cls)
      {
       tagClassItm.setValue("<span class='clsRef'>"+cls.getName()+"</span>");
       targetClass = (AgeClassImprint)cls;
      }
     }).show();
     
    }
   });
   
   
   tagSubclCb = new CheckboxItem();
   tagSubclCb.setTitle("Including subclasses");
   
   targetForm.setItems(tagClassItm, selBtnItm, tagSubclCb);

   addMember(targetForm);
  }
  
  
  DynamicForm rangeForm = new DynamicForm();
  rangeForm.setGroupTitle("Relation cardinality");
  rangeForm.setIsGroup(true);
  
  cardType = new RadioGroupItem();
  cardType.setTitle("Type");

  LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
  
  for( Cardinality rc : Cardinality.values() )
   valueMap.put(rc.name(),rc.getTitle());
  
  cardType.setValueMap(valueMap);

  cardVal = new TextItem();
  cardVal.setValidateOnChange(true);
//  cardVal.setEmptyDisplayValue("(inf)");
  cardVal.setTitle("Cardinality");
  IntegerRangeValidator vldtr = new IntegerRangeValidator();
  vldtr.setMin(0);
  cardVal.setValidators(vldtr);
//  cardVal.setHint("empty or zero means infinity");

  
//  qualUniq = new CheckboxItem("qualuniq");
//  qualUniq.setTitle("Qualifiers' value set must be unique");
 
  rangeForm.setItems(cardType,cardVal);
  
  addMember(rangeForm);
  

//  DynamicForm qualifiersForm = new DynamicForm();
//  qualifiersForm.setGroupTitle("Qualifiers");
//  qualifiersForm.setIsGroup(true);
////  qualifiersForm.setPadding(1);
////  qualifiersForm.setWidth100();
//  qualifiersForm.setHeight(200);
//
//  CanvasItem qTblItem = new CanvasItem();

  VLayout qLay = new VLayout();
  qLay.setGroupTitle("Qualifiers");
  qLay.setIsGroup(true);
  qLay.setWidth100();
  qLay.setHeight100();
  qLay.setPadding(5);
  
  ToolStrip qTools = new ToolStrip();
  qTools.setWidth100();
 
  qTbl = new ListGrid();

  
  ToolStripButton chldBut = new ToolStripButton();
  chldBut.setTitle("Add Qualifier");
  chldBut.setSelected(true);
  chldBut.setIcon(ModeledIcons.get.qualifierAdd());
  chldBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new XSelectDialog<AgeAttributeClassImprint>(rule.getModel().getRootAttributeClass(), AttributeMetaClassDef.getInstance(), new ClassSelectedAdapter()
    {
     
     @Override
     public void classSelected(AgeAbstractClassImprint cls)
     {
      qTbl.addData(new QualifiersRecord(cls.getModel().generateId(), false, cls));
     }
    }).show();

   }
  });
  qTools.addSpacer(5);
  qTools.addButton(chldBut);
  
  ToolStripButton sibBut = new ToolStripButton();
  sibBut.setTitle("Remode qualifier");
  sibBut.setSelected(true);
  sibBut.setIcon(ModeledIcons.get.qualifierDelete());
  sibBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    QualifiersRecord rec = (QualifiersRecord)qTbl.getSelectedRecord();
    
    if( rec == null )
     return;
    
    qTbl.removeData(rec);
   }
  });
  
  qTools.addSpacer(5);
  qTools.addButton(sibBut);

  qLay.addMember(qTools);
  
  qTbl.setWidth100();
  qTbl.setShowHeader(true);

  ListGridField idField = new ListGridField("id", "ID", 60);
  idField.setType(ListGridFieldType.INTEGER);
  idField.setAlign(Alignment.RIGHT);
 
  ListGridField uniqField = new ListGridField("uniq", "Unique", 40);
  uniqField.setType(ListGridFieldType.BOOLEAN);
  uniqField.setAlign(Alignment.CENTER);

  ListGridField subclassNameField = new ListGridField("name", "Class");

  qTbl.setFields(idField, uniqField, subclassNameField);

  qTbl.addCellClickHandler( new CellClickHandler()
  {
   @Override
   public void onCellClick(CellClickEvent event)
   {
    System.out.println(event.getColNum());
   }
  });
 
  qLay.addMember(qTbl);

  addMember(qLay); 

  setRule(rl);
 }
 
 @Override
 public void setRule(RelationRuleImprint rule)
 {
  this.rule = rule;
  
  relationClass = rule.getRelationClass();
  if( relationClass != null )
   relClassItm.setValue(relationClass.getName());
  else
   relClassItm.setValue("");
 
  targetClass = rule.getTargetClass();
  if( targetClass != null )
   tagClassItm.setValue(targetClass.getName());
  else
   tagClassItm.setValue("");

  
  relSubclCb.setValue( rule.isRelationSubclassesIncluded() );
  tagSubclCb.setValue( rule.isSubclassesIncluded() );

  cardType.setValue( rule.getCardinalityType().name() );
  cardVal.setValue(rule.getCardinality());
  
  if( rule.getQualifiers() != null )
  {
   for(QualifierRuleImprint qr : rule.getQualifiers() )
    qTbl.addData(new QualifiersRecord(qr.getId(), qr.isUnique(), qr.getAttributeClassImprint()));
  }

 }

 @Override
 public boolean updateRule()
 {
  String cVal = cardVal.getValue().toString();
  int card;

  if(cVal == null)
   card = 0;
  else
  {
   cVal = cVal.trim();

   if(cVal.length() == 0)
    card = 0;
   else
   {
    try
    {
     card = Integer.valueOf(cVal);
    }
    catch(Exception e)
    {
     card = -1;
    }

    if(card < 0)
    {
     SC.warn("Cardinality must be non-negative integer number");
     return false;
    }

   }
  }

  if(targetClass == null || relationClass == null )
  {
   SC.warn("Target and relation classes can't be empty");
   return false;
  }


  rule.setRelationSubclassesIncluded(relSubclCb.getValueAsBoolean());
  rule.setSubclassesIncluded(tagSubclCb.getValueAsBoolean());
  
  rule.setCardinalityType(Cardinality.valueOf(cardType.getValue().toString()));
  rule.setCardinality(card);
  rule.setTargetClass(targetClass);
  rule.setRelationClass(relationClass);

  ListGridRecord[] recs = qTbl.getRecords();

  rule.clearQualifiers();
  if(recs != null )
  {
   for(ListGridRecord r : recs)
   {
    QualifierRuleImprint qr = rule.getModel().createQualifierRuleImprint();

    QualifiersRecord qRec = (QualifiersRecord) r;
    
    qr.setId(qRec.getId());
    qr.setUnique(qRec.isUniq() );
    qr.setAttributeClassImprint((AgeAttributeClassImprint)qRec.getAgeAbstractClassImprint());

    rule.addQualifier(qr);
   }
  }

  return true;
 }

 
 @Override
 public RelationRuleImprint getRule()
 {
  return rule;
 }

}
