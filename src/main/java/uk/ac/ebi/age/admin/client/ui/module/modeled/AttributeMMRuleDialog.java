package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.AttributeRuleImprint;
import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedAttrubuteRule;

public class AttributeMMRuleDialog extends AttributeRuleDialog
{
 private static AttributeMMRuleDialog instance;
 
 
 public AttributeMMRuleDialog(ModelImprint mod)
 {
  super(mod);
 }

 @Override
 public AttributeRulePanel getRulePanel()
 {
  return new AttributeMMRulePanel(getModel().createAttributeRuleImprint(RestrictionType.MAY));
 }
 
 
 public static void show(AttributeRuleImprint rule, ModelImprint mod, SelectedAttrubuteRule selectedAttrubuteRule)
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

//  instance.layoutChildren("aa");
  instance.show();
 }

 @Override
 public void close()
 {
  instance=null;
  destroy();
 }

}
