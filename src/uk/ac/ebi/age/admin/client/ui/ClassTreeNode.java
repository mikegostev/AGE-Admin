package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;

public class ClassTreeNode extends ImprintTreeNode
{

 public ClassTreeNode(AgeClassImprint cl)
 {
  super(cl);
 }



 public AgeClassImprint getClassImprint()
 {
  return (AgeClassImprint)super.getClassImprint();
 }



 @Override
 public String getIcon()
 {
  return ClassMetaClassDef.getIcon(getClassImprint());
 }

}
