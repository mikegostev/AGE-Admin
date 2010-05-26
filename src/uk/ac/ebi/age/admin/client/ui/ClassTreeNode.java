package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;

public class ClassTreeNode extends ImprintTreeNode
{

 public ClassTreeNode(AgeClassImprint cl)
 {
  super(cl);
 }

 @Override
 public String getMetaClassname()
 {
  return "class";
 }

 public AgeClassImprint getClassImprint()
 {
  return (AgeClassImprint)super.getClassImprint();
 }

}
