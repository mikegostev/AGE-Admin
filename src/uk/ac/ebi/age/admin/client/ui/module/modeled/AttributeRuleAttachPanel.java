package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AttributeRuleImprint;
import uk.ac.ebi.age.admin.client.model.AttributedImprintClass;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class AttributeRuleAttachPanel extends VLayout
{

 public AttributeRuleAttachPanel( final AttributedImprintClass cls, final XEditorPanel editor)
 {
  setTitle("Attributes");
  
  setWidth100();
  setMargin(3);

  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();

  final ListGrid ruleList = new ListGrid();
  ruleList.setShowHeader(false);
  ruleList.setWrapCells(true);
  ruleList.setFixedRecordHeights(false);
  ruleList.setBodyOverflow(Overflow.VISIBLE);
  ruleList.setOverflow(Overflow.VISIBLE);

  ListGridField idField = new ListGridField("id", "ID", 60);
  idField.setType(ListGridFieldType.INTEGER);
  idField.setAlign(Alignment.RIGHT);

  
  ListGridField typeIconField = new ListGridField("type", "Type", 40);
  typeIconField.setAlign(Alignment.CENTER);
  typeIconField.setType(ListGridFieldType.IMAGE);
  typeIconField.setImageURLPrefix("../images/icons/restriction/");
  typeIconField.setImageURLSuffix(".png");

  ListGridField ruleField = new ListGridField("name", "Rule");

  ruleList.setFields(idField, typeIconField, ruleField);

  ToolStripButton btaddMay = new ToolStripButton();
  btaddMay.setIcon("../images/icons/attribute/attach.png");
  btaddMay.setTitle("Add MAY rule");
  btaddMay.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    AttributeRuleImprint rule = cls.getModel().createAttributeRuleImprint(RestrictionType.MAY);
    
    rule.setId( cls.getModel().generateId() );
    
    AttributeMMRuleDialog.show(rule, cls.getModel(), new SelectedAttrubuteRule(){

     @Override
     public void attributeRuleSelected(AttributeRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addAttributeRule(ar);
     }});
   }
  });
  superTS.addButton(btaddMay);

  ToolStripButton btaddMust = new ToolStripButton();
  btaddMust.setIcon("../images/icons/attribute/attach.png");
  btaddMust.setTitle("Add MUST rule");
  btaddMust.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    AttributeRuleImprint rule = cls.getModel().createAttributeRuleImprint(RestrictionType.MUST);
    
    rule.setId( cls.getModel().generateId() );
    
    AttributeMMRuleDialog.show(rule, cls.getModel(), new SelectedAttrubuteRule(){

     @Override
     public void attributeRuleSelected(AttributeRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addAttributeRule(ar);
     }});
   }
  });
  superTS.addButton(btaddMust);


  ToolStripButton btaddMustNot = new ToolStripButton();
  btaddMustNot.setIcon("../images/icons/attribute/attach.png");
  btaddMustNot.setTitle("Add MUSTNOT rule");
  btaddMustNot.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    AttributeRuleImprint rule = cls.getModel().createAttributeRuleImprint(RestrictionType.MUSTNOT);

    rule.setId( cls.getModel().generateId() );

    AttributeMNOTRuleDialog.show(rule, cls.getModel(), new SelectedAttrubuteRule(){

     @Override
     public void attributeRuleSelected(AttributeRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addAttributeRule(ar);
     }});
   }
  });
  superTS.addButton(btaddMustNot);

  
  ToolStripButton btEdit = new ToolStripButton();
  btEdit.setIcon("../images/icons/attribute/attachEdit.png");
  btEdit.setTitle("Edit rule");
  btEdit.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final RuleRecord rr = (RuleRecord)ruleList.getSelectedRecord();
    
    if( rr == null )
     return;
    
    AttributeRuleImprint atrl = rr.getRule();
    
    if( atrl.getType() == RestrictionType.MUSTNOT )
    {
     AttributeMNOTRuleDialog.show(atrl, cls.getModel(), new SelectedAttrubuteRule()
     {

      @Override
      public void attributeRuleSelected(AttributeRuleImprint ar)
      {
       rr.update();
       ruleList.refreshCell(ruleList.getRecordIndex(rr), 1);
      }
     });
    }
    else
    {
     AttributeMMRuleDialog.show(atrl, cls.getModel(), new SelectedAttrubuteRule()
     {

      @Override
      public void attributeRuleSelected(AttributeRuleImprint ar)
      {
       rr.update();
       ruleList.refreshCell(ruleList.getRecordIndex(rr), 1);
      }
     });
    }
     
   }
  });
  superTS.addButton(btEdit);
 
  
  ToolStripButton btdel = new ToolStripButton();
  btdel.setIcon("../images/icons/attribute/detach.png");
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final RuleRecord rec = (RuleRecord)ruleList.getSelectedRecord();
    
    if(rec == null)
     return;

    ruleList.removeData(rec);
    cls.removeAttribiteRule(rec.getRule());
   }
  });
  superTS.addButton(btdel);

  addMember(superTS);

  addMember(ruleList);

 }

 static class RuleRecord extends ListGridRecord
 {
  private AttributeRuleImprint rule;
  
  RuleRecord( AttributeRuleImprint r )
  {
   super();
   
   rule=r;
   
   setAttribute("id", rule.getId() );
   setAttribute("type", rule.getType().name() );
   setAttribute("name", r.toString() );
  }
  
  public void update()
  {
   setAttribute("name", rule.toString() );
  }

  public AttributeRuleImprint getRule()
  {
   return rule;
  }
  
  public void toggleType()
  {
   RestrictionType rtype = rule.getType();
   
   RestrictionType[] vals = RestrictionType.values();
   
   for(int i=0; i < vals.length; i++ )
   {
    if( vals[i] == rtype )
    {
     if( i == (vals.length-1) )
      rtype=vals[0];
     else
      rtype = vals[i+1];
     
     setAttribute("type", rtype.name() );
     rule.setType( rtype );
     return;
    }
   }
  }
  
  public int getId()
  {
   return getAttributeAsInt("id");
  }
 }
}
