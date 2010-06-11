package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.restriction.AttributeRule;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;

public class AttributeMNOTRuleDialog extends AttributeRuleDialog
{
 private static AttributeMNOTRuleDialog instance;
 
 
 public AttributeMNOTRuleDialog(ModelImprint mod)
 {
  super(mod);
  setHeight(570);

 }

 public RulePanel getRulePanel()
 {
  return new AttributeMNOTRulePanel(getModel());
 }
 
 
 public static void show(AttributeRule rule, ModelImprint mod, SelectedAttrubuteRule selectedAttrubuteRule)
 {
  if( instance == null )
   instance = new AttributeMNOTRuleDialog(mod);
  
  if( instance.getModel() != mod )
  {
   instance.destroy();
   instance = new AttributeMNOTRuleDialog(mod);
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
