package uk.ac.ebi.age.admin.client.model.restriction;

import uk.ac.ebi.age.admin.client.model.AgeAbstractClassImprint;

public class IntanceOfRestrictionImprint extends RestrictionImprint
{
 private AgeAbstractClassImprint ageAClassImprint;

 public AgeAbstractClassImprint getAgeAbstractClassImprint()
 {
  return ageAClassImprint;
 }

 public void setAgeAbstractClassImprint(AgeAbstractClassImprint ageClassImprint)
 {
  this.ageAClassImprint = ageClassImprint;
 }
 
 public String toString()
 {
  return ageAClassImprint.getName();
 }
}
