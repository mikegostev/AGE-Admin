package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import uk.ac.ebi.age.admin.client.model.restriction.AttributeRule;
import uk.ac.ebi.age.admin.client.model.restriction.RelationRule;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeClassImprint implements IsSerializable, Serializable, AgeAbstractClassImprint
{
 private String name;
 private String id;
 
 private boolean isAbstract;
 
 private Collection<AgeClassImprint> parents;
 private Collection<AgeClassImprint> children;
 
 private Collection<RelationRule> objectRestrictions;
 private Collection<AttributeRule> attributeRestrictions;
 
 private Collection<String> aliases;
 private Collection<AgeAnnotationImprint> annotations;

 private ModelImprint model;
 
 private transient Object auxData;

 AgeClassImprint()
 {}
 
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

// public void setChildren(Collection<AgeClassImprint> children)
// {
//  this.children = children;
// }

 public Collection<RelationRule> getRelationRules()
 {
  return objectRestrictions;
 }

 public void setRelationRules(Collection<RelationRule> restrictions)
 {
  this.objectRestrictions = restrictions;
 }

 public Collection<AttributeRule> getAttributeRule()
 {
  return attributeRestrictions;
 }

 public void setAttributeRestrictions(Collection<AttributeRule> attributeRestrictions)
 {
  this.attributeRestrictions = attributeRestrictions;
 }

 public AgeClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeClassImprint>(10);

  AgeClassImprint simp = model.createAgeClassImprint();
  
  simp.addSuperClass(this);
  
  return simp;
 }
 
 void addSubClass(AgeClassImprint simp)
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
  simp.addSubClass(this);
 }

 public void addRelationRule(RelationRule rst)
 {
  if( objectRestrictions == null )
   objectRestrictions = new ArrayList<RelationRule>(10);
  
  objectRestrictions.add(rst);
 }
 
 public void addAttributeRule(AttributeRule rst)
 {
  if( attributeRestrictions == null )
   attributeRestrictions = new ArrayList<AttributeRule>(10);
  
  attributeRestrictions.add(rst);
 }

 public boolean isAbstract()
 {
  return isAbstract;
 }

 public void setAbstract(boolean isAbstract)
 {
  this.isAbstract = isAbstract;
 }

 void setModel(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

 public ModelImprint getModel()
 {
  return model;
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
  
  model.removeClassImprint( this );
 }

 @Override
 public void addSuperClass(AgeAbstractClassImprint superClass)
 {
  addSuperClass((AgeClassImprint)superClass);
 }

 public void removeAttribiteRule(AttributeRule rule)
 {
  if( attributeRestrictions != null )
   attributeRestrictions.remove(rule);
 }


 public void removeRelationRule(RelationRule rule)
 {
  if( objectRestrictions != null )
   objectRestrictions.remove(rule);
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
