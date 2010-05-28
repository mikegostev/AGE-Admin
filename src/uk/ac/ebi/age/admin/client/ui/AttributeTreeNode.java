package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;

public class AttributeTreeNode extends ImprintTreeNode
{

 public AttributeTreeNode(AgeAttributeClassImprint cl)
 {
  super(cl);
 }

 @Override
 public String getMetaClassname()
 {
  return "attribute";
 }

 public AgeAttributeClassImprint getClassImprint()
 {
  return (AgeAttributeClassImprint)super.getClassImprint();
 }

}
