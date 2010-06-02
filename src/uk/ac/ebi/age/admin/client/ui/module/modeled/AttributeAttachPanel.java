package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;
import uk.ac.ebi.age.admin.client.model.restriction.AttributeRule;
import uk.ac.ebi.age.admin.client.model.restriction.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class AttributeAttachPanel extends VLayout
{

 public AttributeAttachPanel( final AgeClassImprint cls, final XEditorPanel editor)
 {
  setTitle("Attributes");
  
  setWidth100();
  setMargin(3);

  ToolStrip superTS = new ToolStrip();
  superTS.setWidth100();

  final ListGrid ruleList = new ListGrid();
  ruleList.setShowHeader(false);
  
  ListGridField typeIconField = new ListGridField("type", "Type", 40);
  typeIconField.setAlign(Alignment.CENTER);
  typeIconField.setType(ListGridFieldType.IMAGE);
  typeIconField.setImageURLPrefix("../images/icons/restriction/");
  typeIconField.setImageURLSuffix(".png");

  ListGridField ruleField = new ListGridField("name", "Rule");

  ruleList.setFields(typeIconField, ruleField);

  ToolStripButton btadd = new ToolStripButton();
  btadd.setIcon("../images/icons/attribute/attach.png");
  btadd.addClickHandler(new ClickHandler()
  {
   @Override
   public void onClick(ClickEvent event)
   {
    AttributeRuleDialog.show(new AttributeRule(), cls.getModel(), new SelectedAttrubuteRule(){

     @Override
     public void attributeRuleSelected(AttributeRule ar)
     {
      ruleList.addData(new RuleRecord(ar));
     }});
   }
  });
  superTS.addButton(btadd);

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
  private AttributeRule rule;
  
  RuleRecord( AttributeRule r )
  {
   super();
   
   rule=r;
   
   setAttribute("type", rule.getType().name() );
   setAttribute("name", r.toString() );
  }
  
  public AttributeRule getRule()
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
 }
}
