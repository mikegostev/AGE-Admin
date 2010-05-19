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


}
