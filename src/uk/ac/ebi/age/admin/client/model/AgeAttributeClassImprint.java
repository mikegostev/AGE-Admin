package uk.ac.ebi.age.admin.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AgeAttributeClassImprint implements AgeAbstractClassImprint,IsSerializable, Serializable
{

 private static final long serialVersionUID = 1L;

 private String name;
 private String id;
 
 private Collection<AgeAttributeClassImprint> parents;
 private Collection<AgeAttributeClassImprint> children;
 
 private Collection<String> aliases;
 private Collection<AgeAnnotationImprint> annotations;

 private Collection<AttributeRuleImprint> attributeRestrictions;

 
// private Collection<RestrictionImprint> qualifierRestrictions;
 private ModelImprint model;

 private AttributeType type;
 
 private transient Object auxData;
 
 
 AgeAttributeClassImprint(ModelImprint modelImprint)
 {
  model=modelImprint;
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

 public AgeAttributeClassImprint createSubClass()
 {
  if(children == null)
   children = new ArrayList<AgeAttributeClassImprint>(10);

  AgeAttributeClassImprint simp = model.createAgeAttributClassImprint();
  
  simp.addSuperClass(this);
  simp.setType(AttributeType.ABSTRACT);
  
  return simp;
 }

 
 public Collection<AgeAttributeClassImprint> getParents()
 {
  return parents;
 }

 public Collection<AgeAttributeClassImprint> getChildren()
 {
  return children;
 }



// public Collection<RestrictionImprint> getQualifierRestrictions()
// {
//  return qualifierRestrictions;
// }
//
// public void setQualifierRestrictions(Collection<RestrictionImprint> attributeRestrictions)
// {
//  this.qualifierRestrictions = attributeRestrictions;
// }

 void addSubClass(AgeAttributeClassImprint simp)
 {
  if( children == null )
   children = new ArrayList<AgeAttributeClassImprint>(10);
  
  children.add(simp);
 }

 public void addSuperClass(AgeAttributeClassImprint simp)
 {
  if( parents == null )
   parents = new ArrayList<AgeAttributeClassImprint>(10);
  
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
 
 public void setModel(ModelImprint modelImprint)
 {
  model = modelImprint;
 }

 public ModelImprint getModel()
 {
  return model;
 }

 public boolean isAbstract()
 {
  return type==AttributeType.ABSTRACT;
 }
 
 public void setAbstract(boolean b)
 {
  if( b )
   type=AttributeType.ABSTRACT;
 }


 public AttributeType getType()
 {
  return type;
 }
 
 public void setType( AttributeType t )
 {
  type=t;
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
  
  model.removeAttributeClassImprint( this );
 }
 
 @Override
 public void addSuperClass(AgeAbstractClassImprint superClass)
 {
  addSuperClass((AgeAttributeClassImprint)superClass);
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
 
 public Collection<AttributeRuleImprint> getAttributeRules()
 {
  return attributeRestrictions;
 }
 
 public void addAttributeRule(AttributeRuleImprint rst)
 {
  if( attributeRestrictions == null )
   attributeRestrictions = new ArrayList<AttributeRuleImprint>(10);
  
  attributeRestrictions.add(rst);
 }

 public void removeAttribiteRule(AttributeRuleImprint rule)
 {
  if( attributeRestrictions != null )
   attributeRestrictions.remove(rule);
 }

}
