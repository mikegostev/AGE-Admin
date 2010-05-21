package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.RestrictionImprint;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeClassImprint implements IsSerializable, AgeAbstractClassImprint
{
 private String name;
 private String id;
 
 private Collection<AgeClassImprint> parents;
 private Collection<AgeClassImprint> children;
 
 private Collection<RestrictionImprint> objectRestrictions;
 private Collection<RestrictionImprint> attributeRestrictions;

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

 public Collection<RestrictionImprint> getObjectRestrictions()
 {
  return objectRestrictions;
 }

 public void setObjectRestrictions(Collection<RestrictionImprint> restrictions)
 {
  this.objectRestrictions = restrictions;
 }

 public Collection<RestrictionImprint> getAttributeRestrictions()
 {
  return attributeRestrictions;
 }

 public void setAttributeRestrictions(Collection<RestrictionImprint> attributeRestrictions)
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

 public void addRestriction(RestrictionImprint rst)
 {
  if( objectRestrictions == null )
   objectRestrictions = new ArrayList<RestrictionImprint>(10);
  
  objectRestrictions.add(rst);
 }
 
 public void addAttributeRestriction(RestrictionImprint rst)
 {
  if( attributeRestrictions == null )
   attributeRestrictions = new ArrayList<RestrictionImprint>(10);
  
  attributeRestrictions.add(rst);
 }

}
