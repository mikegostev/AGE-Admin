package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;

public class ModelImprint
{
 private AgeClassImprint rootClass;
 private Collection<AgeClassImprint> classes;

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
}
