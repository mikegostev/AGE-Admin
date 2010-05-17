package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeClassImprint implements IsSerializable
{
 private String name;
 private String id;
 
 private Collection<AgeClassImprint> parents;
 private Collection<AgeClassImprint> children;
 
 private Collection<AgeObjectRestrictionImprint> objectRestrictions;
 private Collection<AgeAttributeRestrictionImprint> attributeRestrictions;

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

 public Collection<AgeClassImprint> getParents()
 {
  return parents;
 }

 public void setParents(Collection<AgeClassImprint> parents)
 {
  this.parents = parents;
 }

 public Collection<AgeClassImprint> getChildren()
 {
  return children;
 }

 public void setChildren(Collection<AgeClassImprint> children)
 {
  this.children = children;
 }

 public Collection<AgeObjectRestrictionImprint> getObjectRestrictions()
 {
  return objectRestrictions;
 }

 public void setObjectRestrictions(Collection<AgeObjectRestrictionImprint> restrictions)
 {
  this.objectRestrictions = restrictions;
 }

 public Collection<AgeAttributeRestrictionImprint> getAttributeRestrictions()
 {
  return attributeRestrictions;
 }

 public void setAttributeRestrictions(Collection<AgeAttributeRestrictionImprint> attributeRestrictions)
 {
  this.attributeRestrictions = attributeRestrictions;
 }

 public void addSubClass(AgeClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeClassImprint>(10);
  
  parents.add(simp);
 }

 public void addRestriction(AgeObjectRestrictionImprint rst)
 {
  if( objectRestrictions == null )
   objectRestrictions = new ArrayList<AgeObjectRestrictionImprint>(10);
  
  objectRestrictions.add(rst);
 }
}
