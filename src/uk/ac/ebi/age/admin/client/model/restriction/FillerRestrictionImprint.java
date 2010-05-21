package uk.ac.ebi.age.admin.client.model.restriction;

import uk.ac.ebi.age.admin.client.model.AgeRelationClassImprint;

public class FillerRestrictionImprint extends RestrictionImprint
{

 private RestrictionImprint filler;
 private AgeRelationClassImprint relation;

 public RestrictionImprint getFiller()
 {
  return filler;
 }

 public void setFiller(RestrictionImprint filler)
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
