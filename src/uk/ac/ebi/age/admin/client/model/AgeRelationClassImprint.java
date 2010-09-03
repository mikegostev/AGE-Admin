package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeRelationClassImprint implements AgeAbstractClassImprint, IsSerializable, Serializable
{
 private static final long serialVersionUID = 1L;

 private String name;
 private String id;
 private Collection<AgeRelationClassImprint> children;
 private Collection<AgeRelationClassImprint> parents;
 
// private Collection<RestrictionImprint> qualifierRestrictions;
 private ModelImprint model;
 private boolean isAbstract;

 private Collection<String> aliases;
 private Collection<AgeAnnotationImprint> annotations;
 
 private transient Object auxData;

 
 AgeRelationClassImprint(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

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

 public AgeRelationClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeRelationClassImprint>(10);

  AgeRelationClassImprint simp = model.createAgeRelationClassImprint();
  
  simp.addSuperClass(this);
  
  return simp;
 }

 
 void addSubClass(AgeRelationClassImprint simp)
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
  
  simp.addSubClass(this);
 }
 
// public void addAttributeRestriction(RestrictionImprint rst)
// {
//  if( qualifierRestrictions == null )
//   qualifierRestrictions = new ArrayList<RestrictionImprint>(10);
//  
//  qualifierRestrictions.add(rst);
// }
// 
// public Collection<RestrictionImprint> getQualifierRestrictions()
// {
//  return qualifierRestrictions;
// }
//
// public void setQualifierRestrictions(Collection<RestrictionImprint> attributeRestrictions)
// {
//  this.qualifierRestrictions = attributeRestrictions;
// }
 
 public void setModel(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

 public ModelImprint getModel()
 {
  return model;
 }

 public void setAbstract(boolean b)
 {
  isAbstract=b;
 }

 public boolean isAbstract()
 {
  return isAbstract;
 }

 public Object getAuxData()
 {
  return auxData;
 }

 public void setAuxData(Object auxData)
 {
  this.auxData = auxData;
 }

 @Override
 public Collection<AgeRelationClassImprint> getChildren()
 {
  return children;
 }

 @Override
 public Collection< AgeRelationClassImprint> getParents()
 {
  return parents;
 }
 
 @Override
 public void removeChild(AgeAbstractClassImprint cimp)
 {
  if( children != null )
   children.remove(cimp);
 }

 @Override
 public void removeParent(AgeAbstractClassImprint cimp)
 {
  if( parents != null )
  {
   parents.remove(cimp);
  
   if( parents.size() == 0 )
    delete();
  }
 }

 @Override
 public void delete()
 {
  if( getParents() != null )
  {
   for( AgeAbstractClassImprint pcls : getParents() )
    pcls.removeChild( this );
  }
  
  if( getChildren() != null )
  {
   for( AgeAbstractClassImprint pcls : getChildren() )
    pcls.removeParent( this );
  } 
  
  model.removeRelationClassImprint( this );
 }

 @Override
 public void addSuperClass(AgeAbstractClassImprint superClass)
 {
  addSuperClass((AgeRelationClassImprint)superClass);
 }
 
 @Override
 public Collection<String> getAliases()
 {
  return aliases;
 }

 @Override
 public void addAlias(String value)
 {
  if( aliases == null )
   aliases = new ArrayList<String>(5);
  
  aliases.add(value);
 }
 
 @Override
 public void removeAlias(String value)
 {
  if( aliases == null )
   return;
  
  aliases.remove(value);
 }
 
 @Override
 public Collection<AgeAnnotationImprint> getAnnotations()
 {
  return annotations;
 }

 @Override
 public void addAnnotation(AgeAnnotationImprint a)
 {
  if( annotations == null )
   annotations = new ArrayList<AgeAnnotationImprint>(5);
  
  annotations.add(a);
 }

 @Override
 public void removeAnnotation(AgeAnnotationImprint a)
 {
  if( annotations == null )
   return;
  
  annotations.remove(a);
 }
}
