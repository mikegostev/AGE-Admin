package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.Collection;
import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.restriction.Cardinality;
import uk.ac.ebi.age.admin.client.model.restriction.QualifierRule;
import uk.ac.ebi.age.admin.client.model.restriction.QualifiersCondition;
import uk.ac.ebi.age.admin.client.model.restriction.RelationRule;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.QualifiersRecord;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class RelationMNOTRulePanel extends RelationRulePanel
{
 private RelationRule rule;

 private final StaticTextItem relClassItm;
 private final StaticTextItem tagClassItm;
 private CheckboxItem relSubclCb;
 private CheckboxItem tagSubclCb;

 private AgeRelationClassImprint relationClass;
 private AgeClassImprint targetClass;

 
 private RadioGroupItem rangeSelect;
 private TextItem cardVal;
 
 private RadioGroupItem qualCondition;
 private ListGrid qTbl;
 
 RelationMNOTRulePanel( final ModelImprint model )
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
   
   relClassItm = new StaticTextItem();
   relClassItm.setTitle("Attribute class");
   relClassItm.setWidth(50);
   relClassItm.setAlign(Alignment.CENTER);
   
   FormItemIcon icn = new FormItemIcon();
   icn.setSrc("../images/icons/relation/selbt.png");
   icn.addFormItemClickHandler(new FormItemClickHandler()
   {
    @Override
    public void onFormItemClick(FormItemIconClickEvent event)
    {
     new XSelectDialog<AgeRelationClassImprint>(model.getRootRelationClass(), RelationMetaClassDef.getInstance(), new ClassSelectedCallback()
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
   relClassItm.setIcons(icn);
   
   relSubclCb = new CheckboxItem();
   relSubclCb.setTitle("Including subclasses");
   
   relForm.setItems(relClassItm,relSubclCb);

   addMember(relForm);
  }
  
  {
   DynamicForm targetForm = new DynamicForm();
   targetForm.setGroupTitle("Target class");
   targetForm.setIsGroup(true);
   targetForm.setPadding(5);
   
   tagClassItm = new StaticTextItem();
   tagClassItm.setTitle("Target class");
   tagClassItm.setWidth(50);
   tagClassItm.setAlign(Alignment.CENTER);
   
   FormItemIcon icn = new FormItemIcon();
   icn.setSrc("../images/icons/class/selbt.png");
   icn.addFormItemClickHandler(new FormItemClickHandler()
   {
    @Override
    public void onFormItemClick(FormItemIconClickEvent event)
    {
     new XSelectDialog<AgeClassImprint>(model.getRootClass(), ClassMetaClassDef.getInstance(), new ClassSelectedCallback()
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
   tagClassItm.setIcons(icn);
   
   tagSubclCb = new CheckboxItem();
   tagSubclCb.setTitle("Including subclasses");
   
   targetForm.setItems(tagClassItm,tagSubclCb);

   addMember(targetForm);
  }
  
  
  DynamicForm rangeForm = new DynamicForm();
  rangeForm.setGroupTitle("Multiplicity condition");
  rangeForm.setIsGroup(true);
  
  rangeSelect = new RadioGroupItem();
  rangeSelect.setTitle("Multiplicity");

  LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
  
  valueMap.put(Cardinality.ANY.name(),"any");
  valueMap.put(Cardinality.EXACT.name(),"exactly");
  valueMap.put(Cardinality.MAX.name(),"less or equal");
  valueMap.put(Cardinality.MIN.name(),"more or equal");
  
  rangeSelect.setValueMap(valueMap);

  cardVal = new TextItem();
  cardVal.setValidateOnChange(true);
//  cardVal.setEmptyDisplayValue("(inf)");
  cardVal.setTitle("Cardinality");
  IntegerRangeValidator vldtr = new IntegerRangeValidator();
  vldtr.setMin(0);
  cardVal.setValidators(vldtr);
 
  rangeForm.setItems(rangeSelect,cardVal);
  
  addMember(rangeForm);
  


  DynamicForm qualifiersForm = new DynamicForm();
  qualifiersForm.setGroupTitle("Qualifiers");
  qualifiersForm.setIsGroup(true);
  qualifiersForm.setHeight(190);
//  qualifiersForm.setNumCols(6);

  qualCondition = new RadioGroupItem();
  qualCondition.setTitle("Qualifiers");
  qualCondition.setVertical(false);
  qualCondition.setWidth(1);
  
  valueMap = new LinkedHashMap<String, String>();
  
  valueMap.put(QualifiersCondition.ANY.name(),"regardless");
  valueMap.put(QualifiersCondition.NONE.name(),"none");
  valueMap.put(QualifiersCondition.SPECIFIED.name(),"selected");
  
  qualCondition.setValueMap(valueMap);

  
  CanvasItem qTblItem = new CanvasItem();
  qTblItem.setColSpan(2);
  
  VLayout qLay = new VLayout();
  qLay.setWidth("98%");
  qLay.setHeight100();
  qLay.setPadding(5);
  
  ToolStrip qTools = new ToolStrip();
  qTools.setWidth100();
 
  qTbl = new ListGrid();

  
  ToolStripButton chldBut = new ToolStripButton();
  chldBut.setTitle("Add Qualifier");
  chldBut.setIcon("../images/icons/qualifier/qadd.png");
  chldBut.addClickHandler( new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    new XSelectDialog<AgeAttributeClassImprint>(model.getRootAttributeClass(), AttributeMetaClassDef.getInstance(), new ClassSelectedCallback()
    {
     
     @Override
     public void classSelected(AgeAbstractClassImprint cls)
     {
      qTbl.addData(new QualifiersRecord(RestrictionType.MAY, (AgeAttributeClassImprint)cls));
     }
    }).show();

   }
  });
  
  qTools.addButton(chldBut);
  
  ToolStripButton sibBut = new ToolStripButton();
  sibBut.setTitle("Remove qualifier");
  sibBut.setIcon("../images/icons/qualifier/qdel.png");
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
  
  qTools.addButton(sibBut);

  qLay.addMember(qTools);
  
  qTblItem.setShowTitle(false);
  
  qTbl.setWidth100();
  qTbl.setHeight(100);
  qTbl.setShowHeader(false);

  ListGridField typeIconField = new ListGridField("type", "Type", 40);
  typeIconField.setAlign(Alignment.CENTER);
  typeIconField.setType(ListGridFieldType.IMAGE);
  typeIconField.setImageURLPrefix("../images/icons/qualifier/");
  typeIconField.setImageURLSuffix(".png");

  ListGridField subclassNameField = new ListGridField("name", "Class");

  qTbl.setFields(typeIconField, subclassNameField);

  qTbl.addCellClickHandler(new CellClickHandler()
  {
   @Override
   public void onCellClick(CellClickEvent event)
   {
    if( event.getColNum() == 0 )
    {
     QualifiersRecord rec = (QualifiersRecord) event.getRecord();
     
     rec.toggleType();
    
     qTbl.refreshCell(event.getRowNum(), event.getColNum());
    }
   }
  });
  
  qTblItem.setCanvas(qLay);
  qLay.addMember(qTbl);
  
  qualifiersForm.setItems(qualCondition,qTblItem);

  addMember(qualifiersForm);
 
 }
 
 public void setRule(RelationRule rule)
 {
  this.rule = rule;

  targetClass = rule.getTargetClass();
  if( targetClass != null )
   tagClassItm.setValue(targetClass.getName());
  else 
   tagClassItm.setValue("");
  
  tagSubclCb.setValue(rule.isSubclassesIncluded());
  relSubclCb.setValue(rule.isRelationSubclassesIncluded());
  
  rangeSelect.setValue( rule.getCardinalityType().name() );
  cardVal.setValue(rule.getCardinality());
  
  qualCondition.setValue(rule.getQualifiersCondition().name());
  
  if( rule.getQualifiersMap() != null )
  {
   for( Collection<QualifierRule> qrc : rule.getQualifiersMap().values() )
   {
    if( qrc != null)
     for(QualifierRule qr : qrc )
      qTbl.addData( new QualifiersRecord(qr.getType(), qr.getAttributeClassImprint()) );
   }
  }
 }

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

  if(targetClass == null)
  {
   SC.warn("Target attribute class can't be empty");
   return false;
  }

  rule.setRelationSubclassesIncluded(relSubclCb.getValueAsBoolean());
  rule.setSubclassesIncluded(tagSubclCb.getValueAsBoolean());

  rule.setCardinalityType(Cardinality.valueOf(rangeSelect.getValue().toString()));
  rule.setCardinality(card);
  rule.setTargetClass(targetClass);
  rule.setRelationClass(relationClass);

  rule.setQualifiersCondition(QualifiersCondition.valueOf(qualCondition.getValue().toString()));
  
  ListGridRecord[] recs = qTbl.getRecords();

  rule.clearQualifiers();
  if(recs != null )
  {
   for(ListGridRecord r : recs)
   {
    QualifierRule qr = new QualifierRule();

    qr.setType(RestrictionType.MUST);
    qr.setAttributeClassImprint((AgeAttributeClassImprint)((QualifiersRecord) r).getAgeAbstractClassImprint());

    rule.addQualifier(qr);
   }
  }

  return true;
 }

 
 public RelationRule getRule()
 {
  return rule;
 }

}
