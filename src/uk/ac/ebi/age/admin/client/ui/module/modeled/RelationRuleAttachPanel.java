package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.ModeledIcons;
import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedRelationRule;

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

public class RelationRuleAttachPanel extends VLayout
{

 public RelationRuleAttachPanel( final AgeClassImprint cls, final XEditorPanel editor)
 {
  setTitle("Relations");
  
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
//  typeIconField.setImageURLPrefix("../images/icons/restriction/");
//  typeIconField.setImageURLSuffix(".png");

  ListGridField ruleField = new ListGridField("name", "Rule");

  ruleList.setFields(idField, typeIconField, ruleField);

  if( cls.getRelationRules() != null )
  {
   for( RelationRuleImprint rr : cls.getRelationRules() )
    ruleList.addData(new RuleRecord(rr));
  }
  
  ToolStripButton btaddMay = new ToolStripButton();
  btaddMay.setIcon(ModeledIcons.get.relationAddMayRule());
  btaddMay.setSelected(true);
  btaddMay.setTitle("Add MAY rule");
  btaddMay.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    RelationRuleImprint rule = cls.getModel().createRelationRuleImprint(RestrictionType.MAY);
    
    rule.setId( cls.getModel().generateId() );

    RelationMMRuleDialog.show(rule, cls.getModel(), new SelectedRelationRule(){

     @Override
     public void relationRuleSelected(RelationRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addRelationRule(ar);
     }});
   }
  });
  superTS.addSpacer(4);
  superTS.addButton(btaddMay);

  ToolStripButton btaddMust = new ToolStripButton();
  btaddMust.setIcon(ModeledIcons.get.relationAddMustRule());
  btaddMust.setSelected(true);
  btaddMust.setTitle("Add MUST rule");
  btaddMust.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    RelationRuleImprint rule = cls.getModel().createRelationRuleImprint(RestrictionType.MUST);
    
    rule.setId( cls.getModel().generateId() );

    RelationMMRuleDialog.show(rule, cls.getModel(), new SelectedRelationRule(){

     @Override
     public void relationRuleSelected(RelationRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addRelationRule(ar);
     }});
   }
  });
  superTS.addSpacer(4);
  superTS.addButton(btaddMust);


  ToolStripButton btaddMustNot = new ToolStripButton();
  btaddMustNot.setIcon(ModeledIcons.get.relationAddMustnotRule());
  btaddMustNot.setSelected(true);
  btaddMustNot.setTitle("Add MUSTNOT rule");
  btaddMustNot.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    RelationRuleImprint rule = cls.getModel().createRelationRuleImprint(RestrictionType.MUSTNOT);
    
    rule.setId( cls.getModel().generateId() );

    RelationMNOTRuleDialog.show(rule, cls.getModel(), new SelectedRelationRule(){

     @Override
     public void relationRuleSelected(RelationRuleImprint ar)
     {
      ruleList.addData(new RuleRecord(ar));
      cls.addRelationRule(ar);
     }});
   }
  });
  superTS.addSpacer(4);
  superTS.addButton(btaddMustNot);

  
  ToolStripButton btEdit = new ToolStripButton();
  btEdit.setIcon(ModeledIcons.get.relationEditRule());
  btEdit.setSelected(true);
  btEdit.setTitle("Edit rule");
  btEdit.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final RuleRecord rr = (RuleRecord)ruleList.getSelectedRecord();
    
    if( rr == null )
     return;
    
    RelationRuleImprint atrl = rr.getRule();
    
    if( atrl.getType() == RestrictionType.MUSTNOT )
    {
     RelationMNOTRuleDialog.show(atrl, cls.getModel(), new SelectedRelationRule()
     {

      @Override
      public void relationRuleSelected(RelationRuleImprint ar)
      {
       rr.update();
       ruleList.refreshCell(ruleList.getRecordIndex(rr), 2);
      }
     });
    }
    else
    {
     RelationMMRuleDialog.show(atrl, cls.getModel(), new SelectedRelationRule()
     {

      @Override
      public void relationRuleSelected(RelationRuleImprint ar)
      {
       rr.update();
       ruleList.refreshCell(ruleList.getRecordIndex(rr), 2);
      }
     });
    }
     
   }
  });
  superTS.addSpacer(4);
  superTS.addButton(btEdit);
 
  
  ToolStripButton btdel = new ToolStripButton();
  btdel.setTitle("Delete rule");
  btdel.setSelected(true);
  btdel.setIcon(ModeledIcons.get.relationDeleteRule());
  btdel.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    final RuleRecord rec = (RuleRecord)ruleList.getSelectedRecord();
    
    if(rec == null)
     return;

    ruleList.removeData(rec);
    cls.removeRelationRule(rec.getRule());
   }
  });
  superTS.addSpacer(4);
  superTS.addButton(btdel);

  addMember(superTS);

  addMember(ruleList);

 }

 static class RuleRecord extends ListGridRecord
 {
  private RelationRuleImprint rule;
  
  RuleRecord( RelationRuleImprint r )
  {
   super();
   
   rule=r;
   
   setAttribute("id", rule.getId() );
   setAttribute("type", ModeledIcons.get.getString("relRule"+rule.getType().name() ) );
   setAttribute("name", r.toString() );
  }
  
  public void update()
  {
   setAttribute("name", rule.toString() );
  }

  public RelationRuleImprint getRule()
  {
   return rule;
  }

 }
}
