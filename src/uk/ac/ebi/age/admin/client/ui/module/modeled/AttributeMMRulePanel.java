package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.Collection;
import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeRule;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedCallback;
import uk.ac.ebi.age.admin.client.ui.QualifiersRecord;
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

public class AttributeMMRulePanel extends AttributeRulePanel
{
 private AttributeRule rule;
 private AgeAttributeClassImprint targetClass;
 private RadioGroupItem cardType;
 private final StaticTextItem attrTgClass;
 private ListGrid qTbl;
 private TextItem cardVal;
 private CheckboxItem subclCb;
 private CheckboxItem valUniq;
 private CheckboxItem qualUniq;
 
 AttributeMMRulePanel( AttributeRule rl )
 {
  setWidth100();
  setHeight100();
  setPadding(10);
  setMembersMargin(10);
 
  /*
  DynamicForm typeForm = new DynamicForm();
  typeForm.setGroupTitle("Rule type");
  typeForm.setIsGroup(true);
  
  typeSelect = new RadioGroupItem();
  typeSelect.setTitle("Type");

  LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
  
  for( RestrictionType rc : RestrictionType.values() )
   valueMap.put(rc.name(),rc.getTitle());

  typeSelect.setValueMap(valueMap);

  typeForm.setItems( typeSelect );
  
  addMember(typeForm);
  */
  
  {
   DynamicForm targetForm = new DynamicForm();
   targetForm.setGroupTitle("Target attribute");
   targetForm.setIsGroup(true);
   targetForm.setPadding(5);
   
   attrTgClass = new StaticTextItem();
   attrTgClass.setTitle("Attribute class");
   attrTgClass.setWidth(50);
   attrTgClass.setAlign(Alignment.CENTER);
   
   FormItemIcon icn = new FormItemIcon();
   icn.setSrc("../images/icons/attribute/selbt2.png");
   icn.addFormItemClickHandler(new FormItemClickHandler()
   {
    @Override
    public void onFormItemClick(FormItemIconClickEvent event)
    {
     new XSelectDialog<AgeAttributeClassImprint>(rule.getModel().getRootAttributeClass(), AttributeMetaClassDef.getInstance(), new ClassSelectedCallback()
     {
      
      @Override
      public void classSelected(AgeAbstractClassImprint cls)
      {
       attrTgClass.setValue("<span class='attrRef'>"+cls.getName()+"</span>");
       targetClass = (AgeAttributeClassImprint)cls;
      }
     }).show();

     
    }
   });
   attrTgClass.setIcons(icn);
   
   subclCb = new CheckboxItem();
   subclCb.setTitle("Including subclasses");
   
   targetForm.setItems(attrTgClass,subclCb);

   addMember(targetForm);
  }
  
  
  DynamicForm rangeForm = new DynamicForm();
  rangeForm.setGroupTitle("Value multiplicity");
  rangeForm.setIsGroup(true);
  
  cardType = new RadioGroupItem();
  cardType.setTitle("Multiplicity");

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
  
  valUniq = new CheckboxItem("valuniq");
  valUniq.setTitle("Values must be unique");
  
  qualUniq = new CheckboxItem("qualuniq");
  qualUniq.setTitle("Qualifiers' value set must be unique");
 
  rangeForm.setItems(cardType,cardVal,valUniq,qualUniq);
  
  addMember(rangeForm);
  


  DynamicForm qualifiersForm = new DynamicForm();
  qualifiersForm.setGroupTitle("Qualifiers");
  qualifiersForm.setIsGroup(true);
//  qualifiersForm.setPadding(1);
//  qualifiersForm.setWidth100();
  qualifiersForm.setHeight(200);

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
 
 public void setRule(AttributeRule rule)
 {
  this.rule = rule;
  
  targetClass = rule.getAttributeClass();
  if( targetClass != null )
   attrTgClass.setValue(targetClass.getName());
  else
   attrTgClass.setValue("");
   
  subclCb.setValue( rule.isSubclassesIncluded() );

  cardType.setValue( rule.getCardinalityType().name() );
  cardVal.setValue(rule.getCardinality());
  
  valUniq.setValue(rule.isValueUnique());
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

  if(targetClass == null)
  {
   SC.warn("Target attribute class can't be empty");
   return false;
  }

  rule.setSubclassesIncluded(subclCb.getValueAsBoolean());
  
  rule.setCardinalityType(Cardinality.valueOf(cardType.getValue().toString()));
  rule.setCardinality(card);
  rule.setAttributeClass(targetClass);

  rule.setValueUnique(valUniq.getValueAsBoolean());
  rule.setQualifiersUnique(qualUniq.getValueAsBoolean());
  
  ListGridRecord[] recs = qTbl.getRecords();

  rule.clearQualifiers();
  if(recs != null )
  {
   for(ListGridRecord r : recs)
   {
    QualifierRuleImprint qr = rule.getModel().createQualifierRuleImprint();

    qr.setType(((QualifiersRecord) r).getType());
    qr.setAttributeClassImprint((AgeAttributeClassImprint)((QualifiersRecord) r).getAgeAbstractClassImprint());

    rule.addQualifier(qr);
   }
  }

  return true;
 }

 
 public AttributeRule getRule()
 {
  return rule;
 }

}
