package uk.ac.ebi.age.admin.client.model;


public class QualifierRuleImprint
{
 private AgeAttributeClassImprint attr;
// private RestrictionType type;
 private boolean unique;

 private ModelImprint model;
 
 private int id;
 
 QualifierRuleImprint(ModelImprint modelImprint)
 {
  model=modelImprint;
 }

 public AgeAttributeClassImprint getAttributeClassImprint()
 {
  return attr;
 }

// public RestrictionType getType()
// {
//  return type;
// }
//
// public void setType(RestrictionType type)
// {
//  this.type = type;
// }

 public void setAttributeClassImprint(AgeAttributeClassImprint attr)
 {
  this.attr = attr;
 }

 public ModelImprint getModel()
 {
  return model;
 }

 public int getId()
 {
  return id;
 }

 public void setId(int id)
 {
  this.id = id;
 }

 public boolean isUnique()
 {
  return unique;
 }

 public void setUnique(boolean unique)
 {
  this.unique = unique;
 }
}
