package uk.ac.ebi.age.admin.client.ui.module.modeled;

import java.util.LinkedHashMap;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;
import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;
import uk.ac.ebi.age.admin.client.model.AttributeRuleImprint;
import uk.ac.ebi.age.admin.client.model.Cardinality;
import uk.ac.ebi.age.admin.client.model.QualifierRuleImprint;
import uk.ac.ebi.age.admin.client.ui.AttributeMetaClassDef;
import uk.ac.ebi.age.admin.client.ui.ClassSelectedAdapter;
import uk.ac.ebi.age.admin.client.ui.QualifiersRecord;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
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

public class AttributeMMRulePanel extends AttributeRulePanel
{
 private AttributeRuleImprint rule;
 private AgeAttributeClassImprint targetClass;
 private RadioGroupItem cardType;
 private final StaticTextItem attrTgClass;
 private ListGrid qTbl;
 private TextItem cardVal;
 private CheckboxItem subclCb;
 private CheckboxItem subclCntSep;
 private CheckboxItem valUniq;
// private CheckboxItem qualUniq;
 
 AttributeMMRulePanel( AttributeRuleImprint rl )
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
   targetForm.setNumCols(3);
   
   attrTgClass = new StaticTextItem();
   attrTgClass.setTitle("Attribute class");
   attrTgClass.setWidth(50);
   attrTgClass.setAlign(Alignment.CENTER);
   
   attrTgClass.setEndRow(false);
   
   ButtonItem  selBtnItm = new ButtonItem();
   selBtnItm.setTitle("Select attribute");
   selBtnItm.setIcon(ModeledIcons.get.attributeRules());
   selBtnItm.setStartRow(false);
   selBtnItm.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler()
   {
    @Override
    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event)
    {
     new XSelectDialog<AgeAttributeClassImprint>(rule.getModel().getRootAttributeClass(), AttributeMetaClassDef.getInstance(), new ClassSelectedAdapter()
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

   
   subclCb = new CheckboxItem();
   subclCb.setTitle("Including subclasses");
   
   targetForm.setItems(attrTgClass,selBtnItm,subclCb);

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
  
  subclCntSep = new CheckboxItem();
  subclCntSep.setTitle("Count subclasses separately");
  
  rangeForm.setItems(cardType,cardVal,valUniq,subclCntSep);
  
  addMember(rangeForm);
  


  DynamicForm qualifiersForm = new DynamicForm();
  qualifiersForm.setGroupTitle("Qualifiers");
  qualifiersForm.setIsGroup(true);
//  qualifiersForm.setPadding(1);
  qualifiersForm.setWidth100();
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
  
  qTblItem.setShowTitle(false);
  
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
    if( event.getColNum() == 1 )
    {
     ((QualifiersRecord)event.getRecord()).toggleUnique( );
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
 
 @Override
 public void setRule(AttributeRuleImprint rule)
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
  subclCntSep.setValue(rule.isSubclassesCountedSeparately());
  
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
  rule.setSubclassesCountedSeparately(subclCntSep.getValueAsBoolean());
  
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
 public AttributeRuleImprint getRule()
 {
  return rule;
 }

}
