package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeAttributeClassImprint implements IsSerializable
{
 private String name;
 private String id;
 
 private Collection<AgeAttributeClassImprint> parents;
 private Collection<AgeAttributeClassImprint> children;
 
 private Collection<AgeQualifierRestrictionImprint> qualifierRestrictions;

 public String getName()
 {
  return name;
 }

 public void setName(String name)
 {
  this.name = name;
 }

 public String getId()
 {
  return id;
 }

 public void setId(String id)
 {
  this.id = id;
 }

 public Collection<AgeAttributeClassImprint> getParents()
 {
  return parents;
 }

 public void setParents(Collection<AgeAttributeClassImprint> parents)
 {
  this.parents = parents;
 }

 public Collection<AgeAttributeClassImprint> getChildren()
 {
  return children;
 }

 public void setChildren(Collection<AgeAttributeClassImprint> children)
 {
  this.children = children;
 }


 public Collection<AgeQualifierRestrictionImprint> getQualifierRestrictions()
 {
  return qualifierRestrictions;
 }

 public void setQualifierRestrictions(Collection<AgeQualifierRestrictionImprint> attributeRestrictions)
 {
  this.qualifierRestrictions = attributeRestrictions;
 }

 public void addSubClass(AgeAttributeClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeAttributeClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeAttributeClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeAttributeClassImprint>(10);
  
  parents.add(simp);
 }

 public void addRestriction(AgeQualifierRestrictionImprint rst)
 {
  if( qualifierRestrictions == null )
   qualifierRestrictions = new ArrayList<AgeQualifierRestrictionImprint>(10);
  
  qualifierRestrictions.add(rst);
 }
}
