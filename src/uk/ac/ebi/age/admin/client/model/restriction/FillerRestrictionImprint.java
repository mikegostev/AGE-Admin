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

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  String op=null;
  
  if( getType() == Type.ONLY )
   op="only";
  else if( getType() == Type.SOME )
   op="some";
  
  sb.append("( ")
  .append(getRelation().getName())
  .append(" ")
  .append(op)
  .append(" ")
  .append(getFiller().toString())
  .append(" )");
  
  return sb.toString();
 }


}
