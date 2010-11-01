package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import uk.ac.ebi.age.admin.client.common.ModelPath;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ModelImprint  implements IsSerializable, Serializable, Annotated
{
 public static final String ROOT_CLASS_NAME = "AgeClass";
 public static final String ROOT_ANNOT_NAME = "AgeAnnotation";
 public static final String ROOT_ATTR_NAME = "AgeAttribute";
 public static final String ROOT_REL_NAME = "AgeRelation";

 private static final long serialVersionUID = 1L;


 private int idGen=1;
 
 private transient ModelPath storePath; 

 private AgeClassImprint rootClass;
 private AgeRelationClassImprint rootRelation;
 private AgeAttributeClassImprint rootAttribute;
 private AgeAnnotationClassImprint rootAnnotation;

 private Set<AgeClassImprint> classes = new HashSet<AgeClassImprint>();

 private Set<AgeAttributeClassImprint> attributes = new HashSet<AgeAttributeClassImprint>();

 private Set<AgeRelationClassImprint> relations = new HashSet<AgeRelationClassImprint>();
 private Set<AgeAnnotationClassImprint> annotationClasses = new HashSet<AgeAnnotationClassImprint>();

 private Collection<AgeAnnotationImprint> annotations = new ArrayList<AgeAnnotationImprint>();
 
 private String name;
 
 public AgeClassImprint getRootClass()
 {
  if( rootClass != null )
   return rootClass;
   
  rootClass = createAgeClassImprint();
  rootClass.setAbstract(true);
  rootClass.setName("AgeClass");
  
  classes.add(rootClass);
  rootClass.setModel( this );
  
  return rootClass;
 }

 AgeClassImprint createAgeClassImprint()
 {
  AgeClassImprint ncls = new AgeClassImprint( this );
  
  ncls.setId("Class"+(idGen++));
  
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
   
  rootAttribute = createAgeAttributClassImprint();
  rootAttribute.setAbstract(true);
  rootAttribute.setName("AgeAttributeClass");

  attributes.add(rootAttribute);
  rootAttribute.setModel( this );
  
  return rootAttribute;
 }

 public AgeAnnotationClassImprint getRootAnnotationClass()
 {
  if( rootAnnotation != null )
   return rootAnnotation;
   
  rootAnnotation = createAgeAnnotationClassImprint();
  rootAnnotation.setName("AgeAnnotation");
  rootAnnotation.setAbstract(true);
  
  annotationClasses.add(rootAnnotation);
  rootAnnotation.setModel( this );
  
  return rootAnnotation;
 }


 AgeAttributeClassImprint createAgeAttributClassImprint()
 {
  AgeAttributeClassImprint ncls = new AgeAttributeClassImprint( this );
  
  ncls.setId("Attribute"+(idGen++));
 
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
   
  rootRelation = createAgeRelationClassImprint();
  rootRelation.setAbstract(true);
  rootRelation.setName("AgeRelationClass");

  relations.add(rootRelation);
  rootRelation.setModel( this );
  
  return rootRelation;
 }


 AgeRelationClassImprint createAgeRelationClassImprint()
 {
  AgeRelationClassImprint ncls = new AgeRelationClassImprint( this );

  ncls.setId("Relation"+(idGen++));
  
  relations.add(ncls);
  
  ncls.setModel(this);

  
  return ncls;
 }
 
 public AgeAnnotationClassImprint createAgeAnnotationClassImprint()
 {
  AgeAnnotationClassImprint ncls = new AgeAnnotationClassImprint( this );
  
  ncls.setId("Annotation"+(idGen++));
  
  annotationClasses.add(ncls);
  
  ncls.setModel(this);

  
  return ncls;
 }
 
 public AgeAnnotationImprint createAgeAnnotationImprint( AgeAnnotationClassImprint cls )
 {
  AgeAnnotationImprint ncls = new AgeAnnotationImprint(cls);
  
  return ncls;
 }

 public AttributeRuleImprint createAttributeRuleImprint(RestrictionType may)
 {
  return new AttributeRuleImprint(may, this);
 }


 public RelationRuleImprint createRelationRuleImprint(RestrictionType may)
 {
  return new RelationRuleImprint(may, this);
 }

 public QualifierRuleImprint createQualifierRuleImprint()
 {
  return new QualifierRuleImprint( this );
 }

 
 public Collection<AgeRelationClassImprint> getRelations()
 {
  return relations;
 }
 
 public Collection<AgeAnnotationClassImprint> getAnnotationClasses()
 {
  return annotationClasses;
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
  annotationClasses.remove(cls);
 }

 @Override
 public ModelImprint getModel()
 {
  return this;
 }

 @Override
 public void removeAnnotation(AgeAnnotationImprint annotation)
 {
  annotations.remove(annotation);
 }

 @Override
 public void addAnnotation(AgeAnnotationImprint annt)
 {
  annotations.add(annt);
 }

 @Override
 public Collection<AgeAnnotationImprint> getAnnotations()
 {
  return annotations;
 }

 public String getName()
 {
  return name;
 }

 public void setName(String name)
 {
  this.name = name;
 }
 
 public ModelPath getStorePath()
 {
  return storePath;
 }

 public void setStorePath(ModelPath storePath)
 {
  this.storePath = storePath;
 }

 public int getIdGen()
 {
  return idGen;
 }

 public void setIdGen(int idGen)
 {
  this.idGen = idGen;
 }

 public int generateId()
 {
  return idGen++;
 } 
}
