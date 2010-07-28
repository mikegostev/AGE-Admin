package uk.ac.ebi.age.admin.client.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelImprint  implements IsSerializable
{
 private AgeClassImprint rootClass;
 private AgeRelationClassImprint rootRelation;
 private AgeAttributeClassImprint rootAttribute;
 private AgeAnnotationClassImprint rootAnnotation;

 private Set<AgeClassImprint> classes = new HashSet<AgeClassImprint>();

 private Set<AgeAttributeClassImprint> attributes = new HashSet<AgeAttributeClassImprint>();

 private Set<AgeRelationClassImprint> relations = new HashSet<AgeRelationClassImprint>();
 private Set<AgeAnnotationClassImprint> annotations = new HashSet<AgeAnnotationClassImprint>();


 
 public AgeClassImprint getRootClass()
 {
  if( rootClass != null )
   return rootClass;
   
  rootClass = new AgeClassImprint();
  
  classes.add(rootClass);
  rootClass.setModel( this );
  
  return rootClass;
 }

 AgeClassImprint createAgeClassImprint()
 {
  AgeClassImprint ncls = new AgeClassImprint();
  
  classes.add(ncls);
  
  ncls.setModel(this);
  
  return ncls;
 }

 public Collection<AgeClassImprint> getClasses()
 {
  return classes;
 }


 public AgeAttributeClassImprint getRootAttributeClass()
 {
  if( rootAttribute != null )
   return rootAttribute;
   
  rootAttribute = new AgeAttributeClassImprint();
  
  attributes.add(rootAttribute);
  rootAttribute.setModel( this );
  
  return rootAttribute;
 }

 public AgeAnnotationClassImprint getRootAnnotationClass()
 {
  if( rootAnnotation != null )
   return rootAnnotation;
   
  rootAnnotation = new AgeAnnotationClassImprint();
  rootAnnotation.setName("AgeAnnotation");
  rootAnnotation.setAbstract(true);
  
  annotations.add(rootAnnotation);
  rootAnnotation.setModel( this );
  
  return rootAnnotation;
 }


 AgeAttributeClassImprint createAgeAttributClassImprint()
 {
  AgeAttributeClassImprint ncls = new AgeAttributeClassImprint();
  
  attributes.add(ncls);
  
  ncls.setModel(this);

  return ncls;
 }
 
 public Collection<AgeAttributeClassImprint> getAttributes()
 {
  return attributes;
 }


 public AgeRelationClassImprint getRootRelationClass()
 {
  if( rootRelation != null )
   return rootRelation;
   
  rootRelation = new AgeRelationClassImprint();
  
  relations.add(rootRelation);
  rootRelation.setModel( this );
  
  return rootRelation;
 }


 AgeRelationClassImprint createAgeRelationClassImprint()
 {
  AgeRelationClassImprint ncls = new AgeRelationClassImprint();
  
  relations.add(ncls);
  
  ncls.setModel(this);

  
  return ncls;
 }
 
 public AgeAnnotationClassImprint createAgeAnnotationClassImprint()
 {
  AgeAnnotationClassImprint ncls = new AgeAnnotationClassImprint();
  
  annotations.add(ncls);
  
  ncls.setModel(this);

  
  return ncls;
 }
 
 public Collection<AgeRelationClassImprint> getRelations()
 {
  return relations;
 }
 
 public Collection<AgeAnnotationClassImprint> getAnnotations()
 {
  return annotations;
 }

 public void removeClassImprint(AgeClassImprint ageClassImprint)
 {
  classes.remove(ageClassImprint);
 }

 public void removeAttributeClassImprint(AgeAttributeClassImprint cls)
 {
  attributes.remove(cls);
 }

 public void removeRelationClassImprint(AgeRelationClassImprint cls)
 {
  relations.remove(cls);
 }

 public void removeAnnotationClassImprint(AgeAnnotationClassImprint cls)
 {
  annotations.remove(cls);
 }





 
 

// private void addClass( AgeClassImprint rootClass )
// {
//  classes.add(rootClass);
//  
//  rootClass.setModel(this);
//  
//  if( rootClass.getChildren() != null )
//  {
//   for( AgeClassImprint sc : rootClass.getChildren() )
//    addClass( sc );
//  }
// }
 



// public void registerClass()
// {
//  classes.add(rootClass);
//  
//  rootClass.setModel(this);
// }
 
 
}
