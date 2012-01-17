package uk.ac.ebi.age.admin.client.ui;

import uk.ac.ebi.age.admin.client.model.AgeAnnotationClassImprint;

public class AnnotationTreeNode extends ImprintTreeNode
{

 public AnnotationTreeNode(AgeAnnotationClassImprint cl)
 {
  super(cl);
 }


 public AgeAnnotationClassImprint getClassImprint()
 {
  return (AgeAnnotationClassImprint)super.getClassImprint();
 }


 @Override
 public String getIcon()
 {
  return AnnotationMetaClassDef.getIcon( getClassImprint() );
 }

}
