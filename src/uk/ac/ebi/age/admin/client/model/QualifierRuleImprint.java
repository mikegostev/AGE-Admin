package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class QualifierRuleImprint implements Serializable, IsSerializable
{
 private AgeAttributeClassImprint attr;
// private RestrictionType type;
 private boolean unique;

 private ModelImprint model;
 
 private int id;
 
 QualifierRuleImprint()
 {}
 
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
