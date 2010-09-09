package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.Collection;
import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.QualifiersRecord;
import uk.ac.ebi.age.admin.client.ui.RelationMetaClassDef;
import uk.ac.ebi.age.model.Cardinality;
import uk.ac.ebi.age.model.RestrictionType;

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
 
 private CheckboxItem qualUniq;
 
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
     new XSelectDialog<AgeRelationClassImprint>(rule.getModel().getRootRelationClass(), RelationMetaClassDef.getInstance(), new ClassSelectedCallback()
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
     new XSelectDialog<AgeClassImprint>(rule.getModel().getRootClass(), ClassMetaClassDef.getInstance(), new ClassSelectedCallback()
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

  
  qualUniq = new CheckboxItem("qualuniq");
  qualUniq.setTitle("Qualifiers' value set must be unique");
 
  rangeForm.setItems(cardType,cardVal,qualUniq);
  
  addMember(rangeForm);
  


  DynamicForm qualifiersForm = new DynamicForm();
  qualifiersForm.setGroupTitle("Qualifiers");
  qualifiersForm.setIsGroup(true);
//  qualifiersForm.setPadding(1);
//  qualifiersForm.setWidth100();
  qualifiersForm.setHeight(160);

  CanvasItem qTblItem = new CanvasItem();
  
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
    new XSelectDialog<AgeAttributeClassImprint>(rule.getModel().getRootAttributeClass(), AttributeMetaClassDef.getInstance(), new ClassSelectedCallback()
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
  sibBut.setTitle("Remode qualifier");
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
  qTbl.setShowHeader(false);

  ListGridField typeIconField = new ListGridField("type", "Type", 40);
  typeIconField.setAlign(Alignment.CENTER);
  typeIconField.setType(ListGridFieldType.IMAGE);
  typeIconField.setImageURLPrefix("../images/icons/restriction/");
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
  
  qLay.addMember(qTbl);
  qTblItem.setCanvas(qLay);
  
  qualifiersForm.setItems(qTblItem);

  addMember(qualifiersForm);
 
  setRule(rl);
 }
 
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
  
  qualUniq.setValue(rule.isQualifiersUnique());
  
  if( rule.getQualifiersMap() != null )
  {
   for( Collection<QualifierRuleImprint> qrc : rule.getQualifiersMap().values() )
   {
    if( qrc != null)
     for(QualifierRuleImprint qr : qrc )
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

  rule.setQualifiersUnique(qualUniq.getValueAsBoolean());
  
  ListGridRecord[] recs = qTbl.getRecords();

  rule.clearQualifiers();
  if(recs != null )
  {
   for(ListGridRecord r : recs)
   {
    QualifierRuleImprint qr = rule.getModel().createQualifierRuleImprint();

    qr.setType(((QualifiersRecord) r).getType());
    qr.setAttributeClassImprint( (AgeAttributeClassImprint)((QualifiersRecord) r).getAgeAbstractClassImprint());

    rule.addQualifier(qr);
   }
  }

  return true;
 }

 
 public RelationRuleImprint getRule()
 {
  return rule;
 }

}
