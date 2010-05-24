package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelImprint  implements IsSerializable
{
 private AgeClassImprint rootClass;
 private Set<AgeClassImprint> classes = new HashSet<AgeClassImprint>();

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
  
  addClass(rootClass);
 }

 private void addClass( AgeClassImprint rootClass )
 {
  classes.add(rootClass);
  
  rootClass.setModel(this);
  
  if( rootClass.getChildren() != null )
  {
   for( AgeClassImprint sc : rootClass.getChildren() )
    addClass( sc );
  }
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
