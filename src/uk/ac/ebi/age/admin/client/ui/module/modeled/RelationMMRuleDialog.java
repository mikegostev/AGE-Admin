package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedRelationRule;

public class RelationMMRuleDialog extends RelationRuleDialog
{
 private static RelationMMRuleDialog instance;
 
 
 public RelationMMRuleDialog(ModelImprint mod)
 {
  super(mod);
 }

 public RelationRulePanel getRulePanel()
 {
  return new RelationMMRulePanel(getModel().createRelationRuleImprint(RestrictionType.MAY));
 }
 
 
 public static void show(RelationRuleImprint rule, ModelImprint mod, SelectedRelationRule selectedRelRule)
 {
  if( instance == null )
   instance = new RelationMMRuleDialog(mod);
  
  if( instance.getModel() != mod )
  {
   instance.destroy();
   instance = new RelationMMRuleDialog(mod);
  }
  
  instance.setRule( rule );
  instance.setListener(selectedRelRule);

  instance.show();
 }

 public void close()
 {
  instance=null;
  destroy();
 }

}
