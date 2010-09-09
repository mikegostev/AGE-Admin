package uk.ac.ebi.age.admin.client.model.restriction;


public class CardinalityRestrictionImprint extends FillerRestrictionImprint
{

 private int cardinality;

 public int getCardinality()
 {
  return cardinality;
 }

 public void setCardinality(int minCardinality)
 {
  this.cardinality = minCardinality;
 }

 public String toString()
 {
  StringBuilder sb = new StringBuilder();
  
  String op=null;
  
  if( getType() == Type.EXACTLY )
   op="exactly";
  else if( getType() == Type.MIN )
   op="min";
  else if( getType() == Type.MAX )
   op="max";
  
  sb.append("( ")
  .append(getRelation().getName())
  .append(" ")
  .append(op)
  .append(" ")
  .append(cardinality)
  .append(" ")
  .append(getFiller().toString())
  .append(" )");
  
  return sb.toString();
 }

}
