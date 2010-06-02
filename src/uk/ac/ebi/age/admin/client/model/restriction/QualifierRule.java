package uk.ac.ebi.age.admin.client.model.restriction;

import uk.ac.ebi.age.admin.client.model.AgeAttributeClassImprint;

public class QualifierRule
{
 private AgeAttributeClassImprint attr;
 private RestrictionType type;

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

}
