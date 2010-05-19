package uk.ac.ebi.age.admin.client.model.restriction;

import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;

public class FillerRestrictionImprint extends ObjectRestrictionImprint
{

 private ObjectRestrictionImprint filler;
 private AgeRelationClassImprint relation;

 public ObjectRestrictionImprint getFiller()
 {
  return filler;
 }

 public void setFiller(ObjectRestrictionImprint filler)
 {
  this.filler = filler;
 }

 public AgeRelationClassImprint getRelation()
 {
  return relation;
 }

 public void setRelation(AgeRelationClassImprint relation)
 {
  this.relation = relation;
 }


}
