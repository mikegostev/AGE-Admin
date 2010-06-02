package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;

public class RelationTreeNode extends ImprintTreeNode
{

 public RelationTreeNode(AgeRelationClassImprint cl)
 {
  super(cl);
 }


 public AgeRelationClassImprint getClassImprint()
 {
  return (AgeRelationClassImprint)super.getClassImprint();
 }


 @Override
 public String getIcon()
 {
  return RelationMetaClassDef.getIcon( getClassImprint() );
 }

}
