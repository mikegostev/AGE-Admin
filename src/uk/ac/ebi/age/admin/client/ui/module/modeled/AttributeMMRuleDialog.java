package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.restriction.AttributeRule;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;

public class AttributeMMRuleDialog extends AttributeRuleDialog
{
 private static AttributeMMRuleDialog instance;
 
 
 public AttributeMMRuleDialog(ModelImprint mod)
 {
  super(mod);
 }

 public RulePanel getRulePanel()
 {
  return new AttributeMMRulePanel(getModel());
 }
 
 
 public static void show(AttributeRule rule, ModelImprint mod, SelectedAttrubuteRule selectedAttrubuteRule)
 {
  if( instance == null )
   instance = new AttributeMMRuleDialog(mod);
  
  if( instance.getModel() != mod )
  {
   instance.destroy();
   instance = new AttributeMMRuleDialog(mod);
  }
  
  instance.setRule( rule );
  instance.setListener(selectedAttrubuteRule);

  instance.show();
 }

 public void close()
 {
  instance=null;
  destroy();
 }

}
