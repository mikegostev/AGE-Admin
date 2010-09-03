package uk.ac.ebi.age.admin.client.model;

import uk.ac.ebi.age.model.RestrictionType;

public class QualifierRule
{
 private AgeAttributeClassImprint attr;
 private RestrictionType type;

 private ModelImprint model;
 
 QualifierRule(ModelImprint modelImprint)
 {
  model=modelImprint;
 }

 public AgeAttributeClassImprint getAttributeClassImprint()
 {
  return attr;
 }

 public RestrictionType getType()
 {
  return type;
 }

 public void setAttributeClassImprint(AgeAttributeClassImprint attr)
 {
  this.attr = attr;
 }

 public void setType(RestrictionType type)
 {
  this.type = type;
 }

 public ModelImprint getModel()
 {
  return model;
 }
}
