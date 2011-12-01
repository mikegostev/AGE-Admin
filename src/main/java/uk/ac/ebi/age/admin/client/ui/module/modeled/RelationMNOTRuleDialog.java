package uk.ac.ebi.age.admin.client.ui.module.modeled;

import uk.ac.ebi.age.admin.client.model.ModelImprint;
import uk.ac.ebi.age.admin.client.model.RelationRuleImprint;
import uk.ac.ebi.age.admin.client.model.RestrictionType;
import uk.ac.ebi.age.admin.client.ui.SelectedRelationRule;

public class RelationMNOTRuleDialog extends RelationRuleDialog
{
 private static RelationMNOTRuleDialog instance;
 
 
 public RelationMNOTRuleDialog(ModelImprint mod)
 {
  super(mod);
  setHeight(620);

 }

 public RelationRulePanel getRulePanel()
 {
  return new RelationMNOTRulePanel(getModel().createRelationRuleImprint(RestrictionType.MUSTNOT));
 }
 
 
 public static void show(RelationRuleImprint rule, ModelImprint mod, SelectedRelationRule lsnr)
 {
  if( instance == null )
   instance = new RelationMNOTRuleDialog(mod);
  
  if( instance.getModel() != mod )
  {
   instance.destroy();
   instance = new RelationMNOTRuleDialog(mod);
  }
  
  instance.setRule( rule );
  instance.setListener(lsnr);

  instance.show();
 }

 public void close()
 {
  instance=null;
  destroy();
 }
}
