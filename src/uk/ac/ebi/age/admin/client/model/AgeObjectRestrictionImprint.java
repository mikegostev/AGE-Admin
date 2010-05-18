package uk.ac.ebi.age.admin.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeObjectRestrictionImprint implements IsSerializable
{
 public enum Type
 {
  SOME,
  ONLY,
  MIN,
  MAX,
  EXACTLY,
  OR,
  AND,
  NOT
 }
 
 
 private AgeRelationClassImprint relation;
 
 private int minCardinality;
 private int maxCardinality;
 
 private Type type;
 
 private AgeObjectRestrictionImprint filler;

 
 public AgeRelationClassImprint getRelation()
 {
  return relation;
 }

 public void setRelation(AgeRelationClassImprint relation)
 {
  this.relation = relation;
 }

 public int getMinCardinality()
 {
  return minCardinality;
 }

 public void setMinCardinality(int minCardinality)
 {
  this.minCardinality = minCardinality;
 }

 public int getMaxCardinality()
 {
  return maxCardinality;
 }

 public void setMaxCardinality(int maxCardinality)
 {
  this.maxCardinality = maxCardinality;
 }

 public Type getType()
 {
  return type;
 }

 public void setType(Type type)
 {
  this.type = type;
 }

 public AgeObjectRestrictionImprint getFiller()
 {
  return filler;
 }

 public void setFiller(AgeObjectRestrictionImprint filler)
 {
  this.filler = filler;
 }
}
