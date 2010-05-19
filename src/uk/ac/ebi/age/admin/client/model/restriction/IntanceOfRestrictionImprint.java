package uk.ac.ebi.age.admin.client.model.restriction;

import uk.ac.ebi.age.admin.client.model.AgeClassImprint;

public class IntanceOfRestrictionImprint extends ObjectRestrictionImprint
{
 private AgeClassImprint ageClassImprint;

 public AgeClassImprint getAgeClassImprint()
 {
  return ageClassImprint;
 }

 public void setAgeClassImprint(AgeClassImprint ageClassImprint)
 {
  this.ageClassImprint = ageClassImprint;
 }
}
