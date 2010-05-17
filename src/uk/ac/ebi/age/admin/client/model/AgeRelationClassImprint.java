package uk.ac.ebi.age.admin.client.model;

import java.util.ArrayList;
import java.util.Collection;

public class AgeRelationClassImprint
{
 private String name;
 private String id;
 private Collection<AgeRelationClassImprint> children;
 private Collection<AgeRelationClassImprint> parents;

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

 public void addSubClass(AgeRelationClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeRelationClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeRelationClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeRelationClassImprint>(10);
  
  parents.add(simp);
 }

}
