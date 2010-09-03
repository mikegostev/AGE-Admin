package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AttributeRule;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;
import uk.ac.ebi.age.model.RestrictionType;

public class AttributeMNOTRuleDialog extends AttributeRuleDialog
{
 private static AttributeMNOTRuleDialog instance;
 
 
 public AttributeMNOTRuleDialog(ModelImprint mod)
 {
  super(mod);
  setHeight(570);

 }

 public AttributeRulePanel getRulePanel()
 {
  return new AttributeMNOTRulePanel(getModel().createAttributeRuleImprint(RestrictionType.MUSTNOT));
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
