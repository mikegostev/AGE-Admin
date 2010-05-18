package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelImprint  implements IsSerializable
{
 private AgeClassImprint rootClass;
 private Collection<AgeClassImprint> classes;

 private AgeAttributeClassImprint rootAttribute;
 private Collection<AgeAttributeClassImprint> attributes;

 private AgeRelationClassImprint rootRelation;
 
 public AgeClassImprint getRootClass()
 {
  return rootClass;
 }

 public Collection<AgeClassImprint> getClasses()
 {
  return classes;
 }

 public void setRootClass(AgeClassImprint rootClass)
 {
  this.rootClass = rootClass;
 }

 public void setRootAttributeClass(AgeAttributeClassImprint atImp)
 {
  rootAttribute = atImp;
 }

 public void setRootRelationClass(AgeRelationClassImprint relImp)
 {
  rootRelation=relImp;
 }
}
